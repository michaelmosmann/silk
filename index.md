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

<div>
<a href="highlights.html" class="icon"><span class="icon-lightbulb"></span>Highlights</a>
<span class="bullet">Lightweight</span>
<span class="bullet">Pure Java</span>
<span class="bullet">Application code stays independent</span>
<span class="bullet">Says NO to magic and pain</span>
<span class="bullet">Sequence of declaration is irrelevant</span>
<span class="bullet">No dependency cycles by concept</span>
<span class="bullet">Injectrons</span>
<span class="bullet">Cross-functional programming</span>
</div>

----
<b class="bullet">Silk allows DI without making your application depend on the DI framework!</b>
<div>
<a class="icon"><span class="icon-plus"></span>100% Independent</a>
<span class="bullet">Configuration by pure Java code</span>
<span class="bullet">Separated from your application code</span>
<span class="bullet">No further runtime dependencies</span>
</div>

----
<b class="bullet">Silk avoids techniques that are hard to understand, debug or maintain.</b>
<div>
<a class="icon"><span class="icon-ban-circle"></span>Less "Magic"</a>
<span class="bullet"> <b>No</b> XML</span>
<span class="bullet"> <b>No</b> AOP</span>
<span class="bullet"> <b>No</b> proxy magic</span>
<span class="bullet"> <b>No</b> annotations</span>
<span class="bullet"> <b>No</b> overrides</span>
<span class="bullet"> <b>No</b> dependency cycles</span>
<span class="bullet"> <b>No</b> accessible mutable state</span>
<span class="bullet"> <b>No</b> code generation</span>
</div>

----
<b class="bullet">Silk provides few concepts to customize your application in a predictable and maintainable way.</b>
<div>
<a class="icon"><span class="icon-sitemap"></span>if-less Modularity</a>
<span class="bullet">Sequence of declarations is irrelevant</span> 
<span class="bullet">Each part can be considered in isolation</span>
<span class="bullet">Simple predictable configuration concepts</span>
<span class="bullet">It is easy to remove what you don't like</span>
</div>

----
<b class="bullet">Silk is designed in a way that allows to forget about it during development without breaking your application.</b>
<div>
<a class="icon"><span class="icon-umbrella"></span>Robust</a>
<span class="bullet"><b>No</b> importance to sequence of definition</span>
<span class="bullet"><b>No</b> string matching!</span>
<span class="bullet">Largely Type-safe</span>
<span class="bullet">Excellent Unit-testability </span>
<span class="bullet">Pushes for immutability</span>
<span class="bullet"><a href="userguide/data.html">Data driven</a> with Value Objects</span>
<span class="bullet">A configuration never changes at runtime</span>
</div>

----
<div>
<a class="icon"><span class="icon-gift"></span>Further Features</a>
<span class="bullet"><a href="userguide/binds.html">Declarative</a> <a href="userguide/binds.html#binder">(fluent interface)</a></span>
<span class="bullet"><a href="userguide/binds.html#multi">Multi-binds</a></span>
<span class="bullet">Generic support</span>
<span class="bullet">Easy to utilise or extend</span>
<span class="bullet">Fast bootstrap</span>
<span class="bullet">Lightweight: less than 120 KB jar archive</span>
<span class="bullet">Safe renaming (e.g. class or instance names)</span>
</div>
<br/>
<br/>