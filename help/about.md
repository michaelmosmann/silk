---
layout : doc
title : About
categories : [general]
---
# About

<abstract>
The name _Silk_ is chosen because its strings are elegant, lightweight, thin and transparent but still reliable and robust. 
So silk as a material matches what we are looking for when connecting software components like we do with DI.
</abstract>

## Me <small style="font-weight:normal;">(the author)</small>
I am a software developer living in Växjö, Sweden. I coded the Java, did the website, logo and everything else connected to the project during my spare time in the last couple of month.

## This Website
This site is hosted on <i class="fa fa-github"></i>[githup pages](http://pages.github.com/). It uses [Jekyll](http://jekyllbootstrap.com/) that is building static HTML pages from [markdown](http://daringfireball.net/projects/markdown/) files on every [git](http://git-scm.com/) push done. All <i class="fa fa-eye"></i> icons are inserted using the [Font-Awesome](http://fortawesome.github.io/Font-Awesome/) font.

### Javadoc Autolinking
A lot of Java type names are linked to the javadoc of that type (example: `Binder`). Those links are not contained in the page source. There they are a `<code></code>` block created by the normal markup for inline-code.
The below [jQuery](http://jquery.com/) script fetches a JSON description of known types created by a Java-Doclet and transforms the blocks into the links you see.

The full script can be viewed [here](/assets/js/autolink.js).
