---
layout : default
title : Bindings
---
# Bindings

## Understand Bindings 
In Silk bindings are just a convenient way to create `Suppliable`s (we don't need to know that those are now). 
Think of them as a util. All different kinds of bindings described below are themselves just a 
convenient way to produce calls to the `Bindings#add`-method that we see below:

{% highlight java %}
public interface Bindings {

	<T> void add( Resource<T> resource, Supplier<? extends T> supplier, Scope scope, Source source );
}
{% endhighlight %}

A `Binding` is a 4-tuple consisting of

- `Resource` : Describes when (for which `Dependency`) to use the `Supplier`
- `Supplier` : Describes how to supply (resolve/create) an instance
- `Scope` : Describes how often a new instance is created and how many exist beside each other
- `Source` : Describes the origin of the binding (the `Module` that defined it) and the binding-type (_multi_, _auto_, ...)

Again: all the different bindings described below just result in such a 4-tuple. Some will create 
more than one but in a simplified inspection it is correct to expect a 1:1 correlation.

### Precision of Bindings
When the `Injector` has to decide which binding should be taken there might be several known that fit the case.
If so the binding that is most specific will be used preferential to satisfy a `Dependency`. 
But what does it mean to be more or most specific or precise ? Generally speaking it could be said, 
that the binding is more narrow, hence applies to less cases, is less general then others. 
Such a bind is therefore more specific or precise. The following order is chosen to satisfy a _naturl_ expectation when using it: 

When matching a `Resource` to a `Dependency` this is the sequence of importance (strongest to weakest): 

1. `Instance` (injected) is more precise
	1. `Type` is more precise
	2. `Name` is more precise
3. `Target` is more precise
	1. `Packages` are more precise
	2. `Instance` (receiver) is more precise (has again `Type` and `Name`)

Following this rules there can be just **one** binding that is the one chosen because bindings have to be unambiguous. 
This means: 

**There cannot be 2 bindings with the same precision matching the same case!**

There can be as many quite similar bindings as long as non of them describe exactly the same `Resource`,
that is the same `Instance` within the same `Target` (see below how targeting makes binds more specific). 
If Silk encounters 2 or more bindings having the exact same `Resource` this will raise an exception 
during the bootstrapping process.

## Simple Bindings


## Array-Bindings
 

## Multi-Bindings
A `multibind` is used to create collections of instances that should be injected together. All of them are bound to the same super-type.

When using a `multibind` we explicitly want multiple instances to coexist for the same resource because the resource models a collection of something.
Notice that you cannot combine this with any other kind of `bind` because there we want all resources to be unambiguous.

Let's assume we have a service that depends on some settings. Those can emerge from different sources. Each represented by a instance of type `Settings`. One is reading the from a `File` and the other one receives them remotely.  
{% highlight java %}
protected void declare() {
	multibind(Settings.class).to(FileSettings.class);
	multibind(Settings.class).to(RemoteSettings.class);
}
{% endhighlight %}

The consumer of this `Settings` can asked for them by simply asking for the array type `Settings[]`. (This is a build in functionality that you get out of the box. For all types you declared any bind (also normal ones) you can ask for the array type as well. 
{% highlight java %}
class SettingDependentService {
  SettingDependentService(Settings[] settings) { /*...*/  }
}
{% endhighlight %}

Often the usage of `List`s is preferred. I don't see a real advantage using them here but in case we want them anyway we once `install` the a `Bundle` that makes `List`s available in general. Now we can use `List<X>` as a replacement for `X[]` everywhere. 
{% highlight java %}
protected void bootstrap() {
	install( BuildinBundle.LIST );
}
{% endhighlight %}
 

## Star-Bindings

## Auto-Bindings


## Targeting Bindings
All of the above forms of bindings can be made more specific by describing the `Target` it should be used for.
This narrows the cases in which it matches a dependency but also makes it more suitable. 

### Localised Bindings
Bindings can be made more specific by narrowing down the packages where the binding applies. The less 
packages are member of a `Packages` set the more precise it becomes. The most specific `Packages` set
therefore contains just a single specific `Package`.

The below bindings taken from `TestPackageLocalisedBinds` show the different possibilities:
{% highlight java linenos %}
protected void declare() {
	bind( String.class ).to( "default" );
	inPackageOf( TestPackageLocalisedBinds.class ).bind( String.class ).to( "test" );
	inSubPackagesOf( Object.class ).bind( String.class ).to( "java-lang.*" );
	inPackageAndSubPackagesOf( List.class ).bind( String.class ).to( "java-util.*" );
}
{% endhighlight %}

Description from the most to the least specific binding given above:

- Line `3` is the most local bind that is just valid in the package of the test class
- Line `4` matches in all sub-packages of `java.lang` 
- Line `5` matches in `java.util` and all its sub-packages
- Line `2` has no localisation

All bindings above can coexist because of their different precision. When supplying a `Dependency` 
the most specific in that respective case will be used.

### Parent Dependent Bindings
Beside the `Packages` where a binding can be used it is also possible to target a specific `Instance`. 
The _Robot-Legs-Problem_ (shown in `TestRobotLegsProblemBinds`) is a good example of where such instance specific bindings can be useful.
A robot has 2 legs, both quite similar but the left `Leg` should get the left `Foot` while the right `Leg` gets another instance - the right `Foot`.
In Silk this could be described like so:
{% highlight java %}
protected void declare() {
	bind( left, Leg.class ).toConstructor();
	bind( right, Leg.class ).toConstructor();
	injectingInto( left, Leg.class ).bind( Foot.class ).to( left, Foot.class );
	injectingInto( right, Leg.class ).bind( Foot.class ).to( right, Foot.class );
}
{% endhighlight %}		
Here `left` and `right` are instances of `Name`s that are used to point out what `Instance` is meant. 
We use the same names for the `Leg`s and `Feet`s but could also have chosen different ones like `leftFoot`, `leftLeg` and so forth.

The `injectingInto`-method describes what parent we are targeting with the `bind` that is called subsequently.  
