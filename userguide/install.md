---
layout : default
title: Installation
---

# Installation

## Get It
<a href="downloads.html">Download</a> the `jar`-file and add it to your project's classpath.

## Use It
Define a first `Module` (e.g. by extending `BinderModule`)
{% highlight java %}
class FirstModule extends BinderModule {

	@Override
	protected void declare() {
		bind(SomeInterface.class).to(SomeImplementation.class);
	}
}{% endhighlight %}

Define a root `Bundle` that installs your first `Module`
{% highlight java %}
class RootBundle extends BootstrapperBundle {

	@Override
	protected void bootstrap() {
		install( FirstModule.class );
	}
}	{% endhighlight %}

Use the `Bootstrap` util to create an `Injector` from your root `Bundle`
{% highlight java %}
Injector injector = Bootstrap.injector( RootBundle.class );	{% endhighlight %}
	
Create a `Dependency` and resolve it using the created `Injector` to construct your application's root instance (in above example it is `SomeInterface.class`)
{% highlight java %}
Dependency<SomeInterface> dependency = Dependency.dependency(SomeInterface.class); 
SomeInterface instance = injector.resolve(dependency);	{% endhighlight %}

Thats it! We are done!

While you need to use `Dependency` for the root instance of your application of cause no further `Dependency` has to be created manually further on. 
All the dependencies will just be injected as you told Silk with the `Module`s and `Bundles`. 

 <a class='next' href="/silk/userguide">Continue with the User Guide...</a>