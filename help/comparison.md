---
layout : doc
title : Comparison
---
<tour class="c-help">
See Also
<a href="harmful.html"><span class="icon-ban-circle"></span> Considered Harmful</a>
<a href="motivation.html">Motivation</a>
</tour>
# <span class="icon-adjust"></span> Comparison

<abstract>
In the below tables the features of 4 different frameworks are compared with each other. 
The features are judged by their influence of the application design and development process as seen by the author of Silk. 
A <span class="good">good</span> influence is indicated green, a <span class="fair">fair</span> one yellow and a <span class="bad">bad</span> one red. Neutral information is kept gray. This <a onclick="javascript:$('table.compare').toggleClass('highlighted');"><span class="icon-th"></span> <span style="color:blue; cursor: pointer;">table highlighting</span></a> is turned **off** by default.<br/><br/>Concepts or techniques that are <a href="harmful.html" class="harmful">considered harmful</a> have dark red text as well. Missing or unsure information is indicated by a `'?'`.
</abstract>


## Library
<div><a class="highlight-toggle" onclick="javascript:$('table.compare').toggleClass('highlighted');"><span class="icon-th"></span> Highlight on/off</a></div>
<table class="compare">
<tr>
	<th>Feature</th><th><a href="http://www.springsource.org/">Spring</a></th><th><a href="http://code.google.com/p/google-guice/">Guice</a></th><th><a href="http://picocontainer.com/">pico-container</a></th><th>Silk</th>
</tr>
<tr>
	<th>Version</th> 
	<td>3.x</td>	
	<td>3.x</td>
	<td>2.14.x</td>
	<td>0.5</td>
</tr>
<tr>
	<th>Archive Size</th> 
	<td class="bad">several MB</td>	
	<td class="fair">&gt; 650KB <i>+ plug-ins</i></td>
	<td class="good">&gt; 300KB <i>+ gems</i></td>
	<td class="good">170KB</td>
</tr>
<tr>
	<th>Further dependencies</th> 
	<td class="bad">good dozen</td>	
	<td class="fair">few</td>
	<td class="good"><b>none</b></td>
	<td class="good"><b>none</b></td>
</tr>
</table>


## API 
<div><a class="highlight-toggle" onclick="javascript:$('table.compare').toggleClass('highlighted');"><span class="icon-th"></span> Highlight on/off</a></div>
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Methods in injector/context</th> 
	<td class="bad">20+</td>
	<td class="bad">20+</td> 
	<td class="fair">14</td> 
	<td class="good"><a href="/assets/javadoc/0.4/se/jbee/inject/Injector.html"><b>1</b></a> (<a href="/userguide/intro.html#inject">example</a>)<i>having 1 argument</i></td>
</tr>
</table>


## Concept
<div><a class="highlight-toggle" onclick="javascript:$('table.compare').toggleClass('highlighted');"><span class="icon-th"></span> Highlight on/off</a></div>
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Container model</th> 
	<td class="bad">flat instances</td>
	<td class="bad">flat instances</td> 
	<td class="fair">hierarchical instances <i>explicit modeled<i></td> 
	<td class="good">hierarchical instances <i>"production rules"</i></td>
</tr>
<tr>
	<th>Configuration style</th> 
	<td class="fair">static <i><b>XML</b>, annotation</i></td> 
	<td class="good">static + programmatic <i><b>annotation</b> + <nobr>fluent interface</nobr></i></td>
	<td class="fair">static + programmatic <i><b>composition</b> + fluent interface + annotation</i></td>
	<td class="good">programmatic declarative<i><b>fluent interface</b></i></td>
</tr>
<tr>
	<th>Wiring style</th>
	<td class="bad">automatic "scan" <i>by name <b>or</b> type</i></td>	
	<td class="fair">explicit <i>by name <b>and</b> type</i></td>
	<td class="fair">explicit <i>by name <b>and</b> type</td>
	<td class="good">explicit <i>name + type + hierarchy<i/></td>
</tr>
<tr>
	<th>Wiring Transparency / Independence</th> 
	<td class="good">yes <i>XML <b>or</b> config-classes</i></td>	
	<td class="bad">no <i>annotations required</i></td>
	<td class="good">yes <i>(except annotation feat.)</i></td>
	<td class="good"><b>yes</b> <i>pure fluent interface</i></td>
</tr>
<tr>
	<th>"External" code handling<i>e.g. a external lib or JRE type</i></th>
	<td class="fair">limited <i>(name/type wiring only)</i></td>	
	<td class="fair">indirect <i>requires <tt>Provider</tt></i></td>
	<td class="fair">limited <i>(to some features)</i></td>
	<td class="good"><b>as own</b><i>uniform</i></td>
