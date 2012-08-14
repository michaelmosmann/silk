---
layout : default
title : Bindings
---
# Bindings

## Concept
In Silk bindings are just a convenient way to create `Suppliable`s (we don't need to know that those are now). 
Think of them as a util. All different kinds of bindings described below are themselves just a 
convenient way to produce calls to the `Bindings#add`-method that we see below:

{% highlight java %}
public interface Bindings {

	<T> void add( Resource<T> resource, Supplier<? extends T> supplier, Scope scope, Source source );
}
{% endhighlight %}

So a `Binding` is noting more than a 4-tuple consisting of

- `Resource` : Describes when (for which `Dependency`) to use the `Supplier`
- `Supplier` : Describes how to supply (resolve/create) an instance
- `Scope` : Describes how created instances are managed (how often new instances are created and how many exist beside each other)
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
	1. `Packages` are more precise (smaller set)
	2. `Instance` (receiver) is more precise (has again `Type` and `Name`)

Following this rules there can be just **one** binding that is the one chosen because bindings have to be unambiguous. 
This means: 

**There cannot be 2 bindings with the same precision matching the same case!**

There can be as many quite similar bindings as long as non of them describe exactly the same `Resource`,
that is the same `Instance` within the same `Target` (see below how targeting makes binds more specific). 
If Silk encounters 2 or more bindings having the exact same `Resource` this will raise an exception 
during the bootstrapping process.

## Simple Bindings
The most common and simple form of binding describes pairs of what implementation should be used for a particular interface. Here is an example:

{% highlight java %}
protected void declare() {
	bind( Interface.class ).to ( Implementation.class );
}
{% endhighlight %}

This tells Silk to use `Implementation` whenever a dependency of type `Interface` is encountered. 
In case we don't have such an abstraction we could also advice to directly construct our `Implementation`.

{% highlight java %}
protected void declare() {
	construct( Implementation.class );
}
{% endhighlight %}

What is just another shortcut for this declaration:

{% highlight java %}
protected void declare() {
	bind( Implementation.class ).toConstructor();
}
{% endhighlight %}

In both cases we don't (need to) provide any additional information how Silk should construct the instance.
The bootstrapper will ask the `InjectionStrategy` to pick the `Constructor` to use for our `Implementation`.
All dependencies needed to instanciate it are resolved in the same way.

In some cases we want a special instance injected into the constructor selected. Therefore we can add additional 
_hints_ so Silk knows what to do. Generally we have 2 options to achieve this:

- Use targeting (see end of page)
- Add `Parameter`s to the `toConstructor` call

Lets have a look at the latter solution. Assuming our `Implementation` needs a instance of `Foo` as constructor
argument and we want to get a special named `Foo` for it we can describe this like that:

{% highlight java %}
protected void declare() {
	Instance<Foo> foo = instance( named( "specialOne" ), raw( Foo.class ) );
	bind( Implementation.class ).toConstructor( foo );
}
{% endhighlight %}

We refer to this special `Foo` instance using a `Instance` descriptor that we pass as argument to `toConstructor` call.
Now Silk knows what should be resolved here. To make this run some module has to bind `Foo` so it becomes constructable as well (e.g. by using `construct( foo )`). 

**Note that the `Parameter`s (like an `Instance`) do not describe the signature of the constructor!**

The are used to help out when needed. All dependencies that are resolved correctly without such a _hint_ don't have to be added.
Therefore sequence also don't play a role. It just becomes important in case a `Parameter` given matches more than one constructor dependency.
Than the first parameter is used for the first matching, the second for the second and so on.

See also `TestConstructorParameterBinds` and `TestDependencyParameterBinds` for more detailed examples.

## <a id="array"></a>Array-Bindings
There is a build in support in Silk that when binding something to a class `X` the 1-dimensional array type `X[]` is implicitly defined as well.
The array contains all known `X` instances. There can be more than one because for type `X` `multibind`s (see below) have been used 
or it's those are instances with different precision so they normally apply to different injections.

So as soon as we do a usual bind like this: 
{% highlight java %}
protected void declare() {
	bind( Integer.class ).to(42);
}
{% endhighlight %}
We can inject `Integer` as well as `Integer[]`. With just this above definition alone that array would be equal to `new Integer[]{42}`.
This behaviour works on the raw-type. Currently there is no support for something similar with generic types. 
But it is possible to bind directly to a generic array type and define all the members. Of cause this can also be used to explicitly
define what elements should be contained in e.g. `Integer[]`
{% highlight java %}
protected void declare() {
	bind( Integer[].class ).toElements( 4, 2 );
}
{% endhighlight %}
When this binding can be used (matches) to inject a dependency of type `Integer[]` just the defined 
elements `4` and `2` will be contained independent of any of the binds done for `Integer`. Together 
with targeting (see below) this can be used to _replace_ the meaning of `Integer[]` just for special
situations. 

Together with _bridge_-`Supplier`s (see `BuildinBundle`) the build in array-support can be used to also easily get `List`s,
`Set`s or your custom collection type injected containing all the elements from the binds on the 
element type or a special bind for the array type. Note that even though you receive the collection
as `List` (or something else) the binds are defined like above binding to the array type. This keeps
them independently from the different forms of collection asked for by different receivers. We don't
need to explicitly define a bind to `List<Integer>` (even though this would work as well what can be 
used to replace a general behaviour in some cases).

## <a id="multi"></a>Multi-Bindings
A `multibind` is used to create collections of instances that should be injected together. All of them are bound to the same super-type.

When using a `multibind` we explicitly want multiple instances to coexist for the same resource because the resource models a collection of something.
Notice that you cannot combine this with any other kind of `bind` because there we want all resources to be unambiguous.

Let's assume we have a service that depends on some settings that can emerge from different sources. Each represented by a instance of type `Settings`. One is reading the from a `File` and the other one receives them remotely.  
{% highlight java %}
protected void declare() {
	multibind(Settings.class).to(FileSettings.class);
	multibind(Settings.class).to(RemoteSettings.class);
}
{% endhighlight %}

The consumer of this `Settings` can asked for them by simply asking for the array type `Settings[]`. This is a build in functionality that you get out of the box. For all types you declared any bind (also normal ones) you can ask for the array type as well. 
{% highlight java %}
class SettingDependentService {
  SettingDependentService(Settings[] settings) { /*...*/  }
}
{% endhighlight %}

Often the usage of `List` is preferred. I don't see a real advantage using them here but in case we want them anyway we once `install` a `Bundle` that makes `List`s available in general. Now we can use `List<X>` as a replacement for `X[]` everywhere. 
{% highlight java %}
protected void bootstrap() {
	install( BuildinBundle.LIST );
}
{% endhighlight %}
Like `List`s here we could also add `Set<X>`s as equivalent to `X[]`. This kind of _bridges_ from an array-type to any kind of collection can be added easily to Silk with a _one-liner_ by extending the `ArrayBridgeSupplier`.  


## <a id="star"></a>Star-Bindings
Like `autobind` a `starbind` is just a convenient method on top of the `bind` API.
It models a _wildcard_-binding and is nothing more than the below (from `Binder`):

{% highlight java %}
public <T> TypedBinder<T> starbind( Class<T> type ) {
	return bind( Instance.anyOf( Type.raw( type ) ) );
}
{% endhighlight %}

Wildcard bindings are used when a bind should explicitly match any dependency that
has the raw `type` given (also any `Name` is matched).

This is used e.g. to _forward_ all dependencies on `Provider` to the `Supplier`
that builds the _bridge_ to the type provided by it. The declaration in the `ProviderBridgeModule` looks like this: 

{% highlight java %}
protected void declare() {
	per( DEPENDENCY_TYPE ).starbind( Provider.class ).to( SuppliedBy.PROVIDER_BRIDGE );
}
{% endhighlight %}

Even though the `starbind` doesn't provide that much functionality it is a slightly
different intention to use one in comparison to a `bind`. Beside the `Provider` 
all other kinds of _bridges_ like `List`s and `Set`s, `ServiceMethod`s or your custom service
make use of one `starbind` declaration.


## <a id="auto"></a>Auto-Bindings
A `autobind` is a convenient helper method on top of normal `bind`s. 
It is nothing else than binding the same class (from `to`-clause) to all its 
super-types and -interfaces (that would be the `bind`-clause).

Think of a design where one class (instance of that) serves as implementation 
for a hole suite of interfaces. A general setting service could look like this: 
{% highlight java %}
class SettingService 
	implements UserSettings, AdminSettings, EmailSettings { /* ... */ } 
{% endhighlight %}
Now a single `autobind` is done:
{% highlight java %}
protected void declare() {
	autobind( SettingService.class ).toConstructor();
}
{% endhighlight %}
What is the same as doing:
{% highlight java %}
protected void declare() {
	bind( SettingService.class ).toConstructor();
	bind( UserSettings.class).to( SettingService.class );
	bind( AdminSettings.class).to( SettingService.class );
	bind( EmailSettings.class).to( SettingService.class );
}
{% endhighlight %}
In some cases this could become quite messy. Therefore the `autobind` is 
provided even though it might be useful quite seldom. 

Right now there is no support for super-types with generics like for `Integer`
the `Compareable<Integer>` will not be bound automatically. It is planed to
implement this at some point.


## <a id="targeting"></a>Targeting Bindings
All of the above forms of bindings can be made more specific by describing the `Target` it should be used for.
This narrows the cases in which it matches a dependency but also makes it more suitable. 

The `Target` describes the `Instance` in which something should be injected as well as the `Packages` in which the binding is valid. 
Both techniques can be used to make a binding very precise and therefore more significant in all cases where it matches.

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
the most specific in that respective case will be used. This are different ones in different cases.

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
