---
layout : doc
title : Highlights
---

# Highlights

### Application code stays independent 
Silk absolutely prevents your application code from getting dependent on the DI-framework and 
vigorously encourages the use of constructor injection so that your code works just as well autonomously. 
As a result an application design is driven that can be tested nicely selfcontained by unit tests without the complexity of a DI framework.

### Pure Java
Silks is a small flexible core that is easy to utilise and extend. 
It helps to decouple your application with elegance by providing a well-considered handful of 
techniques to modularise and configure your programs without conditional declarations with solely pure Java code. 

### Sequence of declaration is irrelevant
Together with Silk's immutability the absence of conditions brings a functional nature to Silk wherein the sequence of declarations becomes irrelevant!
 This is a huge simplification when considering the composition of an application. 

### Says NO to the pain of *magic*
Most of all Silk aims to allow easy long term development that comes with no surprises so it vehemently avoids any kind of _\*magic\*_ and guides you with helpful error messages.
Above all it allows to consider small components separately whereby even large, highly configurable systems become manageable without pain. 

### Lightweight
The absence of any further runtime dependencies and its slim footprint of less than 120K makes Silk a fine 
choice for small projects although at heart Silk is especially designed to scale in the large.

### No dependency cycles by concept + cross-functional programming
Through its revolutionary <a href="userguide/services.html">service</a> concept Silk decouples the application and prevents dependency cycles by concept. 
Further it allows to tackle cross-functional concerns with pure Java what avoids the complexity of conventional meta programming approaches.

### Injectrons
Silk invented the `Injectron`, a _first class_ access object abstraction of a _singleton's_ `Resource` within an `Injector` context. 
This implies they can be injected like the _resources_ themselves what allows to pre-resolve dependencies within extensions (on top of the core) like the service concept.  

