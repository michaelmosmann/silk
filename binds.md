---
layout : default
title : Bindings
---
# Bindings

## Ordinary Bindings

## Multi-Bindings
A `multibind` is used to create collections of elements (instances) having the same supertype that should be injected together.

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

## Auto-bindings

