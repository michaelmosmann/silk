---
layout : default
---

<tour>
&nbsp;<br/>Silk DI<br/><em>a lightweight</em> <br/>Java<br/><em>Dependency Injection</em></br> tool
</tour>

<abstract>
With **Silk** your code is no longer cluttered by <s>`@annotations`</s> to guide the dependency injection. 
Everything is described in external pure Java declarations provided through a <a href="/userguide/binds.html#binder">fluent interface</a>.
</abstract>

<a class="next" href="userguide/snippets.html"><span class="icon-reorder"> </span> Show me some code</a>

{% highlight java %}
protected void bootstrap() {
	install( Silk.class );
}
protected void declare() {
	bind(Awesomeness.class).to(YourApp.class);
}
{% endhighlight %}

----

<div class="icon"><span class="icon-adjust"></span><a href="help/comparison.html">Comparison</a></div>

- Silk DI
- vs. Spring Ioc
- vs. Google Guice
- vs. Pico container

----

<div class="icon"><span class="icon-lightbulb"></span><a href="highlights.html">Highlights</a></div>

- Lightweight
- Pure Java
- Application code stays independent
- <a href="/help/harmful.html">Says NO</a> to magic and pain
- Sequence of declaration is irrelevant
- Pushes for constructor injection
- Injectrons
- Cross-functional programming

----

<b class="bullet">Silk allows DI without making your application depend on the DI framework!</b>
<div class="icon"><span class="icon-cog"></span>100%<br/>Independent</div>

- Configuration by **pure Java** code
- Separated from your application code
- No further runtime dependencies
- **No annotations** in your code

<b class="bullet">Silk avoids techniques that are hard to understand, debug or maintain.</b>
<div class="icon"><span class="icon-magic"></span><span class="icon-ban-circle" style="font-size:50px; padding:5px; color: #b43639;"></span>No "Magic"</div>

-  <b>No</b> XML
-  <b>No</b> AOP
-  <b>No</b> proxies
-  <b>No</b> annotations
-  <b>No</b> overrides
-  <b>No</b> dependency cycles
-  <b>No</b> accessible mutable state
-  <b>No</b> code generation


<b class="bullet">Silk has all the features that often require additional libs in other frameworks.</b>
<div class="icon"><span class="icon-gift"></span>Feature-Rich</div>

- <a href="userguide/binds.html">Declarative</a>, <a href="userguide/binds.html#binder">fluent interface</a></span>
- <a href="userguide/binds.html#multi">Multi-binds</a></span>
- <a href="userguide/intro.html#data">Uniform Generic support</a>
- Easy to utilise or extend
- <a href="userguide/scopes.html">Scopes</a>
- <a href="userguide/services.html">Services</a>
- <a href="userguide/binds.html#inspect">Custom Inspection</a>


<b class="bullet">Silk provides few concepts to configure an app in a predictable and maintainable way.</b>
<div class="icon"><span class="icon-th-large"></span>if-less<br/><a href="userguide/modularity.html">Modularity</a></div>

- Sequence of declarations is irrelevant 
- Each part can be considered in isolation
- Simple predictable configuration concepts
- It is easy to <a href="userguide/modularity.html#uninstall">remove</a> what you don't like
- Fast bootstrap

<b class="bullet">Silk is designed for easy development where refactoring does not break an application.</b>
<div class="icon"><span class="icon-umbrella"></span>Robust</div>

- <b>No</b> importance to sequence of definition
- Safe renaming
- Largely Type-safe
- Excellent Unit-testability 
- Pushes for immutability
- <a href="userguide/data.html">Data driven</a> with Value Objects
- A configuration never changes at runtime


<br/>
<br/>
