---
layout : doc
title : Philosophy
---

# Philosophy

<abstract>
How should a DI container integrate and impact the development process and product of an application ?
</abstract>

The philosophy in Silk is that you develop the application as if there is no DI container. 
Because in the ideal application you don't need one. An ideal application has a design that allows
to wire manually without creating a mess. It has no need for _scopes_ or massive setup code. The 
ideal application is _simple_ enough to avoid the complexity of a DI container altogther with the
task that it fulfills. 

In reality that ideal application barely exists. Especially with the growing number of team members 
and project size the ability to simplify early and often on a gloabl level shrinks more and more.
We try to counter this effect by decouple and modularize code fragments as good as we can. At some
point manual setup code grows out a manageable length and complexity. The procedural paradigm does 
not match very well to the typical character of wiring code. At this point we have two options:

1. Simplifying the code as long as needed so that it becomes manageable again
2. Introduce a DI container

The 1. options is rarly seen as viable, as the work of many developers would be somewhat _on hold_.
In other cases upcoming features already indicate that a high flexibility is needed on the application 
composition level and it is intentionally decided to use a DI container to take care of the inherent
complexity. 

Still - in the long run - the goal should be to get rid of the complexity. That means to commit as
little as possible to _solutions_ that are not available without the DI container in place.    
As a consequence the DI container should have 2 important chracteristics:

1. It should allow to achieve the same result as manual wiring can.  
2. It should allow to switch between using the DI container and wire _manually_ at any time on a 
   small fragment level so that just those parts that solved better with the container need to use it.
   That includes that the container should _drive_ a style of coding that is as if there is no 
   container and that more and more fragments can smothly regain manual setup code.
   
...to be continued



 