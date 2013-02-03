---
layout : userguide
title : Scopes
---
# Scopes

## Concept 
Each binding is done in a `Scope`. The scope describes or controls how many instances are created for each `Resource`, when to create another one and which of the instances is injected.
Typical scopes are:

* one instance per application (injector)
* one instance per thread
* one instance per injection (known as default)
* one instance per exact `Type` (of the injected/demanded instance)
* one instance per exact `Type` (of the receiving/parent instance)
* ...

The `Scope` takes the role of a factory that creates `Repository`s. Each `Injector` has a single `Repository` per `Scope`. 
So scopes are stateless and should be JVM singletons (constants). All state is held inside the repositories - once per injector. 
It is up to a `Repository` to manage the instances (the state) in the way that is appropiate for the `Scope` they have been created from.

## Overview
All `Scope`s that come with the framework are contained in the `Scoped` util class. 

## Scoping of Bindings
In a binding declaration the `Scope` is the first statement given by `per(Scope)`. Most of the time the part is left out whereby the default - one instance per application - is used.
When used you can make blocks of all binds in the same scope like in the below example:

{% highlight java %}
protected void declare() {
	ScopedBinder appBinder = per( Scoped.APPLICATION );
	appBinder.construct( Foo.class );
	appBinder.construct( Bar.class );
	
	ScopedBinder injectionBinder = per( Scoped.INJECTION );
	injectionBinder.construct( Baz.class );
	injectionBinder.construct( Qux.class );
}
{% endhighlight %}

This can be useful in `Module`s that provide a few binds for different scopes. Especially if the `Binder` is targeted additionally it makes sense to use this way to specify the scope.
In most cases all of the bindings in a `Module` are in the same scope. Here it is more readable to make use of the `BinderModule` and provide the `Scope` to use as constructor argument:

{% highlight java %}
	private static class PerInjectionModule
			extends BinderModule {

		public PerInjectionModule() {
			super(Scoped.INJECTION);
		}

		@Override
		protected void declare() {
			construct( Foo.class );
			construct( Bar.class );
		} 
{% endhighlight %}
But the explicit constructor is just necessary in case another scope than `Scoped``.APPLICATION` should be used.

## Expiry of Scopes

## Snapshots

## Repositories
A `Repository` contains and manages the already created instances. 
How many there are and the conditions to ask the `Injectable` for a new one differ from implementation to implementation.


{% highlight java %}
public interface Repository {

	<T> T serve( Demand<T> demand, Injectable<T> injectable );
}
{% endhighlight %}


 <a class='next' href="services.html"><span class="icon-chevron-right"></span>Services</a>
 
