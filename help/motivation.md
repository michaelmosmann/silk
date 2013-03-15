---
layout : doc
title : Motivation
---
# Motivation

## Why DI / IoC ?
There are enough articles out there (most famous one is <a href="http://martinfowler.com/articles/injection.html">the one of Martin Fowler</a>) explaining the benefits of dependency injection as a tool to 
achieve inversion of control and why this can be important. 

Let me just remind you that everything has drawbacks too. 
Before using a DI framework like Silk you should be sure, that the benefits outbalance the drawbacks. 
In my opinion a DI tool just makes sense if the application requires a high flexibility in module 
composition and other configurations in the stage of program assembling. 
Than the wiring glue code gets out of hand when just doing a manual approach.  

## Why another DI / IoC framework ?
Dependency injection has been around for quite awhile now. For Java there are a few big or mature 
players and a vast number of smaller or younger ones. **Still none of them is satisfactory.** 
Have a look at the <a href="comparison.html">comparison page</a> to see what is wrong with the existing alternatives.

I will explain this is detail as soon as I find time. Mostly I feel that if one takes the decision 
to put the glue code in the hands of a DI tool this tool should just do that with as little impact
as possible on the _usual_ development as it would be without it.

