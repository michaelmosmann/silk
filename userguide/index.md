---
layout : userguide
title: User Guide
---

# User Guide
To get a good overview about Silk you should start reading the <a href="intro.html">introduction</a>.
In case you know the _google guice_ DI framework a lot of the concepts should be quite familiar for you. You could directly start reading about the different <a href="binds.html">bindings</a>. 
Notice that Silk has a more advanced modularization concepts so you should have a look to the <a href="modularity.html">modularity page</a> as well. 

## Tour
<a href="/userguide/install.html" class="book list c-docs"><span>1.</span> Installation</a>
How to get and use the Silk framework.

<hr/>
<a href="/userguide/intro.html" class="book list c-docs"><span>2.</span> Intro-duction</a>
A briefly introduction into all concepts of the Silk framework. 

<hr/>
<a href="/userguide/binds.html" class="book list c-docs"><span>3.</span> Bindings</a>
The different types of bindings discussed in detail.

<hr/>
<a href="/userguide/scopes.html" class="book list c-docs"><span>4.</span> Scopes</a>
Scopes in Silk are very flexible. Learn about the scopes existing and how custom scopes can be done. 

<hr/>
<a href="/userguide/services.html" class="book list c-docs"><span>5.</span> Services</a>
Silk's service concept comes close to what is sometimes called _micro-services_. They allow to turn 
a usual java method into a first class service object that use a general interface. This decouples
implementation and usage. In contrast to most equivalent approaches Silk's services do not require
to explicitly wire them. Wiring is automatic and refactoring safe since method names and parameter 
order are irrelevant. The wiring is determined from types.

<hr/>
<a href="/userguide/modularity.html" class="book list c-docs"><span>6.</span> Modularity</a>

<hr/>
<a href="/userguide/data.html" class="book list c-docs"><span>7.</span> Data<br/>Types</a>

<hr/>
<a class="next list" href="snippets.html"><span class="icon-reorder"></span> Show me some code...</a>
