---
layout : doc
title : Motivation
---
# Comparison

<abstract>
In the below tables the features of 4 different frameworks are compared with each other. The features are judged by their influence of the application and development process as seen by the author of Silk. A good influence is indicated green, a fair one yellow and a bad one red. Neutral information is kept gray.
</abstract>

## Concept
<table class="compare">
<tr>
	<th>Feature</th><th><a href="http://www.springsource.org/">Spring</a></th><th><a href="http://code.google.com/p/google-guice/">Guice</a></th><th><a href="http://picocontainer.com/">pico-container</a></th><th>Silk</th>
</tr>
<tr>
	<th>Container model</th> 
	<td class="bad">flat instances</td>
	<td class="bad">flat instances</td> 
	<td class="fair">hierarchical instances <i>explicit modelled<i></td> 
	<td class="good">hierarchical instances <i>"production rules"</i></td>
</tr>
<tr>
	<th>Configuration style</th> 
	<td class="fair">static <i><b>XML</b>, annotation</i></td> 
	<td class="good">static + programmatic <i><b>annotation</b> + <nobr>fluent interface</nobr></i></td>
	<td class="fair">static + programmatic <i><b>composition</b> + annotation</i></td>
	<td class="good">programmatic <i><b>fluent interface</b></i></td>
</tr>
<tr>
	<th>Wiring style</th>
	<td class="bad">automatic <i>by name <b>or</b> type</i></td>	
	<td class="fair">explicit <i>by name <b>and</b> type</i></td>
	<td class="fair">explicit <i>by name <b>and</b> type</td>
	<td class="good">explicit <i>name + type + hierarchy<i/></td>
</tr>
<tr>
	<th>Wiring Independence</th> 
	<td class="fair">yes <i>with XML only!</i></td>	
	<td class="bad">no <i>annotations required</i></td>
	<td class="fair">yes <i>(not annotation features)</i></td>
	<td class="good"><b>yes</b> <i>pure fluent interface</i></td>
</tr>
<tr>
	<th><i>"External"</i> code handling</th>
	<td class="fair">limited <i>(name/type wiring only)</i></td>	
	<td class="fair">indirect <i>requires <tt>Provider</tt></i></td>
	<td class="fair">limited <i>(not all features)</i></td>
	<td class="good"><b>as own</b><i>uniform</i></td>
</tr>
<tr>
	<th>Coupling style</th> 
	<td class="fair">referenceable</td>	
	<td class="good">loose</td>
	<td class="fair">referenceable</td>
	<td class="good">loose</td>
</tr>
</table>

## Generics
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>generics support</th>
	<td class="bad">no</td>
	<td class="fair">yes <i>each a literal class!</i></td>
	<td class="bad">no</td>
	<td class="good"><b>yes</b> <i><code>Type</code> instance</i></td>
</tr>
<tr>
	<th>generic type safty</th>
	<td>-</td>	
	<td class="good">compile time</td>
	<td>-</td>
	<td class="fair">largely compile time<i>full at runtime</i></td>
</tr>
<tr>
	<th>wildcard generics</th> 
	<td>-</td>	
	<td class="bad">no</td>
	<td>-</td>
	<td class="good">yes</td>
</tr>
</table>


## Injection
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Annotation guidance</th>
	<td class="fair">required <i>XML alternative</i></td>	
	<td class="bad">required</td>
	<td class="good">less important alternative</td>
	<td class="good"><b>no need<b></td>
</tr>
<tr>
	<th>Constructor injection</th>
	<td class="fair">yes <i>annotactions or XML<i></td>	
	<td class="bad">yes <i>requires annotations</i></td>
	<td class="good"><b>idiomatic</b> <i>automatic: most to few args</i></td>
	<td class="good"><b>idiomatic</b> <i>fluent interface</i><i>many options<i></td>
</tr>
<tr>
	<th>Field injection<i>(considered harmful)</i></th>
	<td class="fair">yes <i>annotations</i></td>	
	<td class="fair"><b>idiomatic</b> <i>annotations</i></td>
	<td class="fair">yes <i>named or annotations</i></td>
	<td class="good"><b>no</b></td>
