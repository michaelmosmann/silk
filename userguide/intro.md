---
layout : userguide
title : Introduction
---
# Introduction

The introduction will give you a overview about the concepts of the Silk framework deep enough to use well. 
Each concept is further described in detail on a separate page.
Those can be read as a tour starting with <a href="binds.html">bindings</a> and finishing with Silk's <a href="data.html">data types</a>.
All pages of the tour are linked to the next tour page at the bottom of the page. You can also use the above menu bar _User Guide_ to navigate through the tour. 
I recommend to read about the details in the sequence given an skip the pages explaining concepts that are familiar for you. 

This introduction will explain the general features and concepts. The details and more advanced features will be described later on.
There are a lot of noteworthy characteristics - like Silks general immutability - that will not be mentioned here since this introduction 
tries to give you a good overview in the usage of Silk. With a deeper understanding you'll get into the implications of all it's characteristics bit by bit.    

**Note!**
The examples given will make use of the `BinderModule` and `BootstrapperBundle` base classes for `Module`s and `Bundle`s. 
Implementing the interfaces directly would result in code that looks a little different, less fluent.

## Bindings
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
bind( Interger.class ).to( 42 ); // a constant
bind( Number.class ).to( Float.class ); // as a link to a sub-type that is bound itself to something
Constructor<MyClass> constructor = //... from whatever
bind( MyClass.class).to( constructor );
bind( MyOtherClass.class).toConstructor(); // that is chosen by Silk
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


## Injection
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
The `Constructor` used (if not specified) is automatically picked by a `ConstructionStrategy` that selects one constructor to use for each `Class`. You can customize this to use your own strategy. 
By default the constructor with no parameters is selected (if available) or the 1st (in sequence of definition within the class) with any parameters.

There is no build in support for field injection since this is considered harmful as internal state could be changed unforeseeable. 
If fields have a dynamic nature this should be made explicit by using e.g. a `Provider` or any other _indirection_. 
This makes it much more obvious that state is about to change during execution. There are ways to add field injection but I highly recommend to not dig into this.

Anyway if you don't see a way to make a class _constructible_ you always have the option to construct it yourself during the bootstrapping process or use a custom `Supplier` that takes care of the construction. 

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
   

## Scopes
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
You'll not find the very common _REQUEST_ and _SESSION_ scopes since such scopes are directly dependent on the servlet container and frontend framework used.
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

### Arrange Object Lifecycles Among Each Other
With the power of different lifecycles (scopes) comes the burden of possible misconfiguration when instances with a shorter lifecycle are injected into more durable ones. 
A common mistake when using _google guice_ is such a case where a _session_ or _request_ scoped instance is injected into an _application singleton_. The worst is,
that such a mistake doesn't show up as a problem until the referenced shorter living object actually is expired. In _guice_ this is overcome by using a _provider_ 
so that the actual instance worked with is updating as it changes. But this just helps when the problem has been detected  - and it clutters your code with DI problem solutions. 

Silk will throw a `MoreFrequentExpiryException` in the moment you try to inject a shorter living object into a longer living one and point out that this will not work out later on.
This is achieved by assigning an `Expiry` to each `Scope` during setup. During the injection Silk is aware of the different expires combined so it can encounter problems directly.    


## Modularity

### Composition
The composition of an application is composed on 2 levels:

* `Bundle`s: The composite. It bundles other `Bundle`s and `Module`s as a unit. They are `install`ed within.
* `Module`s: The leafs. They do the `Bindings` using one or more `bind`-`to` expressions.

So _grouping_ and _binding_ is strictly separated in Silk. 
While _modules_ can be constructed by the user (e.g. `new ...`) the construction of _bundles_ is dedicated to the `Bootstrapper`.
To `install` a `Bundle` it is stated by its `.class`, a `Module` is installed by an instance. Often _modules_ are also installed using the `.class` reference what is a convenience way to let the bootstrapper also take care of the module construction when there are no arguments to pass to a constructor.

The bootstrapping process starts with a `Bundle` that should be the root of the graph to install. All _bundles_ and _modules_ reachable (installed) from that root will be contained in that configuration's graph.      



## Services

## Data Types

<a class='next' href="binds.html">Start the tour with Bindings...</a>