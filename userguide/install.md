---
layout : userguide
title: Installation
---

# Installation

<abstract>
Since there are no further dependencies the installation of Silk is really simple.
</abstract>

<table class='toc'>
	<tr><th>#<a href="#get">Get It</a></th><td>How to integrate the library in a build</td></tr>
	<tr><th>#<a href="#use">Use It</a></th><td>How to include the library in a project</td></tr>
</table>

## <a id="get"></a>Get It
I know, maven and co are quite popular... but all I can observe is people 
creating messes as big as they can manage and maven will surely make
their mess a big and messy one. So all I have to say is: _Luke, go with with 
the source - or you'll end up on the dark side._

### Manually 
<a href="/downloads/">Download</a> the latest `jar`-file and add it to your 
project's classpath.

### Maven(s)
{% highlight xml %}
 	<dependency>
		<groupId>se.jbee</groupId>
		<artifactId>silk-di</artifactId>
		<version>0.6</version>
	</dependency>
{% endhighlight %}

Ivy
{% highlight xml %}
	<dependency org="se.jbee" name="silk-di" rev="0.6"/>
{% endhighlight %}

Gradle
{% highlight java %}
	'se.jbee:silk-di:0.6'
{% endhighlight %}


## <a id="use"></a>Use It
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

While you need to use `Dependency` for the root instance of your application of course no further `Dependency` has to be created manually further on. 
All the dependencies will just be injected as you told Silk with the `Module`s and `Bundle`s. 

 <a class='next' href="intro.html"><span class="fa fa-chevron-right"></span>Introduction</a>
