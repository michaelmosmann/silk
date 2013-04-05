---
layout : userguide
title : Introduction
---
# Introduction

<abstract>
This introduction will explain the general features and concepts deep enough to use it well. 
The details and more advanced features will be described later on.
There are a lot of noteworthy characteristics - like Silks general immutability - that will not be discussed here since this introduction 
tries to give you a good overview in the **usage of Silk**. With a deeper understanding you'll get into the implications of all it's characteristics bit by bit.    
</abstract>

<table class='toc'>
	<tr><th>#<a href="#binds">Bindings</a></th><td></td></tr>
	<tr><th>#<a href="#inject">Injection</a></th><td></td></tr>
	<tr><th>#<a href="#modularity">Modularity</a></th><td></td></tr>
	<tr><th>#<a href="#scopes">Scopes</a></th><td></td></tr>
	<tr><th>#<a href="#services">Services</a></th><td></td></tr>
	<tr><th>#<a href="#data">Data Types</a></th><td></td></tr>
	<tr><th>#<a href="#customise">Customization</a></th><td></td></tr>
</table>


**Note!**
The examples given will make use of the `BinderModule` and `BootstrapperBundle` base classes for `Module`s and `Bundle`s. 
Implementing the interfaces directly would result in code that looks a little different, less fluent.

## <a id="binds"></a> Bindings
To _bind_ means to associate a type (usually an interface) with a _strategy_  to create an instance that can be used when a dependency of the bound type is encountered. 
These _bindings_ are defined using a fluent interface, the `Binder`. A basic binding form of such a binding has a `bind` and a `to` clause like this:
{% highlight java %}
	bind( Some.class ).to(...);
{% endhighlight %}
For both, the `bind` and the `to` clause different arguments can be used to vary meaning and behaviour.   
All bindings are declared separated from your application code in so called `Module` classes (see modularity below).
There will be **no `@annotations`** within your code to guide the dependency injection! This is a key concept in Silk that makes it very different from other frameworks.

### Supplying instances (the `to` clause)
Out of the box there are a handful of different ways to use the `to` clause.
{% highlight java %}
bind( Integer.class ).to( 42 ); // a constant
bind( Number.class ).to( Float.class ); // as a link to a sub-type that is bound itself to something
Constructor<MyClass> constructor = //... from whatever
bind( MyClass.class).to( constructor );
bind( MyOtherClass.class).toConstructor(); // that is chosen by Silk's Inspector
{% endhighlight %}
The majority of bindings should make use of _linked_ bindings and the `toConstructor()` method so that you benefit from all of Silk's features.
Therefore you can describe in more detail what instances you want to be passed to a constructor by adding `Parameter`s to the `toConstructor` method:
{% highlight java %}
bind( Car.class ).toConstructor( Type.raw( ElectricEngine.class ) );
{% endhighlight %}
Now an instance of `ElectricEngine` will be injected as `Engine` into the `Car` object. 
There are more advanced _hints_ possible that will be explained <a href="binds.html#basics">later during the tour</a>.
But is important to understand that those additional informations **do not describe the constructor's signature!** 
They are just added in exception cases when special specific instances are needed to make Silk understand what you want. 
The usual case is to **not** add `Parameter` _hints_.   

#### Supply dynamic instances
In a few cases the instance itself is yield somewhat dynamically. It is produced or chosen by a custom strategy. 
Silk offers three different abstractions on different levels of details that can be used:  
{% highlight java %}
bind( Bar.class ).to( new BarProvider() ); // using a Provider
bind( Float.class ).to( new FloatFactory() ); // using a Factory
bind( Foo.class ).to( FooSupplier.class ); // using a Supplier
{% endhighlight %}
The most powerful is a `Supplier`. This is the core abstraction everything is based on. 
A `Factory` is somewhat simpler and allows suppling instances based on the `Instance` to inject and the one receiving the injected instance.
The `Provider` is similar to those known from other DI frameworks. While _providers_ can be used to supply instances they are no core concept in Silk!
But it is easy to add the _provider_ behavior known from _google guice_ as well see soon. 

