---
layout : userguide
title : Introduction
---
# Introduction

The introduction will give you a quick overview about the concepts of the Silk framework. 
Each concept is further described in detail on a separate page. 
Those can be read as a tour starting with <a href="binds.html">bindings</a> and finishing with Silk's <a href="data.html">data types</a>.
All pages of the tour are linked to the next tour page at the bottom of the page. You can also use the above menu bar _User Guide_ to navigate through the tour. 
I recommend to read about the details in the sequence given an skip the pages explaining concepts that are familiar for you. 

This introduction will explain the general features and concepts. The details and more advanced features will be described later on.
There are a lot of noteworthy characteristics - like Silks general immutability - that will not be mentioned here since this introduction 
tries to give you a good overview in the usage of Silk. With a deeper understanding you'll get into the implications of all it's characteristics bit by bit.    

## Bindings
To _bind_ means to associate a type (usually an interface) with a _strategy_  to create an instance that can be used when a dependency of the bound type is encountered. 
These _bindings_ are defined using a fluent interface, the `Binder`. A basic binding form of such a binding has a `bind` and a `to` clause like this:
{% highlight java %}
	bind( Some.class ).to(...);
{% endhighlight %}
For both, the `bind` and the `to` clause different arguments can be used to vary meaning and behaviour.   
All bindings are declared separated from your application code in so called `Module` classes (see modularity below).
There will be no `@annotations` within your code to guide the dependency injection! This is a key concept in Silk that makes it very different from other frameworks.

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

#### Dynamically supplied instances
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
class MyListSupplier
		extends ArrayBridgeSupplier<MyList<?>> {
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
By default **none** is installed so that you can explicitly pick what you want - and this is very easy to do. 

## Scopes

## Modularity

## Services

## Data Types

<a class='next' href="binds.html">Start the tour with Bindings...</a>