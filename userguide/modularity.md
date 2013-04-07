---
layout : userguide
title : Modularity
---
# Modularisation

## Bundles
A set of `Bindings` is described by a tree structure composed of `Bundle`s as nodes and `Module`s as leafs. 
The role of a `Bundle` is to describe a subset of child bundles and modules in its only method called `bootstrap`. 
When `install`-ing a `Bundle` is always referred statically by its `class` instance. 
This comes as no surprise when knowing that the bootstrapping is a _static_ process that just happens once.

Inheritance should just be used to provide helper methods.
As soon as the `bootstrap` method is implemented to `install` or `uninstall` bundles or modules no further 
inheritance should occur. For such purposes always use composition. Hence split into 2 separate bundles and
install them both.

The below example show a typical case where the `BootstrapperBundle` class is utilized to install a child bundle:
{% highlight java %}
protected void bootstrap() {
	install(ChildBundle.class);
}
{% endhighlight %}

### The Root Bundle
All `Bundle`-trees need a single root node that is the entrance point for the `Bootstrapper` when visiting 
the tree structure. 

### <a id="uninstall"></a> Uninstalling Bundles and Modules
Overrides are hard to understand and maintain. Therefore there are no overrides at all. 
You cannot override bindings, bundles or modules because this soon or later ends up in a mess.

If dividing into bundles or modules that you can compose more flexible doesn't feel appropriate or isn't possible since it is not part of your source you can `uninstall` `Bundle`s or `Module`s.
Once a bundle or module has been uninstalled it cannot be reinstalled again. Every uninstalled component will definitely not be bootstrapped including all children and grant-children of a uninstalled bundle.
But it is very well possible that another bundle installs one of the children or grant-children. Of cause that child will be bootstrapped since there is a path in the tree of installed bundles that reaches it.  

To summarise it: Each `Bundle` has one of the below three states during the bootstrapping process:

* _installed_ (could be uninstalled later on)
* _uninstalled_ (cannot be installed any further)
* _not installed_ (but could be installed later on)

### Cross-Dependent or Cyclic Bundles
Bundles can be seen as static descriptions. Therefore the `Bootstrapper` can assume that each call
of a `Bundle`s `bootstrap(Bootstrapper)` method will produce the same result (calls on the `Bootstrapper`). 
Thereby it is not necessary to redo any `install` call for the same bundle. The effect is already covered after doing it once. 

**The static nature of bundles allows them to have any graph of references between each other.**

Bundles can (e.g.):

* be cross dependent on each other (A installs B, B installs A)
* have cycles (A installs B, B installs C, C installs A)
* ...whatever you can think of...

For a user of Silk this means: You don't have to care at all about the structure (graph) of bundles. There will be no problem with it!

## Modular Bundles
A `ModularBundle` is a bundle where each child `Bundle` is associated with a specific configuration value.
This values are `enum` constants. Within the modular bundle they are passed as argument to the `install` call (here `Machine`) to express their association:
{% highlight java %}
class MachineBundle extends ModularBootstrapperBundle<Machine> {
	protected void bootstrap() {
		install( GenericMachineBundle.class, null );
		install( LocalhostBundle.class, Machine.LOCALHOST );
		install( Worker1Bundle.class, Machine.WORKER_1 );
	}
}
{% endhighlight %}
Which of the associated `Bundle`s actually will be installed depends on the choice that can be made
in different ways (described below). With modular bundles structure is separated from the decision 
which subtree should be expanded in a specific configuration. 
The use of an `enum` and the possibility to specify a _default_ for the case no such option has been chosen grants high stability. 

From within a usual `Bundle` there are 2 ways to use a `ModularBundle` that are described below. 