### Handle multiple instances 
#### ...of the same type
When there are multiple instances of the same type you can `Name` them, and _tell_ Silk which one it should take when injecting into another instance:   
{% highlight java %}bind( named("pi"), float.class ).to( 3.1415f );
bind( named("e"), float.class ).to( 2.7182f );
injectingInto( RadiusCalculation.class ).bind( float.class ).to( named("pi"), float.class );
//...
{% endhighlight %}
Thereby `RadiusCalculation` itself doesn't need to point out what `float` is needed. 
It is also allowed to use interfaces in the `injectingInto` clause. Than this binding applies whenever the injected type is an `instanceof` the interface type given in the clause.  

#### ...within different owners
Sometimes there is more than one receiving instance and even though all have the same type they should get a specific instance each for one of their dependencies.
Or the dependency depends on the hierarchy of instances. In such cases the `winthin` clause can be used:
{% highlight java %}
injectingInto( Child.class ).within( Parent.class ).bind( X.class ).to( SpecialX.class );
injectingInto( named("a"), Child.class ).within( named("b"), Parent.class ).bind( X.class ).to( AxInB.class );
{% endhighlight %}
(The test `TestParentTargetBinds` shows more detailed examples)

#### ...in different packages
In case there already is a binding for a specific type but within your implementation you want to bind and use another instance. 
A good way to solve this is to make a _package localised_ binding:
{% highlight java %}
	inPackageOf(MyClass.class).bind( Some.class ).to(MyImplementation.class);
{% endhighlight %}
Or the bind can made available for a package and everything contained:
{% highlight java %}
	inPackageAndSubPackagesOf(MyClass.class).bind( Some.class ).to(MyImplementation.class);
{% endhighlight %}
These concepts can also be combined to create even more specific exceptions from a general bind.  

#### ...as collection
Sometimes it is intended to bind multiple implementations for one super class or interface and all of them should be injected together.
Here `multibind` can be used to define the members of the collection we get injected:
{% highlight java %}
multibind( Fruit.class ).to( Apple.class );
multibind( Fruit.class ).to( Orange.class );
{% endhighlight %}
The `multibind` calls can of cause appear in different `Module`s or in the same. 
The constructor of a class dependent on _fruits_ could look like this:
{% highlight java %}
public Mixer(Fruit[] fruits) {
   //...
}
{% endhighlight %}
With a little more also `List`s or `Set`s can be injected. Within one of your `Bundle`s you just install a _bridge_ from `T[]` to `Collection<T>` what can be done for `java.util` lists and sets using:
{% highlight java %}
	install(BuildinBundle.LIST);
{% endhighlight %}
and/or
{% highlight java %}
	install(BuildinBundle.SET);
{% endhighlight %}
and/or you install your own _bridge_ that makes _your collection_ type work as well:
{% highlight java %}
per( Scoped.DEPENDENCY_TYPE ).starbind( MyList.class ).to( MyListSupplier.class );
{% endhighlight %}
Here we meet the `Supplier` again. Such a _bridge_ is very simple to add by extending the `ArrayBridgeSupplier`:
{% highlight java %}
class MyListSupplier extends ArrayBridgeSupplier<MyList<?>> {
	@Override
	<E> MyList<E> bridge( E[] elements ) {
		return new MyListImpl<E>( elements ); // assuming there is a constructor that takes an array 
	}
}
{% endhighlight %}
Now the `Mixer` constructor could look like:
{% highlight java %}
public Mixer(List<Fruit> fruits) {
   //...or Set<Fruit> or MyList<Fruit>
}
{% endhighlight %}
Still the `multibind` is used to define the elements of any of those collections. 
When installing more than one _bridge_ (from array to a collection type) all of them can be used in parallel as well.