</tr>
<tr>
	<th>Coupling style<i>How interface/impl. is brought together</i></th> 
	<td class="fair">referenceable</td>	
	<td class="good">loose</td>
	<td class="fair">referenceable</td>
	<td class="good">loose</td>
</tr>
</table>


## Types
<div><a class="highlight-toggle" onclick="javascript:$('table.compare').toggleClass('highlighted');"><span class="icon-th"></span> Highlight on/off</a></div>
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Generics support<i>(Consider same type with different generic to be different)</i></th>
	<td class="bad">no</td>
	<td class="fair">yes <i class="harmful">each a literal class!</i></td>
	<td class="bad">case-related</td>
	<td class="good"><b>yes</b> <i><code>Type</code> instance</i></td>
</tr>
<tr>
	<th>Generic type safty</th>
	<td>-</td>	
	<td class="good">compile time</td>
	<td>-</td>
	<td class="fair">largely compile time<i>full at runtime</i></td>
</tr>
<tr>
	<th>Wildcard generics</th> 
	<td>-</td>	
	<td class="fair">yes<i>explicit via literal</i></td>
	<td>-</td>
	<td class="good"><a href="/userguide/binds.html#star"><b>yes</b></a></td>
</tr>
<tr>
	<th>Primitive types handling</th> 
	<td>?</td>	
	<td class="good">primitive == wrapper</td>
	<td>?</td>
	<td class="good">primitive == wrapper</td>
</tr>
<tr>
	<th>Bind to all (generic) supertypes</th> 
	<td class="bad">no</td>	
	<td class="bad">no</td>
	<td class="bad">no</td>
	<td class="good"><a href="/userguide/binds.html#auto"><b>yes</b></a></td>
</tr>
<tr>
	<th>Type links</th> 
	<td class="bad">no</td>	
	<td class="good">yes</td>
	<td class="bad">no</td>
	<td class="good"><a href="/userguide/binds.html#basics"><b>yes</b></a></td>
</tr>
</table>


## Injection
<div><a class="highlight-toggle" onclick="javascript:$('table.compare').toggleClass('highlighted');"><span class="icon-th"></span> Highlight on/off</a></div>
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Annotation guidance <i><span class="harmful">(considered harmful)</span> <a href="harmful.html#annotations">Why?</a></i></th>
	<td class="fair">required <i>XML alternative</i></td>	
	<td class="bad">required</td>
	<td class="good">less important alternative</td>
	<td class="good"><b>no need<b></td>
</tr>
<tr>
	<th>Constructor injection</th>
	<td class="fair">yes <i class="harmful">annotations or XML<i></td>	
	<td class="fair">yes <i class="harmful">requires annotations</i></td>
	<td class="good"><b>idiomatic</b> <i>most to few args</i></td>
	<td class="good"><b>idiomatic</b> <i>fluent interface</i><i><a href="/userguide/binds.html#parameters">many options</a><i></td>
</tr>
<tr>
	<th>Field injection<i><span class="harmful">(considered harmful)</span> <a href="harmful.html#field_injection">Why?</a></i></th>
	<td class="fair">yes <i class="harmful">annotations</i></td>	
	<td class="fair"><b>idiomatic</b> <i class="harmful">annotations</i></td>
	<td class="fair">yes <i>named or <span class="harmful">annotations</span></i></td>
	<td class="good"><b>no</b></td>
</tr>
<tr>
	<th>Setter injection <i><span class="harmful">(considered harmful)</span> <a href="harmful.html#setter_injection">Why?</a></i></th>
	<td class="fair"><b>idiomatic</b><i>named, annotations or XML</i></td>	
	<td class="fair">yes <i class="harmful">annotations</i></td>
	<td class="fair">yes <i>name or type</i></td>
	<td class="good"><b>no</b></td>
</tr>
<tr>
	<th>Factory methods</th>
	<td class="fair">yes <i class="harmful">named XML, annotations<i></td>	
	<td class="fair">yes <i class="harmful">annotations</i></td>
	<td class="good">yes <i>convention, programmatic</i></td>
	<td class="good"><b>yes</b> <i>fluent interface<i></td>
</tr>
<tr>
	<th>Static injection<i><span class="harmful">(considered harmful)</span> <a href="harmful.html#static_injection">Why?</a></i></th>
	<td class="fair">yes <i class="harmful">named XML + annotations</i></td>	
	<td class="fair">yes <i class="harmful">annotations</i></td>
	<td class="good">no?</td>
	<td class="good"><b>no</b></td>
</tr>
<tr>
	<th>Method interception<i>technique</i></th>
	<td class="bad">yes <i class="harmful">aspects<i></td>	
	<td class="bad">yes <i class="harmful">aspects<i></td>
	<td class="fair">yes <i>programmatic<i></td>
	<td class="fair"><a href="/userguide/services.html">services</a> <i>programmatic<i></td>
