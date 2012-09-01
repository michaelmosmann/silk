---
layout : default
title: Installation
---

# Installation

* <a href="downloads.html">Download</a> the `jar`-file and add it to your project's classpath.

* Define a first `Module`	
{% highlight java %}
class FirstModule extends BinderModule {

	@Override
	protected void declare() {
		bind(SomeInterface.class).to(SomeImplementation.class);
	}
}{% endhighlight %}

* Define a root `Bundle` that installs your first `Module`
{% highlight java %}
class RootBundle extends BootstrapperBundle {

	@Override
	protected void bootstrap() {
		install( FirstModule.class );
	}
}	{% endhighlight %}

* Use the `Bootstrap` util to create an `Injector` from your root `Bundle`
{% highlight java %}
Injector injector = Bootstrap.injector( RootBundle.class );	{% endhighlight %}
	
* Create a `Dependency` and resolve it using the created `Injector` to construct your application's root instance (here our example `SomeInterface.class`)
{% highlight java %}
Dependency<SomeInterface> dependency = Dependency.dependency(SomeInterface.class); 
SomeInterface instance = injector.resolve(dependency);	{% endhighlight %}

Thats it! We are done!