Another way to define the elements of a collection is to directly bind the array type of a class:
{% highlight java %}
bind( Fruit[].class ).toElements( Apple.class, Orange.class );
{% endhighlight %}
Also when binding array-types we can inject them as any of the installed collection types. 
By default **none** is installed so that you can explicitly pick what you want - and this is very easily done in on `install` as we have seen above. 


## <a id="inject"></a>Injection
The usual case is to create a single `Injector` for your application that serves as context. Once created from a root `Bundle` a `Injector` is immutable.
{% highlight java %}
Injector injector = Bootstrap.injector( YourRootBundle.class );
{% endhighlight %}
The injection itself is initialized by asking for your root object (usually the _application_):
{% highlight java %}
MyApplication app = injector.resolve( Dependency.dependency( MyApplication.class ) );
{% endhighlight %}
In order to fulfill the dependencies of your root object this indirectly creates your object graph. No further `Dependency`s should be created and/or resolved manually.
If your application has different setups the bootstrapping gets additional arguments that will be shown later when talking about modularity. 

### Make Classes Constructible 
Silk is focused on constructor injection since this keeps your code independent and testable as well as it asserts a final injection state for all injected objects.
The `Constructor` used (if not specified) is automatically picked by a `Inspector` that selects one constructor to use for each `Class`. You can customize it to use your own strategy. 
By default the constructor with no parameters is selected (if available) or the 1st with any parameters (this turned out to be error prone and will be changed!).

There is no build in support for field injection since this is considered harmful as internal state could be changed unforeseeable. 
If fields have a dynamic nature this should be made explicit by using e.g. a `Provider` or any other _indirection_. 
This makes it much more obvious that state is about to change during execution. There are ways to add field injection but I highly recommend to not dig into this.

Anyway if you don't see a way to make a class _constructible_ you always have the option to construct it yourself during the bootstrapping process or use a custom `Supplier` that takes care of the construction. Another option are factory methods described below.

#### Hint Constructor Arguments
Silk has a very powerful way to make it understand what instances should be used as arguments to a constructor. They are passed to the `toConstructor` clause.
The example shows how to pass special `named` instances or a special implementation type as the `asType` as occurring in the constructor signature.