### Conditional Installation with Options
The `ModulearBundle` is installed by referring to its `enum` `.class` instance.
{% highlight java %}
protected void bootstrap() {
	install( MachineBundle.class, Machine.class );
}
{% endhighlight %}
Now all `chosen` modular bundles will be installed. In this case the `Options` for the type 
`Marchine` is used as a single choice. In the `Globals` setup before the bootstrapping one machine 
will be specified (or not = `null`) to control the actually installed `Bundle`s specified above.
{% highlight java %}
Options options = Options.STANDARD.chosen( Machine.LOCALHOST );
Injector injector = Bootstrap.injector( RootBundle.class, Globals.STANDARD.options( options ) );
{% endhighlight %}
For this example both `install` calls will another value than the `chosen` one will be ignored and
not installed. 

The test `TestModularBinds` shows a complete example of this form of usage. 

### Hard-coded Selective Installation
One or more children of a `ModulearBundle` are installed by referring to their specific `enum` constants.
{% highlight java %}
protected void bootstrap() {
	install( MachineBundle.class, Machine.LOCALHOST );
}
{% endhighlight %}
This can also be used with multiple constants:
{% highlight java %}
protected void bootstrap() {
	install( BuildinBundle.LIST, BuildinBundle.COLLECTION );  
}
{% endhighlight %}
This will (independently from the `Options`) install just the chosen parts of the modular bundle. 
The `BuildinBundle` used in this example is Silk's _repository_ for general functionality that the 
user can pick from like shown above. This shows that `ModularBundle`s can also be used to decouple
a subtree of the composition from the need of knowing the exact `Bundle` class that should be installed. 
Those can be hidden behind a _concept_ represented by the `enum` constant.   

The test `TestEditionFeatureBinds` shows the use of modular bundles for install bundles _feature dependent_.

## Modules
`Module`s are the nodes of the composition tree. Their role it to describe the actual <a href="binds.html">bindings</a>.
On a low level that mean `add`ing 4-tuples to to the `Bindings` passed as argument. 

{% highlight java %}
interface Module {
	void declare( Bindings bindings, Inspector inspector );
}
{% endhighlight %}
The `Inspector` passed is used the determine what `Constructor` or factory `Method` is picked up for a specific class.

But usually the `Binder` fluent interface is used that takes care of the details. Therefore almost all modules
extend the `BinderModule` that extends the `Binder` API while it is also a `Module`.

{% highlight java %}
class MyModule extends BinderModule {

	@Override
	protected void declare() {
		//bind(...
	}
}
{% endhighlight %}
The parameters `Bindings` and `Inspector` are than _wrapped_ by the fluent interface used. 

### Modules as Bundles
Often you'll see that modules are used as if they are `Bundle`s (e.g. they are _installed_ or used as _root bundle_ in tests).
This is because `BinderModule` also implements the `Bundle` interface. So almost all modules are 
also bundles that just install themselves. But this does not fit for all modules.

Conceptually modules are used as instances (`new MyModule()` **can** be done by the user) while bundles 
are always referred by their `.class`. But when used as a bundle the instance creation will be done 
by the bootstrapper.

#### Monomodal Module Classes
When modules installed by the user code a module will also just be installed once when it effectively 
cannot have different initial behavior for 2 instances. This is called a _monomodal_ class. 
E.g. a class without instance fields is always _monomodal_.

### Customizing the Fluent Interface
If the `Binder` API should be used but the module has constructor arguments and therefore cannot
be a bundle as well the `InitializedBinder` can be extent quite easy. On the other hand the `Binder` 
API is totally on top of the low level `Module` functionality what allows to add other ways or other
fluent interfaces without the need to change Silk itself. This is up to the user. 
  
It is also possible to directly extend the existing `Binder` API with a own base module extending 
the `BinderModule` or the `InitializedBinder`. 
This allows to add bind-methods in a way so that those become available as if they always have 
been part of the API. This should be preferred over static helper methods.
To also extend the parts of the binder API that come after the first call (most often `bind`)
covariant return type overrides can be used as show below for the `per` method.

{% highlight java %}
class MyBaseModule extends BinderModule {

	@Override
	public MyScopedBinder per( Scope scope ) {
		return new MyScopedBinder(/*...*/);
	}
}
class MyScopedBinder extends ScopedBinder { }
{% endhighlight %}


### Preset Modules

<a class='next' href="config.html"><span class="icon-chevron-right"></span>Configuration</a>
