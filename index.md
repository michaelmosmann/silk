---
layout : default
---

<div class="teaser">
	<h3>100% Independence</h3></th>
	<p>Silk allows DI without making your application depend on the DI framework!</p>
	<ul>
	<li>Configuration by code</li>
	<li>Separated from your application code<sup>1</sup></li>
	<li>No further runtime dependencies</li>
	</ul>		

	<h3>if-less Modularity</h3>
	<p>Silk provides few concepts to customize your application in a predictable and maintainable way.</p>
	<ul>
	<li>Bundles always just installed once<br/><small>multiple install-calls or circles are allowed</small></li> 
	<li>Uninstall bundles</li>
	<li>Simple configuration <br/><small>through editions, features and constant properties</small></li>
	</ul>		

	<h3>Less "Magic"</h3>
	<p>Silk avoids techniques that are hard to understand, debug or maintain.</p>
	<ul>
	<li><b>No</b> XML</li>
	<li><b>No</b> AOP</li>
	<li><b>No</b> proxy magic</li>
	<li><b>No</b> annotations<sup>2</sup></li>
	<li><b>No</b> accessible mutable state <br/><small>A configuration will never change during runtime</small></li>
	<li><b>No</b> overrides</li>
	<li><b>No</b> dependency cycles</li>
	</ul>		
	<h3>Robust</h3>
	<p>Silk is designed in a way that allows to forget about it during development without breaking your application.</p>
	<ul>
	<li><b>No</b> importance to sequence of definition</li>
	<li><b>No</b> string matching! <br/><small>Allows safe renaming</small></li>
	<li>Type-safe <br/><small>seldom just raw-type safety</small></li>
	<li>Excellent Unit-testability </li>
	<li>Pushes for immutability <br/><small>enforces constructor injection</small></li>
	<li>Data driven <br/><small>core uses mostly Value Objects</small></li>
	</ul>


	<h3>Features</h3>
	<ul>
	<li>Declarative dependency descriptions <br/><small>using a guice-like builder</small></li>
	<li>Multi-binds</li>
	<li>Full generic support</li>
	<li>Easy to extend <br/><small>e.g. with Set or List support</small></li>
	<li>Fast bootstrap</li>
	<li>Lightweight: about 120 KB jar archive</li>
	</ul>		
</div>

{% highlight java %}
protected void declare() {
	bind(Awesomeness.class).to(Silk.class);
}
{% endhighlight %}

The <b>3rd</b> Generation Java <br/><i>Inversion of Control (IoC)</i> / <i>Dependency Injection (DI)</i> framework

With Silk your code is no longer cluttered by annotations to guide the dependency injection. Everything is described in external declarations provided through a fluent interface.  

Silk absolutely prevents your application code from getting dependent on the DI-framework and vigorously encourages the use of constructor injection so that your code works perfect autonomously what also achieves an application design that can be tested gracefully with unit tests without the complexity of frameworks.

Silk helps to decouple your application elegant and provides a well-considered small set of techniques to modularise and configure you programs without if (or other) conditions. 
### Highlights

soon...