{% highlight java %}
protected void declare() {
	bind( Foo.class ).toConstructor( 
		instance( named( "other", raw( Bar.class ) ), 
		asType(Subtype.class, Supertype.class) );
}
{% endhighlight %}

These kind of hints do not have to be complete nor correctly sorted compared to the constructors parameters. 
If they are unambiguous Silk will understand what should go where. In cases of multiple matching parameters the sequence of hints will be kept.

See also `TestConstructorParameterBinds` for complete examples.

### Use Methods as Instance Factories
Normally this is not needed with Silk to construct the _durable_ instances but some like to also use DI as a more general factory for the _transient_ objects.
You can turn any method into such a factory by letting the `Inspector` pick it up. Therefore a bind with such a inspector is done in a `Module`. All parameters of the methods used will be injected automatically. 

{% highlight java %}
protected void declare() {
	bind( all().methods() ).inModule();
	bind( all().methods() ).in( new MyFactoryImplementation.class );
}
{% endhighlight %}

The methods can also appear `inModule` that declares the `bind`. This makes sense for wiring support instances that arn't part of your application.

The inspection can be fully customized by implementing a _own_ `Inspector`. The build-in one `Inspect` allows to narrow down the selection further more in several ways. To give a few examples:

{% highlight java %}
protected void declare() {
	bind( all().methods().returnTypeAssignableTo( raw( Marker.class ) ) ).in( MyImpl.class);
	bind( all().methods().annotatedWith( Factory.class ).namedBy( NameIs.class ) ).in( MyImpl.class );
}
{% endhighlight %}

The above shows a way that utilizes _your_ annotations to determine what methods to pick and derive the `Name` of the instance(s) produced if it should be a special `Instance`.

You can also hint `Parameters` when using factory methods:

{% highlight java %}
protected void declare() {
	bind( all().methods() ).in( MyImpl.class, instance(named("foo", raw(String.class)) );
}
{% endhighlight %}

See also `TestInspectorBinds` for more examples.


### Inject _dynamic_ into _static_
A _provider_ is an indirect access to an injected instance that is e.g. used to be able to inject a _reference_ to a dynamically changing object into more statically ones that can _fetch_ the current value for each call. 
Silk's `Provider` looks like all the others used in various DI frameworks, except that its access method is named `provide` instead of `get`:
{% highlight java %}
public interface Provider<T> {

	T provide();
}
{% endhighlight %}
The more important difference is, that _providers_ are no core concept in Silk. By default you cannot ask for a `Provider` of the instances you have bound.
This has a very simple reason: Providers usually appear within your application code what would make your code depend on the DI framework what is exactly what Silk aims to avoid. 
So if you really need _providers_ (services are an alternative) you should use your own interface so the dependency goes in the right direction. 
You can easily add it in less than 10 lines of code. Have a look at Silk's own `Provider` implementation as a template. 
Another alternative is to just wrap Silk's `Provider` within our own. Therefore you install it using:
{% highlight java %}
install( BuildinBundle.PROVIDER );
{% endhighlight %}
Now you can ask for any `Provider<T>` for all bound types `T`.
   


## <a id="modularity"></a>Modularity

### Composition
The composition of an application is composed on 2 levels:

* `Bundle`s: The composite. It bundles other `Bundle`s and `Module`s as a unit. They are `install`ed within.
* `Module`s: The leafs. They do the `Bindings` using one or more `bind`-`to` expressions.

So _grouping_ and _binding_ is strictly separated in Silk. 
While _modules_ can be constructed by the user (e.g. `new ...`) the construction of _bundles_ is dedicated to the `Bootstrapper`.
To `install` a `Bundle` it is stated by its `.class`, a `Module` is installed by an instance. 
Often _modules_ are also installed using the `.class` reference what is a convenience way to let the 
bootstrapper also take care of the module construction when there are no arguments to pass to a constructor.

The bootstrapping process starts with a `Bundle` that should be the root of the graph to install.
All _bundles_ and _modules_ reachable (installed) from that root will be contained in that configuration's graph.      

#### Remove what you don't like (from a bundle-tree)
Now we know how to compose larger trees of `Bundle`s and `Module`s. But what if we would like to use an existing bundle that contains something we do not want ?
We just go and uninstall the parts we don't like:
{% highlight java %}
protected void bootstrap() {
	install( WhatWeAlmostWantBundle.class );
	uninstall( PartOfAboveBundleThatWeDoNotWantBundle.class );
} 
{% endhighlight %}
Such `uninstall`s can appear wherever you like. You can bundle them in a separate bundle or like show above directly when installing something.
Literally a bundle could be `uninstall`ed before it is even installed. Remember that the sequence of installations does not matter. 

It might not seam like it but there is a very big difference between _overrides_ and _uninstall_. 
You will notice this when maintaining a larger project. While _overrides_ requires you to know and consider *all* involved parts _uninstall_ just requires to consider the uninstalled bundle itself what is way simpler.  

### Adapt an Application to slightly Different Setups/Configurations
Often the same application is sold to customers in slightly different configurations depending on the customer's needs. 
Sometimes there is a fix set of _editions_ a customer could chose from. Such kind of requirements we can model with ease using `Edition`s.

They allow to decide on a _per class_ base what `Bundle`s and `Module`s to include in an edition. 
The bundles itself do not contain any conditions like `if-else` blocks or such. 
Instead the `Edition` is passed as an argument to the bootstrapping process:
{% highlight java %}
Injector injector = Bootstrap.injector( RootBundle.class, Globals.STANDARD.edition( edition ) );
{% endhighlight %}
A simple edition that _contains_ the core of your application based on the packages could look like this:
{% highlight java %}
Edition core = Globals.edition(Packages.packageAndSubPackagesOf(CoreBundle.class));
Injector injector = Bootstrap.injector( RootBundle.class, core );
{% endhighlight %}
The conditions that control if you want an edition or not are in one place where you bootstrap your `Injector`.
There you have the full control so you can load this from a properties file, a database, command line arguments or such.

#### Pick Single Features
Editions give you a good control but in some business areas it is common so sell individual features to a customer. 
You could model this with editions but they do not really fit the problem. Silk gives you this finer control with `Feature`s.
They utilize a `enum` to model the set of available features with the enum constants. Each constant is a separate feature.
When bootstrapping you decide what features to include:
{% highlight java %}
Edition edition = Globals.edition( MyFeature.BAR, MyFeature.BAZ )
Injector injector = Bootstrap.injector( RootBundle.class, Globals.STANDARD.edition( edition ) );
{% endhighlight %}
The `edition` method creates an `Edition` from the chosen features `BAR` and `BAZ` of your `Feature`-`enum` `MyFeature`.

The test `TestEditionFeatureBinds` shows how to create your own feature annotation so you could annotate `Module`s and `Bundle` with the features they represent. 
But this is just one way. You can implement other strategies to determine the features in a few lines of code. 

### Run different Modes <small>(like PROD or DEV)</small>
Beside the economically driven slices there is a hole bunch of technical configurations. 
While this can be modeled with the above techniques it might not fit well to use them since they are another dimension beside the one of editions or features.
Most likely you want to have a _DEV_ mode for all _editions_ or _features_ and a lot of other switches that can be summed up as a applications configuration.

Silk helps you modeling this using configuration `Options` that are passed to the bootstrapping as well:
{% highlight java %}
Injector injector = Bootstrap.injector( RootBundle.class, Globals.STANDARD.options( options ) );       
{% endhighlight %}
`Options` are _constant_ immutables that cannot be mutated during bootstrapping.
Each option property is again modeled by an `enum` to make sure all possible values can be covered when deciding what should be installed.
{% highlight java %}
Options options = Options.STANDARD.chosen( RunMode.PROD );
// define more...
{% endhighlight %}
First we define the current value `PROD` of the property `RunMode`. 
The `options` are later passed to the `Bootstrap.injector` as seen above as part of the `Globals`.
When working with `Options` we use them together with `ModularBundle`s. 
Those allow to `install` other bundles and modules dependent on constant values. So we define:
{% highlight java %}
private static class RunModeDependentBundle extends ModularBootstrapperBundle<RunMode> {

	@Override
	protected void bootstrap() {
		install( ProductionBundle.class, RunMode.PROD );
		install( DevelopmentBundle.class, RunMode.DEV );
		install( ProductionBundle.class, null );
	}
}
{% endhighlight %}
Now we have build a switch so that `ProductionBundle` is installed when our property `RunMode` has the value `PROD` and another bundle `DevelopmentBundle` when it is `DEV`.
And we have defined that `ProductionBundle` is also used as fall-back when the property has no value, hence it is `null`. Have a look to `TestConstantModularBinds` for another example.

Of cause `ModularBundle`s can also be used without `Constants`. The `BuildinBundle` is an example of this kind of usage. 
There we have different options a user could pick from. We have seen such a installation before when adding `List`s or `Provider`s.


## <a id="scopes"></a>Scopes
One of the main advantages of DI is that instance creation is moved away from processing code. 
Many instances are created and _manged_ by the DI framework. So the lifecycle of objects is one of its tasks. 
A `Scope`s is a description of an object lifecycle and the diversity of instances for each `Resource` in that scope.

While the task of scopes is quite complicated the usage is very simple. 
The instances involved in a `bind` can be scoped by starting with the `per` clause:
{% highlight java %}
per( Scoped.APPLICATION ).bind( Foo.class ).to( ... );
per( Scoped.INJECTION ).bind( Bar.class ).to( ... );
{% endhighlight %}
The 1st line _tells_ Silk to create a _application singleton_ which means there will be just one instance throughout the application (or more precise within one `Injector`) what is also the default behavior if the `per` clause is omitted.

The 2nd line is somewhat the opposite of the 1st. One instance per injection creates a new instance whenever one is injected into another object. So effectively all objects will have their very own `Bar` instance.

### _Session_ Or _Request_ Scopes (and others)
All the `Scope`s shipped with Silk are contained in the `Scoped` utility class and can be used like that. 
You'll not find the very common _REQUEST_ and _SESSION_ scopes since such scopes are directly dependent on the servlet container and fronted framework used.
This is not a problem at all since it is very simple to write such scopes yourself in several lines of code.

Silk is much easier to extend with new scopes than other DI frameworks through clear separation of concerns.
The `Scope` is a singleton that acts as a factory for a `Repository`. This keeps track of the instances and picks the instance to use in a particular injection case.
But it is not burdened with the creation. The `Injectable` provides new instances when needed to fulfill the `Demand`:
{% highlight java %}
public interface Repository {

	<T> T serve( Demand<T> demand, Injectable<T> injectable );
}       
{% endhighlight %}
We had a closer look into this to understand that scopes are very flexible in Silk. 
There are 2 such flexible `Scope`s that are not possible to build in other DI frameworks at all:

* `Scoped.DEPENDENCY_TYPE` : creates one instance for each full generic `Type` of a `Dependency` (the type of the instance to inject into an object). 
	We have seen this in use earlier when talking about _bridges_. 
	Another example is the `Provider`. This scope makes it very easy to build any kind of wrapper or indirection within the resolution of instances.
* `Scoped.TARGET_INSTANCE` : creates one instance for each full generic `Type` of the receiver of a `Dependency`. 
	This allows to easily get the correct `Logger` injected into each class or solve problems like the _robot two legs problem_ where you have quite similar compounds with just a few differences.       

### Achieving Consistency with Snapshot Scopes
Another unique feature of Silk are snapshot scopes. 
They deliver a consistent _view of the world_ kept in a snapshot while the reality changes asynchronous to the _observer_.
This sounds very theoretical so lets have a look to an example. 

Say you have a `Settings` interface and multiple implementations.
One is based on files, another one is static code, a third one backed by a database. You added a `FileScope` and a `DatabaseScope` so that in general your state is updating when changed. 
But the change itself is beyond your control. Still you'd wish you could have a consistent view where you don't have to deal with strange failures caused by asynchronous changes.
In the example the `Settings` could be used in a web application. During a request you read a setting (without knowing or wanting to know where it is from).
It could happen very well that in between 2 reads in the same request this setting changes. Most of the time we get away with that but sometimes this might cause big trouble.
This trouble has an end. You snapshot both of these _asynchronous_ scopes into your _REQUEST_ scope (the one from which you want to have a consistent view) so that within a request you'll find a consistent world that doesn't change while you are computing the response.
{% highlight java %}
	Scope consistentFileScope = Scoped.asSnapshot(MyScopes.FILE, MyScopes.REQUEST); 
{% endhighlight %}
The above creates a scope that _synchonizes_ the _FILE_ scope into the _REQUEST_ scope. Instead of your `MyScopes.FILE` scope you'll just use the snapshot version in the `per(...)` clause.

### Arrange Object Life-cycles Among Each Other
With the power of different life-cycles (scopes) comes the burden of possible misconfiguration when instances with a shorter lifecycle are injected into more durable ones. 
A common mistake when using _google guice_ is such a case where a _session_ or _request_ scoped instance is injected into an _application singleton_. The worst is,
that such a mistake doesn't show up as a problem until the referenced shorter living object actually is expired. In _guice_ this is overcome by using a _provider_ 
so that the actual instance worked with is updating as it changes. But this just helps when the problem has been detected  - and it clutters your code with DI problem solutions. 

Silk will throw a `MoreFrequentExpiryException` in the moment you try to inject a shorter living object into a longer living one and point out that this will not work out later on.
This is achieved by assigning an `Expiry` to each `Scope` during setup. During the injection Silk is aware of the different expires combined so it can encounter problems directly.    


## <a id="services"></a>Services
While _services_ are very common pattern Silk's services are radically different. 
The core idea is that every service function can be described in a uniform way with an interface having a single parameter and return type as generic: `Service<Parameter,Result>`.
Of cause real life services have more than one input value. Silk turns this into an advantage by making it explicit. 
All arguments are passed as a record containing all values. Hence we create one class per service that is this record. 
Thereby each _service_ is unique by just its parameter `Type`. This allows to automatically identify the method that is the implementation body of a service since just one single known service method will have the exact parameter type.

A typical example is a login process. Within the application code the class `LoginParameters` is created:
{% highlight java %}
class LoginParameters {
  final String name;
  final String password;
  // ... constructor
}
{% endhighlight %}
There is a `UserServices` implementation that is responsible for various user related services:
{% highlight java %}
class UserServices {
  
  public Success login(LoginParameters params) { 
  	// ...whatever to do
  }
  // more service methods
}
{% endhighlight %}
Now we tell Silk that when looking for service methods it will find some in `UserServices` by creating a `ServiceModule`
{% highlight java %}
private static class MyServicesModule extends ServiceModule {

	@Override
	protected void declare() {
		bindServiceMethodsIn( UserServices.class );
		// ... others
	}
}
{% endhighlight %}
Which methods are `ServiceMethod`s (Silk's internal abstraction) is customizable through the `ServiceStrategy`.

When using the login the `UserServices` are not exposed completely. Instead the `Service` abstraction is used for the one function needed here. 
For example the UI login form has to delegate the request to the service. Therefore we inject the service.
{% highlight java %}
class LoginForm {

	LoginForm(Service<LoginParameters,Success> login) {
		// here we have the login without creating any dependency to the implementor whatsoever!
	}
}
{% endhighlight %}
Note that the `Service` interface mentioned here is not defined by Silk (this would make your code depend on it). 
It is an application interface that gets connected to Silk's low level representation `ServiceMethod` with a adapter bind. 
The test `TestServiceBinds` shows how to do it for an interface just like in this example.

### Say Goodbye to Dependency Cycles  

Effectively all dependencies point to the chosen service interface and the record classes that act as parameter container. 
Thereby services can be wired in any network without causing any dependency cycles whatsoever. 
There are no proxies, bytecode enhancements or other hard magic! 
Still there can be usage cycles between services and/or service providers like the `UserServices`.
It also makes the service methods more readable, more stable to change (just the record class is changing) and also allows to use the _parameters_ class as UI-bean.

### Easy Long Term Maintenance
When using service methods all dependencies can be injected into the service method directly. 
In case the `login` methods makes use of a `Session` object to track the login state and some access to check the password this dependencies can be inject too:
{% highlight java %}
  public Success login(LoginParameters params, Session session, 
  	Service<PasswordValidParameters,Boolean> passwordChecker) { 
  	// ...use session and checker to login 
  }
{% endhighlight %}
Thereby the sequence of parameters or the method name doesn't play any role. They can be renamed or refactored without breaking anything.
Through this all dependencies become directly visible and coupling is reduced. 
The `UserServices` itself don't need to have any field for dependencies. All comes with the service method.
That makes it much simpler to determine all the code that has to be removed when a system function (service method) is no longer needed.

### Say Goodbye to Scope Issues
When dependencies are injected directly in the service method the problems arising from different `Scope`s just disappear since 
all dependency instances are just used for the single invocation. Another invocation might use other instances because of scopes
but the implementation doesn't have to care at all about that. It becomes transparent.

The overhead caused through the additional injections is automatically kept minimal. 
Silk preresolves all dependencies that are stable (application singletons) so their injection comes at no additional costs for resolving them for each invocation.
Also dynamic dependencies are partially resolved to the `Injectron` to use. This is actually faster than using `Provider`s that would be necessary without service methods.
Last but not least the costs of the service interface and `ServiceMethod` interface invocation are monomorphic so they might even be inlined by JVMs what makes them very fast.

### Adding Cross-Functional Behavior
The uniformity of service methods also allows to add behaviors to all of them that usually done with AOP. 
For example logging can be added before and/or after all invocation of `ServiceMethod`s. 

The test `TestServiceInvocationBinds` shows a complete example.  


## <a id="data"></a>Data Types
Silk is a data driven framework. All data is modeled as immutable value objects. 

### Represent Any Type Uniform
The most important one is `Type` that is a generic form of `Class`. It is used to construct all type descriptions. Here some examples:
{% highlight java %}
Type.raw(Integer.class); // corresponds to Class<Integer> 
Type.raw( Number.class ).asLowerBound(); // corresponds to Class<? extends Number> 
Type.raw( List.class ).parametized( Number.class ); // corresponds to Class<List<Number>>
Type.raw( List.class ).parametized( Number.class ).parametizedAsLowerBounds() // Class<List<? extends Number>>
{% endhighlight %}
Due to limitations in java generics the `Type` instance itself will not reflect the type parameterization so it becomes _visible_ for the compiler. 
But the `Typecast` utility allows to create full reflected types for several common parametized types like `List`, `Set`, `Collection`, `Factory`, `Injectron`, `Provider`:  
{% highlight java %}
Type<List> raw = Type.raw( List.class ).parametized( Number.class );
Type<? extends List<Number>> parametized = Typecast.listTypeOf( Number.class );
{% endhighlight %}
Thereby the nessessary cast is kept in one place. This type-system workaround can be adapted to also have the same convenience for application specific generic types.

### Describe Instances
Dependency injection is instance based in Silk. Hence multiple instances of the same `Type` (full generic) are distinguished by their `Name` as well.
The `Instance` value object models this combination of `Type` and `Name`. When binding the _name_ can be omitted but internally this uses the `Name.DEFAULT` for those binds.


## <a id="customise"></a>Customise the Binding Process
Out of the box Silk uses simple robust strategies to draw the connection between bindings and the application classes.

### Customise Object Creation
A `Inspector` is used to decide which `Constructor` is used to create instances of a type and what `Method` implements a particular `Factory` method.
Espeially when changing to Silk from e.g. an annotation based DI framework it could be useful to use a custom `Inspector`. 
All that needs to be done is to create a own implementation or use the existing util class `Inspect` and pass such a instance to the bootstrapping process:
{% highlight java %}
Inspector inspector = Inspect.all().constructors().annotatedWith( Inject.class );
Injector injector = Bootstrap.injector( RootBundle.class, inspector, Globals.STANDARD );
{% endhighlight %}

### Customise Service Method Selection
`ServiceMethod`s are chosen by a special `Inspector` as well that is given by `ServiceModule.SERVICE_INSPECTOR`.
This default bind can be replaced in any `ServiceModule` using the `bindServiceInspectorTo` method:
The below example shows how to utilise annoations to do the job:
{% highlight java %}
class ServiceInspectorModule extends ServiceModule {

	@Override
	protected void declare() {
		bindServiceInspectorTo( Inspect.all().methods().annotatedWith( MyServiceAnnotation.class ) );
	}

}
{% endhighlight %}

#### Note
While the above examples show how to bring back annotations I highly recommend to just see this as a way to switch the framework an change smothly to an annotation free implementation.

<a class='next' href="binds.html"><span class="icon-chevron-right"></span>Bindings</a>
