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

<table class="toc">
<tr>
	<th>#<a href="#field_injection">A. Instance Field Injection</a></th>
	<td></td>
</tr>
<tr>
	<th>#<a href="#static_injection">B. Static Field Injection</a></th>
	<td></td>
</tr>
<tr>
	<th>#<a href="#setter_injection">C. Setter Injection</a></th>
	<td></td>
</tr>
<tr>
	<th>#<a href="#reinjection">D. Reinjection</a></th>
	<td></td>
</tr>
<tr>
	<th>#<a href="#annotations">E. Annotation Injection Guidance</a></th>
	<td></td>
</tr>
<tr>
	<th>#<a href="#aspects">F. Aspects &amp; Proxies</a></th>
	<td></td>
</tr>
</table>

## Beliefs
General assumptions and beliefs that are based on empirical evidence:

- Every feature that exists will be used
- Every feature that is easy to abused will be abused
- Popularity is no evidence of quality nor correctness or accuracy
- The simplicity of usage is totally unrelated to the complexity of its consequences  

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
All problems of <a href="#reinjection">D.&nbsp;Reinjection</a> apply to accessible fields as well.

## <a id="static_injection"></a> B. Static Field Injection

#### Constant Fields
Constant fields are **constant** - using reflection (via DI or not) to change that violates the expectations of the programmer looking at the code. 
This removes the predictability that is connected to the concept of `static` values (_globals_). When predictability is already lost on the _global_ level
it is of cause not possible any longer to have good predictability in the overall application at runtime. Thereby it does not matter if DI just replaces the constant once since tha actual value still is not what the programmer sees in the code wherefore he will be wrong about his expectations. 

#### Class Fields
That are non-`final` but `static` fields. Such a field is de-facto a JVM wide singleton. Manipulation that from within a DI container is violating the hole purpose of having such a container in order to be able to have _singletons_ that not _collide_ when you have 2 processes running the same application. The fact that the container should change the value of the field must mean that the used value is a managed context instance. This can just work for one context when there are two _working_ with the same class field. 

Such a _static patch_ might be the only way to avoid larger adaptation of library code we cannot change that hasn't been build for multiple parallel contexts in the same JVM. If so we should not delegate this _patch_ to the DI container where it becomes _invisible_ to the programmer. Since there is already the static limitation we could inject the needed dependency into a container _singleton_ that does that _static patch_ manually so that it becomes obvious to the programmer. This allows to rely on constructor injection again and gives the possibility to write a **OBS!!!** comment  right next to the code that does the _patch_.


## <a id="setter_injection"></a> C. Setter Injection
The concerns with setter injection are not about the technique itself but about the consequences when using the resulting type. Setters do not enforce correct initialization in the way a constructor can. A programmer can't derive what setters are necessary to call and which might be optional when using the type without DI. Moreover this code does not _break_ (compile error) when new setters are introduced (as it would be the case with constructors). To argue that a type is just use via a DI container is to argue that the code _depends_ on this technique. Still the goal has to be to keep DI as transparent as possible, hence to allow an application to work clean and stable wired manually as well.

Further setters deny any initialization logic (like what to do in `null` argument case) or make it very cumbersome and error prone since the sequence of setter invocations can't be enforced but other values might play a role in the logic. When using constructor injection this is again very simple, straight forward and stable. 

As <a href="field_injection">A.&nbsp;Instance Field Injection</a> setters are sometimes seen as _good_ solution when dealing with many dependencies. Especially when some are so called _optional_ dependencies. Again it must be said that many dependencies are a design flaw that is more persistent than solved using setters (this was discussed in more detail at <a href="field_injection">A.</a>). 

Beyond that setters deny the usage of `final` state, whereby all problems of mutable accessible state and in particular <a href="reinjection">D.&nbsp;Reinjection</a> apply orthogonal to the problems described above what causes a multiplication of complexity whereby it becomes hard or impossible to understand or predict the actual behavior of the type instance and the application using it in all occurring cases.

Last but not least to argue that setter injections allows dependency cycles without proxies is to argue that dependency cycles are a desirable characteristic or _unresolvable_.
Both arguments are easy to prove wrong. 

## <a id="reinjection"></a> D. Reinjection
To see re-injection as _feature_ implicates the wish to mutate the application composition during runtime. This might arise from different reasons.

#### Scope related re-injection
An idea might be that a _dynamic_ instance (e.g. exist per request) should become usable within a _static_ one (application singleton or session).
This is plain wrong and a programming error since it does not work when used in parallel (e.g. two threads). 

#### Event related re-injection
Another idea could be to mutate the composition of the application after an _event_ occurred (like a _setting_ has been changed).
The changed composition state manifests in the changing field instance. Very likely this field is used as if it is _stable_. For example the field is **not** assigned to a local variable at the beginning of each method so that the method is accessing the field multiple times. Through the occurred event this might be **another instance** half way through the method. No programmer will anticipate this and program in a way that is not affected by this potential problem. But for sure the algorithm of a method will go wrong when a dependency that was assumed to be _stable_ is changing during computation. The results are unpredictable behavior and return values.

Whereas the programmer will anticipate such a problem in case the _dynamic_ of a field is made _visible_ by using a indirection like a _provider_. 
This concept also works correctly for the interaction of different scopes. It is a better physical and technical representation of the _dynamical_ nature and its solution. 

In general the idea of re-injection is the idea of mutable state on a high behavioral level that is hard to think through in all its cases and consequences. This needs to be avoided as much as possible. It is easy to prove that it is possible in every case.

## <a id="annotations"></a> E. Annotation Injection Guidance

#### DI Container as Dependency
Annotations defined by the DI _framework_ make large parts of the user code _depend_ on the DI framework code.
This is surely wrong. In the attempt to decouple a program by using DI the program effectively is widely coupled **to** the DI framework.
The direction of dependency has been inverted but in the wrong direction.

This flaw could conceptually be repaired fairly easy. The annotation should not be part of the DI container but is passed as a configuration to the bootstrapping.
If all containers would follow this principle a container user could switch much easier than he could today. 

The attempt to standardize the annotations used and make those available in the JRE is flawed in itself and not a good way to change the dependency direction.
It persist the actual solution characteristics as they have been understood when the annotations have been designed. 
Deeper insight into the domain of dependency injection and the resulting improvements and conceptual changes are hard to add and enforce. 
Actual implementations will always be limited to the conceptual model implied by the annotations. Additions will use special DI container specific annotations that
a user would need to change when changing the container. A better approach would have been to standardize the notions of concepts so that each framework uses the 
same language for identical concepts. Than a user could keep all of _his own_ annotations that are just mapped again to the configuration of another container.

#### Limitations of Annotations
...

## <a id="aspects"></a> F. Aspects &amp; Proxies
...