</tr>
<tr>
	<th>Providers <i>(lazy indirection)</i></th>
	<td class="fair">yes</td>	
	<td class="fair"><b>idiomatic</b> <i class="harmful">annotations</i></td>
	<td class="good">no</td>
	<td class="good"><b>optional</b> <i>bridge via fluent interface</i></td>
</tr>
<tr>
	<th>Reinection<i><span class="harmful">(considered harmful)</span> <a href="harmful.html#reinjection">Why?</a></i></th>
	<td class="bad">yes <i class="harmful">setters</i></td>	
	<td class="good"><b>no</b> <i>use indirection</i></td>
	<td class="fair">explicit call-side <i>programmatic</i></td>
	<td class="good"><b>no</b> <i>use indirection</i></td>
</tr>
<tr>
	<th>Optional injection<i class="harmful">(considered harmful)</i></th>
	<td class="good">no?</td>	
	<td class="bad">yes <i class="harmful">annotations</i></td>
	<td class="good">no?</td>
	<td class="good"><b>no</b></td>
</tr>
<tr>
	<th>Mixed injection<i>(assisted injection)</i></th>
	<td>no?</td>	
	<td>yes <i class="harmful">annotations</i></td>
	<td>no?</td>
	<td><a href="/userguide/services.html">services</a></td>
</tr>
<tr>
	<th>Post-construction hook</th>
	<td>yes <i class="harmful">annotations?</i></td>	
	<td>yes <i class="harmful">annotations?</i></td>
	<td>yes<i>(lifecycles)</i></td>
	<td>through custom <code>Supplier</code> only</td>
</tr>
</table>


## Modularity
<div><a class="highlight-toggle" onclick="javascript:$('table.compare').toggleClass('highlighted');"><span class="icon-th"></span> Highlight on/off</a></div>
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Arrays</th>
	<td class="bad">no</td>
	<td class="bad">no</td>
	<td class="good"><b>idiomatic</b><i>implicit available</i></td>
	<td class="good"><b>idiomatic</b><i>implicit available</i></td>
</tr>
<tr>
	<th>Collections <i>List/Sets/Maps<i></th>
	<td class="bad"><code>java.util</code><i>explicit in XML</i></td>	
	<td class="fair"><code>java.util</code> <i>(via plugin-in)</i><i>fluent interface</i></td>
	<td class="fair"><code>java.util</code><i>explicit programmatic</i></td>
	<td class="good"><b>any</b>(via bridge)<i>fluent interface</i></td>
</tr>
<tr>
	<th>Multibinds</th>
	<td class="bad">manual, explicit <i>XML tags</i></td>	
	<td class="fair">manual, explicit,<br/> loose coupled <i>fluent interface</i></td>
	<td class="bad">manual, explicit <i>programmatic</i></td>
	<td class="good"><b>autom., implicit</b>,<br/> loose coupled <i>fluent interface</i></td>
</tr>
<tr>
	<th>Sequence of declarations</th> 
	<td class="bad">crucial</td>	
	<td class="bad">crucial</td>
	<td class="bad">crucial</td>
	<td class="good"><b>irrelevant</b></td>
</tr>
</table>


## Variants (Module Combinability)
<div><a class="highlight-toggle" onclick="javascript:$('table.compare').toggleClass('highlighted');"><span class="icon-th"></span> Highlight on/off</a></div>
A _variant_ should be understand as a slightly different _form_ of the _module structure_, a permutation of the application configuration/composition.
How can those be build, viewed, understand, and combined with other _variants_ ?
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Creation technique<i>Usage</i></th>
	<td class="bad">overrides<i class="harmful">XML files</i><i>annotated config classes</i></td>
	<td class="bad">overrides<i>Module classes</i></td>
	<td>?</td>
	<td class="good">install/uninstall<i>fluent interface</i></td>
</tr>
<tr>
	<th>Perspective</th> 
	<td class="bad">combined</td>	
	<td class="bad">combined</td>
	<td>?</td>
	<td class="good"><b>isolated</b></td>
</tr>
<tr>
	<th>Composition</th> 
	<td class="bad">not combinable</td>	
	<td class="bad">not combinable</td>
	<td>?</td>
	<td class="good"><b>combinable</b></td>
</tr>
</table>


## Applicability Restrictiveness
<div><a class="highlight-toggle" onclick="javascript:$('table.compare').toggleClass('highlighted');"><span class="icon-th"></span> Highlight on/off</a></div>
In what ways can _binds_ be limited, how exact or _narrow_ are they ?
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Principle</th>
	<td class="bad">local per file</td>
	<td class="bad">local modules</td>
	<td class="fair">programmatic filters<i class="harmful">per case</i></td>
	<td class="good">type hierarchy<br/>+ package-sets</br>+ injection hierarchy</td>
