---
layout : default
---

<p style="width:60%; display: inline-block;">The lightweight <nobr><b>3rd</b> Generation <b>Java</nobr> Dependency Injection</b> (DI) framework.</p>

<div class="teaser">
	<div>
	<h3>100% Independence</h3></th>
	<p>Silk allows DI without making your application depend on the DI framework!</p>
	<ul>
	<li>Configuration by pure Java code</li>
	<li>Separated from your application code</li>
	<li>No further runtime dependencies</li>
	</ul>
	</div>

	<div>
	<h3>Less "Magic"</h3>
	<p>Silk avoids techniques that are hard to understand, debug or maintain.</p>
	<ul>
	<li><b>No</b> XML</li>
	<li><b>No</b> AOP</li>
	<li><b>No</b> proxy magic</li>
	<li><b>No</b> annotations</li>
	<li><b>No</b> overrides</li>
	<li><b>No</b> dependency cycles</li>
	<li><b>No</b> accessible mutable state</li>
	</ul>
	</div>

	<div>
	<h3>if-less Modularity</h3>
	<p>Silk provides few concepts to customize your application in a predictable and maintainable way.</p>
	<ul>
	<li>Sequence of declarations is irrelevant</li> 
	<li>Each part can be considered in isolation</li>
	<li>Simple predictable configuration concepts</li>
	<li>It is easy to remove what you don't like</li>
	</ul>
	</div>		
	
	<div>
	<h3>Robust</h3>
	<p>Silk is designed in a way that allows to forget about it during development without breaking your application.</p>
	<ul>
	<li><b>No</b> importance to sequence of definition</li>
	<li><b>No</b> string matching!</li>
	<li>Largely Type-safe</li>
	<li>Excellent Unit-testability </li>
	<li>Pushes for immutability</li>
	<li><a href="/silk/userguide/data.html">Data driven</a> with Value Objects</li>
	<li>A configuration never changes at runtime</li>
	</ul>
	</div>

	<div>
	<h3>Further Features</h3>
	<ul>
	<li><a href="/silk/userguide/binds.html">Declarative dependency/injection descriptions</a> <a href="/silk/userguide/binds.html#binder">(fluent interface)</a></li>
	<li><a href="/silk/userguide/binds.html#multi">Multi-binds</a></li>
	<li>Generic support</li>
	<li>Easy to utilise or extend</li>
	<li>Fast bootstrap</li>
	<li>Lightweight: about 120 KB jar archive</li>
	<li>Safe renaming (e.g. classes or instance names)</li>
	</ul>
	</div>
</div>

{% highlight java %}
protected void bootstrap() {
	install( Silk.class );
}
protected void declare() {
	bind(Awesomeness.class).to(YourApp.class);
}
{% endhighlight %}

With Silk your code is no longer cluttered by annotations to guide the dependency injection. 
Everything is described in external pure Java declarations provided through a fluent interface.

Silk absolutely prevents your application code from getting dependent on the DI-framework and 
vigorously encourages the use of constructor injection so that your code works just as well autonomously. 
As a result an application design is driven that can be tested nicely selfcontained by unit tests without the complexity of frameworks.

Silks is a small flexible core that is easy to utilise and extend. 
It helps to decouple your application with elegance by providing a well-considered handful of 
techniques to modularise and configure your programs without conditional declarations. 
Together with Silk's immutability this brings a functional nature to it wherein the sequence of declarations becomes irrelevant! 

Most of all Silk aims to allow easy long term development that comes with no surprises so it vehemently avoids any kind of _\*magic\*_ and guides you with helpful error messages.
Above all it allows to consider small components separately whereby even large, highly configurable systems become manageable without pain. 

The absence of any further runtime dependencies and its slim footprint of 120K makes Silk a fine 
choice for small projects although at heart Silk is a graceful mighty butterfly waiting to breathe life into huge applications.

Through its revolutionary service concept Silk decouples your application and prevents dependency cycles by concept. 
Further it allows to tackle cross-functional concerns with pure Java free of meta programming approaches.

<h2 class="home">Highlights</h2>

### Pure Java
### Application code stays independent 
### Sequence of declaration is irrelevant
### No dependency cycles by concept with services

