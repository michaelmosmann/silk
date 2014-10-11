---
layout : userguide
title : Services
---
# Services

<abstract>
The service concept comes close to what is sometimes called micro-services. 
Usual java method become a first class service object with a generic interface. 
It decouples implementation and usage wiring by type. Thereby wiring is automatic 
and refactoring-safe since method names or parameter order are irrelevant.
</abstract>

## Providing Services
Services are usual methods. As a convention or basis of the concept each service
method has to have a unique combination of one of the parameter types and the
return type of the method. It is this combination a particular service is 
identified with. To not become dependent on a service abstraction of the 
dependency injection library an application should declare its own service
interface. In almost all cases this will look like the following:

{% highlight java %}
	interface Service<P, R> {

		R exec( P param );
	}
{% endhighlight %}

The generic `P` is the parameter, the generic `R` the return type that together
make the service unique. The method implementing a concrete service can have 
any signature as long as it also has one of type `P` and the return type `R`.

Here is an example of a service that handles user login:

{% highlight java %}
Status login(LoginCredentials login, UserRepository users) {
  // lookup user and check credentials
}
{% endhighlight %}

It plays nicely with the service concept to create a _input parameters object_
type, like `LoginCredentials` in the example above. As just the login will have
such a parameter the return type (here `Status`) can be a common one used by
many services. 

Instead of making use of fields of the enclosing class (as it is usually done)
to fetch data etcetera such dependencies of the `login` functionality can be
added as further parameters to the service method. On invocation such additional
parameters will be resolved and passed automatically provided that they have
been bound properly as usual.

To get this automatic linkage between a service caller and a service provider
the container has to know about the existence of service methods.
This is done in a `ServiceModule` by using `bindServiceMethodsIn` with the
class that contains one or more service methods. 

In addition the service interface (defined by the application) has to be 
connected to the service supplier by a small glue code adapter class; 
in the below example this is called `MyServiceSupplier`:

{% highlight java %}
class MyServiceModule extends ServiceModule {

	@Override
	protected void declare() {
		bindServiceMethodsIn( MyServicesClass.class );
		starbind( Service.class ).toSupplier( MyServiceSupplier.class );
	}
}
{% endhighlight %}

A complete example implementation can be found in `TestServiceBinds`. The
adapter forwards a call to `exec` of the `Service` class (as in this example)
to a `invoke` call on a `ServiceMethod`, what is silk's internal abstraction
of a service method. In principle this could be used directly but as it causes
dependencies in the wrong direction the extra indirection of an adapter should 
be preferred.

## Using Services
Once services have been bound as described above it becomes very easy to use 
them. Instead of directly calling the function implementing a service (like 
`login`) and thereby becoming dependent upon the implementing class (and code
artefact it is contained within) the service abstraction created (here 
`Service`) is used. Continuing the above example a user of the _login_ service
asks for a `Service<Status,LoginCredentials`. This can be a constructor parameter
or in the case of another service also a parameter of another service method.

A user of the login service could look like this:
{% highlight java %}
class LoginController {

	private Service<Status,LoginCredentials> loginService;
	/* constructor etc. */
	void login(/* ... */) {
		Status s = loginService.exec(new LoginCredentials(user, password));
		// do something with s
	}
}
{% endhighlight %}

Now effectively the service user does not need to know about the implementer of
the _login_ service. Only the parameter passed to the service 
(`LoginCredentials`) and the return type needs to be known. This are record like
value objects that can be contained in a common module as value objects do not
depend upon business logic implementation code. 

Services can naturally also be used with other service methods. 

{% highlight java %}
Users teenagers(TeenagerRange r, Service<Users, UserProperties> users) {
	// use users to filter teenagers in the given range
}
{% endhighlight %}

This allows flexible service composition. Not only are the dependencies of
different services decoupled from a particular implementation but this allows
also to have any graph of dependencies between services (including cycles) 
without having to resolve or care about them. 

Another benefit of services that changes to actual implementation, as 
introducing another parameter when a service becomes logically dependent upon
a new functionality will not cascade out to the users of services as the wiring
is not explicitly defined but given through the types. The container will take
care of such changes and it will just work. 

Last but not least the service pattern naturally makes the service functions
nicely decoupled well testable code close to the idea of a pure function.
In tests all _dependencies_ of the computation are passed directly to the function.
All that goes in and out is simple to control for a test so that it can be
implemented with a stub or checked for results/calls/changes without requiring
a DI container or mocking framework for testing.

## Satisfying Cross Cutting Concerns
The service concept provides a unified interface for all processes wired to
a service method. This allows to do AOP like interception without the pain of
bytecode manipulation etcetera. The `ServiceInvocation` interface gives 
access to individual invocations of service methods so that cross cutting 
concerns like logging or transaction handing could be added. 
The `TestServiceInvocationBinds` shows an example of this. 
This is a very powerful feature that has to be discovered further to saddle 
with a good way to plug in and do interceptions of service method invocations.

## Alternatives
The usage of services makes most sense when one fully commits to use it for all
business layer procedures. This might be a too large step to take that might 
also pay off when the application does benefit largely from the strong decoupling
provided by services. 

An alternative to services is to use `require`-`provide` bindings as they are
described [here](binds.html#require). These also offer a loose coupling, that
in their case in based on interfaces.


<a class='next' href="data.html"><span class="fa fa-chevron-right"></span>Data <br/>Types</a>