</tr>
<tr>
	<th>Specific package (set)<i>(with or without sub-packages)</i></th>
	<td class="bad">no</td>
	<td class="bad">no</td>
	<td class="fair">possible<i>not build in</i></td>
	<td class="good"><a href="/userguide/binds.html#targeting">yes</a><i>fluent-interface</i></td>
</tr>
<tr>
	<th>Specific class<i>also with wild-card types</i></th>
	<td class="fair">nearly</td>
	<td class="good">yes<i>explicit via literal</i></td>
	<td class="good">yes<i>gems filter util</i></td>
	<td class="good"><a href="/userguide/binds.html#targeting">yes</a><i>fluent-interface</i></td>
</tr>
<tr>
	<th>Specific interface<i>incl. all implementations</i></th>
	<td class="bad">no</td>
	<td class="bad">no</td>
	<td class="good">yes<i>gems filter util</i></td>
	<td class="good"><a href="/userguide/binds.html#targeting">yes</a><i>fluent-interface</i></td>
</tr>
<tr>
	<th>Specific parent instance<i>also specific grandparents</i></th>
	<td class="bad">no</td>
	<td class="bad">no</td>
	<td class="bad">no</td>
	<td class="good"><a href="/userguide/binds.html#targeting">yes</a><i>fluent-interface</i></td>
</tr>
</table>


## Scopes
<div><a class="highlight-toggle" onclick="javascript:$('table.compare').toggleClass('highlighted');"><span class="icon-th"></span> Highlight on/off</a></div>
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Default scope</th>
	<td>singleton</td>
	<td>injection</td>
	<td>singleton</td>
	<td>singleton</td>
</tr>
<tr>
	<th>Custom scopes<i>difficulty</i></th>
	<td class="bad">advanced</td>
	<td class="bad">advanced</td>
	<td class="fair">medium<i>(life-cycles)<i/></td>
	<td class="good">simple</td>
</tr>
<tr>
	<th>Available scopes</th>
	<td class="fair">singleton, injection, request, session</td>
	<td class="fair">singleton, injection <i>request, session via plug-in</i></td>
	<td class="good">programmatic life-cycles</td>
	<td class="good">singleton, injection, per dependency type, per receiver type, ...</td>
</tr>
</table>


## Performance
<div><a class="highlight-toggle" onclick="javascript:$('table.compare').toggleClass('highlighted');"><span class="icon-th"></span> Highlight on/off</a></div>
There seamed to be no actual numbers on performance. I had no time to do proper tests including all 4 frameworks yet but I would like to get these numbers if available somewhere or by myself if needed. Until than I can just give indications based on older measurements I found and personal experience gained in years of usage (not pico-container) or expectation (pico). 

<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Bootstrapping</th>
	<td class="fair"><em>slower</em> than guice</td>
	<td class="good">stated that it is <em>fast</em></td>
	<td class="good">about as guice?</td>
	<td class="good">about as guice?</td>
</tr>
<tr>
	<th>Object Creation</th>
	<td class="fair">stated that it is <em>slower</em> than guice</td>
	<td class="good">stated that it is <em>fast</em></td>
	<td class="good">about as guice?</td>
	<td class="good">about as guice?</td>
</tr>
</table>


## Error Behaviour
<div><a class="highlight-toggle" onclick="javascript:$('table.compare').toggleClass('highlighted');"><span class="icon-th"></span> Highlight on/off</a></div>
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Dependency cycles</th>
	<td class="fair">viable <i><span class="harmful">setter</span> only</i></td>
	<td class="bad">viable <i class="harmful">proxies</i></td>
	<td class="fair">viable <i>for interface</i></td>
	<td class="good"><b>illegal</b></td>
</tr>
<tr>
	<th>Detection of a cyclic dependencies error</th>
	<td class="fair">runtime<i>(bootstrapping)</i></td>
	<td class="good">-</td>
	<td>?</td>
	<td class="fair">runtime<i>(bootstrapping)</i></td>
</tr>
<tr>
	<th>Scope faults<i>(e.g. injecting request scoped into a singleton)</th>
	<td>?</td>
	<td class="bad">not detected</td>
	<td class="good">not possible<i>(self managing objects)</i></td>
	<td class="fair">at injection</td>
</tr>
<tr>
	<th>Ambiguous binding error</th>
	<td class="good">eager</td>
	<td class="fair">lazy</td>
	<td class="fair">lazy</td>
	<td class="good">eager</td>
</tr>
</table>

## Summing Up
<table class="compare">
<tr>
	<th></th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Best to use for</th>
	<td>...</td>
	<td>...</td>
	<td>...</td>
	<td>...</td>
</tr>
</table>

## Noteworthy

- With Silk **all binds** are defined by just using the fluent `Binder` interface within `Bundle` and `Module` classes. There are no exceptions to this, no _overrides_ or annonymous classes needed, no error prone visiblity _exports_ or similar hard to maintain techniques. 


