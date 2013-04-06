---
layout : doc
title : Considered Harmful
---
<tour class="c-help">
See Also
<a href="comparison.html"><span class="icon-adjust"></span> Comparison</a>
</tour>
<a href="https://groups.google.com/group/silk-di" class="serif" style="text-decoration: none; position: absolute; top: 10px; margin-left: 660px; color:black;"><span class="icon-comments" style="font-size: 48px; text-align: right;"></span> <span style="display:inline-block;"><i>Join the Discussion</i><br/>in the <b>User Group</b></span></a>

# <span class="icon-ban-circle"></span> Considered Harmful

<abstract>
The philosophy of Silk is to think about the prize of possible features. Often we tend to see just what we _get_ and not what it will _bring_ or _mean_ as a consequence as well. Therefore Silk breaks with a lot of popular techniques and considers them to be harmful. Of cause this deserves an explanation based on facts and objective arguments. I will try to let you participate in my thoughts about different concepts that Silk avoids - not because they would be hard to implement but **because of the prize** that we would pay. 
</abstract>

## Beliefs
General assumptions and beliefs that are based on empirical evidence:

- Every feature that exists will be used

## <a id="field_injection"></a> A. Instance Field Injection
To see why it is harmful it is important to analyze why we would do field injection (in favor of constructor injection).
When field injection is used the field could be accessible (`public`) or inaccessible for usual programmatic access. 

#### Inaccessible Fields
When we use field injection because we made the field _private_ (non `public` of some form) this implicates that we cannot initialize the field using the constructor (otherwise we would have done so and made the field `private final`).
In other words, we need _reflection_ of one kind to initialize a instance. This is a design flaw that denies to use the type's _API_ as is. 
As a consequence DI, a _mocking_ tool or custom reflection setup is for example also needed in tests. 

Field injection is sometimes favored over constructor injection in cases where parameters feel cumbersome because of the pure amount of fields and therefore constructor parameters. 
Of cause this is a design flaw. To use field injection is a bad fix that helps to persists this kind of flaws. Obviously a class with many fields (that are injected dependencies) has to much responsibility. It should be split in smaller units that compose better with less parameters and hence less dependencies. We should not mess around with dependencies just because a DI framework will take care of that mess.

#### Accessible Fields
When we inject fields that are also accessible it means they cannot be `final` at the same time. Hence we have accessible mutable state in the type's _API_. 
As a consequence you cannot rely on all the code connected to or dependent on this field since it can change any time changed from anywhere in a usual programmatic way. 
We have to expect that this kind of access will happen at some point and we have to admit that we do not fully understand the implications when that happens. 

## <a id="static_injection"></a> B. Static Field Injection

#### Constant Fields
Constant fields are **constant** - using reflection (via DI or not) to change that violates the expectations of the programmer looking at the code. 
This removes the predictability that is connected to the concept of `static` values (_globals_). When predictability is already lost on the _global_ level
it is of cause not possible any longer to have good predictability in the overall application at runtime. Thereby it does not matter if DI just replaces the constant once since tha actual value still is not what the programmer sees in the code wherefore he will be wrong about his expectations. 

#### Class Fields
That are non-`final` but `static` fields. Such a field is de-facto a JVM wide singleton. Manipulation that from within a DI container is violating the hole purpose of having such a container in order to be able to have _singletons_ that not _collide_ when you have 2 processes running the same application. The fact that the container should change the value of the field must mean that the used value is a managed context instance. This can just work for one context when there are two _working_ with the same class field. 

Such a _static patch_ might be the only way to avoid larger adaptation of library code we cannot change that hasn't been build for multiple parallel contexts in the same JVM. If so we should not delegate this _patch_ to the DI container where it becomes _invisible_ to the programmer. Since there is already the static limitation we could inject the needed dependency into a container _singleton_ that does that _static patch_ manually so that it becomes obvious to the programmer. This allows to rely on constructor injection again and gives the possibility to write a **OBS** comment  right next to the code that does the _patch_.

 
