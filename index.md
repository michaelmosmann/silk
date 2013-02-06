---
layout : default
---
<tour>
&nbsp;<br/>Silk DI<br/><em>a lightweight</em> <br/>Java<br/><em>Dependency Injection</em></br> tool
</tour>

<abstract>
With **Silk** your code is no longer cluttered by <s>`@annotations`</s> to guide the dependency injection. 
Everything is described in external pure Java declarations provided through a fluent interface.
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

<div class="icon"><span class="icon-lightbulb"></span><a href="highlights.html">Highlights</a></div>

- Lightweight
- Pure Java
- Application code stays independent
- Says NO to magic and pain
- Sequence of declaration is irrelevant
- No dependency cycles by concept
- Injectrons
- Cross-functional programming

----

<b class="bullet">Silk allows DI without making your application depend on the DI framework!</b>
<div class="icon"><span class="icon-resize-full"></span>100%<br/>Independent</div>

- Configuration by pure Java code
- Separated from your application code
- No further runtime dependencies


<b class="bullet">Silk avoids techniques that are hard to understand, debug or maintain.</b>
<div class="icon"><span class="icon-ban-circle"></span>Less "Magic"</div>

-  <b>No</b> XML
-  <b>No</b> AOP
-  <b>No</b> proxy magic
-  <b>No</b> annotations
-  <b>No</b> overrides
-  <b>No</b> dependency cycles
-  <b>No</b> accessible mutable state
-  <b>No</b> code generation


<b class="bullet">Silk provides few concepts to configure an app in a predictable and maintainable way.</b>
<div class="icon"><span class="icon-sitemap"></span>if-less<br/>Modularity</div>

- Sequence of declarations is irrelevant 
- Each part can be considered in isolation
- Simple predictable configuration concepts
- It is easy to remove what you don't like


<b class="bullet">Silk is designed for easy development where refactoring does not break an application.</b>
<div class="icon"><span class="icon-umbrella"></span>Robust</div>

- <b>No</b> importance to sequence of definition
- <b>No</b> string matching!
- Largely Type-safe
- Excellent Unit-testability 
- Pushes for immutability
- <a href="userguide/data.html">Data driven</a> with Value Objects
- A configuration never changes at runtime


<div class="icon"><span class="icon-gift"></span>Further<br/>Features</div>

- <a href="userguide/binds.html">Declarative</a> <a href="userguide/binds.html#binder">(fluent interface)</a></span>
- <a href="userguide/binds.html#multi">Multi-binds</a></span>
- Generic support
- Easy to utilise or extend
- Fast bootstrap
- Lightweight: less than 120 KB jar archive
- Safe renaming
<br/>
<br/>