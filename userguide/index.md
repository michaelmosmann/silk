---
layout : tour
title: User Guide
---

# User Guide
<abstract>
To get a good overview about Silk you should start reading the <a href="intro.html">introduction</a>.
In case you know the _google guice_ DI framework a lot of the concepts should be quite familiar for you. 
You could directly start reading about the different <a href="binds.html">bindings</a>. 
Notice that Silk has a more advanced modularization concepts so you should have a look to the <a href="modularity.html">modularity page</a> as well. 
</abstract>

## Tour
<a href="/userguide/install.html" class="book list c-docs"><span class="icon-save"></span><em>1.</em> Installation</a>
How to get and use the Silk DI container.

<hr/>
<a href="/userguide/intro.html" class="book list c-docs"><span class="icon-lightbulb"></span><em>2.</em> Intro-duction</a>
A (not any longer that) briefly introduction into all concepts of the Silk DI container. 
This outlines all parts in short that subsequently get a seperate page each.

<hr/>
<a href="/userguide/binds.html" class="book list c-docs"><span class="icon-random"></span><em>3.</em> Bindings</a>
The different types of bindings discussed in detail. 
This documents the most important part of the API as it will be used from a usual user perspective.

<hr/>
<a href="/userguide/scopes.html" class="book list c-docs"><span class="icon-eye-open"></span><em>4.</em> Scopes</a>
Scopes in Silk are very flexible. Learn about the scopes existing and how custom scopes can be done. 

<hr/>
<a href="/userguide/modularity.html" class="book list c-docs"><span class="icon-th-large"></span><em>6.</em> Modularity</a>
Silk is designed to scale from tiny to very large. A application configuration is described on 2 levels. 
Modules describe the bindings, bundles describe a composition of modules and other bundles.
The special characteristic of Silk is that all conditions kept outside of these bundles and modules. 
They become input arguments to the bootstrapping process.

<hr/>
<a href="/userguide/services.html" class="book list c-docs"><span class="icon-briefcase"></span><em>5.</em> Services</a>
Silk's service concept comes close to what is sometimes called _micro-services_. 

<hr/>
<a href="/userguide/data.html" class="book list c-docs"><span class="icon-exchange"></span><em>7.</em> Data<br/>Types</a>
Another unique feature of Silk is its general immutability. The functional parts are connected with immutable data types.
Beside them any injection context (injector) is immutable as well after it has been bootstrapped. Thereby Silk
becomes a nearly functional behaviour. The state manages by scopes is the only exception to this. 

<hr/>
<a class="next list" href="snippets.html"><span class="icon-reorder"></span> Show me some code...</a>
Some prefer to just see some code. Here you go...