</tr>
<tr>
	<th>Setter injection<i>(considered harmful)</i></th>
	<td class="fair"><b>idiomatic</b><i>named, annotations or XML</i></td>	
	<td class="fair">yes <i>annotations</i></td>
	<td class="fair">yes <i>name or type</i></td>
	<td class="good"><b>no</b></td>
</tr>
<tr>
	<th>Factory methods</th>
	<td class="fair">yes <i>named XML, annotations<i></td>	
	<td class="fair">yes <i>annotations</i></td>
	<td class="good">yes <i>convention, programmatic</i></td>
	<td class="good">yes <i>fluent interface<i></td>
</tr>
<tr>
	<th>Static injection<i>(considered harmful)</i></th>
	<td class="fair">yes <i>named XML + annotations</i></td>	
	<td class="fair">yes <i>annotations</i></td>
	<td class="good">no?</td>
	<td class="good"><b>no</b></td>
</tr>
<tr>
	<th>Method interception<i>technique</i></th>
	<td class="bad">yes <i>aspects<i></td>	
	<td class="bad">yes <i>aspects<i></td>
	<td class="good">yes <i>programmatic<i></td>
	<td class="good">services <i>programmatic<i></td>
</tr>
<tr>
	<th>Providers <i>(lazy indirection)</i></th>
	<td class="fair">yes</td>	
	<td class="fair"><b>idiomatic</b> <i>annotation</i></td>
	<td class="good">no</td>
	<td class="good">optional <i>bridge via fluent interface</i></td>
</tr>
<tr>
	<th>Reinection<i>(considered harmful)</i></th>
	<td class="bad">yes <i>setters</i></td>	
	<td class="good">no <i>use <tt>Provider</tt>s</i></td>
	<td class="fair">explicit call-side <i>programmatic</i></td>
	<td class="good"><b>no</b></td>
</tr>
<tr>
	<th>Optional injection<i>(considered harmful)</i></th>
	<td class="good">no?</td>	
	<td class="bad">yes <i>annotations</i></td>
	<td class="good">no?</td>
	<td class="good"><b>no</b></td>
</tr>
</table>

## Modularity
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Collections <i>List/Sets/Maps<i></th>
	<td class="bad">explicit <code>java.util</code><i> XML</i></td>	
	<td class="fair">plugin-in <code>java.util</code><i> fluent interface</i></td>
	<td>?</td>
	<td class="good"><b>any</b>(via bridge)<i>fluent interface</i></td>
</tr>
<tr>
	<th>Multibinds</th>
	<td class="bad">manual, explicit <i>XML tags</i></td>	
	<td class="fair">semi-automatic, explicit, loose coupled <i>fluent interface</i></td>
	<td class="bad">manual, explicit <i>programmatic</i></td>
	<td class="good">automatic, loose coupled <i>fluent interface</i></td>
</tr>
<tr>
	<th>Re-configuration<i>perspective</i><i>composition</i><i>usage</i></th>
	<td class="bad">overrides<i>combined</i><i>not combinable</i><i>XML files</i></td>
	<td class="bad">overrides<i>combined</i><i>not combinable</i><i>Modules</i></td>
	<td>?</td>
	<td class="good">install/uninstall<i>isolated</i><i>combinable</i><i>fluent interface</i></td>
</tr>
</table>


## Scopes
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Default scope</th>
	<td class="good">singleton</td>
	<td class="fair">injection</td>
	<td class="good">singleton</td>
	<td class="good">singleton</td>
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

## Error Behaviour
<table class="compare">
<tr>
	<th>Feature</th><th>Spring</th><th>Guice</th><th>pico-container</th><th>Silk</th>
</tr>
<tr>
	<th>Dependency cycles</th>
	<td class="fair">yes <i>setter only</i></td>
	<td class="bad">yes <i>proxies</i></td>
	<td class="good">no?</td>
	<td class="good">no</td>
</tr>
<tr>
	<th>Cyclic dependencies error</th>
	<td>runtime</td>
	<td>-</td>
	<td>?</td>
	<td>runtime</td>
</tr>
</table>
