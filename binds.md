---
layout : default
title : Bindings
---
# Bindings

## Understand Bindings 
In Silk bindings are just a convenient way to create `Suppliable`s. Think of them as a util. 
All different kinds of bindings described below are themselves just a convenient way to produce calls
to the `Bindings#add`-method that we see below:

{% highlight java %}
public interface Bindings {

	<T> void add( Resource<T> resource, Supplier<? extends T> supplier, Scope scope, Source source );
}
{% endhighlight %}

A `Binding` is a 4-tuple consisting of

- `Resource` : Describes when (for which `Dependency`) to use the `Supplier`
- `Supplier` : Describes how to supply (resolve/create) an instance
- `Scope` : Describes how often a new instance is created and how many exist beside each other
- `Source` : Describes the origin of the binding (the `Module` that defined it) and its type

Again: all the different bindings described below just result in such a 4-tuple. Some will create 
more than one but in a simplified inspection it is correct to expect a 1:1 correlation.

 

## Basic Bindings


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


## More Specific Bindings
All of the above forms of bindings can be made more specific. This narrows the cases in which it 
matches a dependency but also makes it more suitable. A binding that is more specific will used 
preferential to satisfy a dependency. 

When matching a `Resource` to a `Dependency` this is the sequence of importance (strongest to weakest): 

1. `Instance` is more precise
	1. `Type` is more precise
	2. `Name` is more precise
3. `Target` is more precise
	1. `Packages` are more precise
	2. `Instance` (receiver) is more precise

### Localise Bindings
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