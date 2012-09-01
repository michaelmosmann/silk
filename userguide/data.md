---
layout : default
title : Data
---
# Data

<table class='toc'>
	<tr><th>#<a href="#Name">Name</a></th><td>To name <code>Instance</code>s so that they can be distinguished.</td></tr>
	<tr><th>#<a href="#Type">Type</a></th><td>A generic aware <code>Class</code> descriptor.</td></tr>
	<tr><th>#<a href="#Instance">Instance</a></th><td><code>Name</code> and <code>Type</code> of an instance.</td></tr>
	<tr><th>#<a href="#Resource">Resource</a></th><td>An <code>Instance</code> and its <code>Target</code> of use.</td></tr>
	<tr><th>#<a href="#Dependency">Dependency</a></th><td>What is needed (to inject a single instance)</td></tr>
	<tr><th>#<a href="#Injection">Injection</a></th><td>What has been injected (as parents).</td></tr>
	<tr><th>#<a href="#Demand">Demand</a></th><td>What should be served by a <code>Repository</code></td></tr>
	<tr><th>#<a href="#Emergence">Emergence</a></th><td>An <code>Instance</code>'s durability (or <code>Expiry</code>).</td></tr>
	<tr><th>#<a href="#Expiry">Expiry</a></th><td>How frequent an instance expires and another one is created.</td></tr>
	<tr><th>#<a href="#Target">Target</a></th><td>Parent <code>Instance</code> and the <cpde>Packages</code>.</td></tr>
	<tr><th>#<a href="#Packages">Packages</a></th><td>In which packages something can be used.</td></tr>
	<tr><th>#<a href="#Source">Source</a></th><td>Where a <code>Resource</code> came from.</td></tr>
	<tr><th>#<a href="#Suppliable">Suppliable</a></th><td>Something that can be supplied (become an <code>Injectron</code>)</td></tr>
</table>

##Concept
Silk uses a lot of immutable value objects that are used to describe what we have or are looking for. 
Most of them wrap primitives or combine 2 other value objects into one.

## <a id="Name"></a>Names
A name is basically a `String` with some constraints applied to it. 
Names are used to distinguish between different instances of the same type. 
Instances of different types do not need a name to be different! 
 

### Format
- **Case insensitive** 

	All names are matched without paying attention to upper or lower case. 
- **Letters and Digits**

	A name should consist of letters and digits. Whitespace between can be used. Leading and tailing whitespace will be trimmed. 
	Names should not contain any characters that have a special meaning to regular expressions!

### Wildcard-Names
The star character `*` can be used to as wildcard. A wildcard is equal to the regular expression `.*` so it will match none or any sequence of any character.
To create Wildcard-Names the factory method `prefix` can be used.

### The `DEFAULT`-Name
In the most cases it is not needed to explicitly name instances since there will just be one of the `Type`. 
Those instances get the `DEFAULT` name what is the empty `String` `""`. 
So technically all instances are `named` instances but most of them use the `DEFAULT` `Name`.

### The `ANY`-Name 
There is a special constant `Name` that will match all names hence it is just a wildcard `*`. This is
sometimes used as the required `Name` of a `Dependency` to indicate that we are satisfied with whatever `named` instance as long as the `Type` matches.

In such cases it is guaranteed that the instanced injected will be the `DEFAULT` instance as long as it is bond such an instance.
Of cause there can be just one or more explicitly `named` instances. In such case the most precise one not considering the name will be used.  

## <a id="Type"></a>Types
A is a generic variant of a `Class`. Hence it has type parameters that themselves are generic `Type`s. Types are used to describe 

## <a id="Instance"></a>Instances
## <a id="Resource"></a>Resources
## <a id="Dependency"></a>Dependencies
## <a id="Injection"></a>Injections
## <a id="Demand"></a>Demands
## <a id="Emergence"></a>Emergences
## <a id="Expiry"></a>Expiries
## <a id="Target"></a>Targets
## <a id="Packages"></a>Packages
## <a id="Source"></a>Source
## <a id="Suppliable"></a>Suppliable

 <a class='next' href="scopes.html">Continue with Scopes --&gt;</a>