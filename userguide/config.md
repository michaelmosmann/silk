---
layout : userguide
title : Configuration
---
# Configuration

## A\. Concept
Silk's configuration is an important part of its `if`-_less_ <a href="modularity.html">modularization</a> concept. 
The configuration objects are the data that controls the composition of `Module`s and `Bundle`s instead of 
adding control flow code within those. Hence the composition is not hard-coded but data-driven. 
This is more flexible since the same composition tree can be interpreted different depending on **data**
and it is easier to understand since all _conditions_ will be in one place directly before the bootstrapping.

## 1\. Globals
All configuration are passed as a single `Globals` value object to the bootstrapping process:

{% highlight java %}
Globals globals = Globals.STANDARD;
// ... use fluent interface
Injector injector = Bootstrap.injector( RootBundle.class, globals );
{% endhighlight %}

Thereby the different configuration objects are immutables. Their methods can be used as a fluent interface.
It is important to remember to (re-) assign the results since no state is changed in place. 
As a starting point the `Globals.STANDARD` value is used.

The `Globals` is a record value object consisting of 3 parts:

- `Edition`s: A filter function on `Module` and/or `Bundle` `.class`es.  
- `Options`: A set of selections (1 choice) or alternatives (many choices) of fixes option sets (modeled through enums) that are used to control e.g. `ModularBundle` installation.
- `Presets`: A typed key-value map holding 1 value per exact `Type` that is propagated to `PresetModule`s.

## 2\. Editions
An `Edition` is a filter function that allows to exclude individual `Module`s or hole `Bundle`s based on the `class`.
{% highlight java %}
interface Edition {

	boolean featured( Class<?> bundleOrModule );
}
{% endhighlight %}
With this simple interface there can be various strategies use to compose an application based on high level _functional_ parts that represent features.

#### Select Features by Packages
Silk ships with 2 simple but very useful kinds of editions. The first lets you define _features_ based on a `Packages` set.
{% highlight java %}
Globals globals = Globals.STANDARD.edition( Packages.packageAndSubPackagesOf( ExtentionA.class ) );
{% endhighlight %}

### Features
The second build-in `Edition` literally sees an edition as a set of `Feature`s.
In contrast to the filter character of an edition a specific feature is derived from a `Module` or `Bundle` `.class`.
That allows to map sets of module and bundle classes to one conceptual _feature_, an idea. 
Silk's feature use user defined `enum`s to represent the set of possible features.
{% highlight java %}
Globals globals = Globals.STANDARD.edition( MyFeatures.A, MyFeatures.C, MyFeatures.M  );
{% endhighlight %}

A full example is given in `TestEditionFeatureBinds`.

## 3\. Options
(see [Modularisaion: Conditional Installation](modularity.html#options))

## 4\. Presets
(see [Modularisaion: Preset Modules](modularity.html#presets))


<a class='next' href="services.html"><span class="fa fa-chevron-right"></span>Services</a>
