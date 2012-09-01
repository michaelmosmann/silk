---
layout : default
title : About
categories : [general]
---
# About

## The Silk Framework
When I tried described to a non programmer what I do in my spare time I came up with something like:

**it's the lines between the dots...**

and that's pretty much it. The name Silk is chosen because this lines are elegant, lightweight, thin and transparent but still reliable and robust. So silk as a material matches what we are looking for when connecting software components like we do with DI.

## The Author  
It is a one man show. I coded all the Java and did the website, logo and everything else connected to the project during my spare time in the last couple of month.

## This Website
This site is hosted on <a href="http://pages.github.com/">githup pages</a>. It uses <a href="http://jekyllbootstrap.com/">Jekyll</a> that is building static HTML pages on every commit.
For development a local Jekyll is used. Here is the `_config.yaml`:

	source:      .
	destination: ./_site
	auto:        true
	server:      true
	baseurl:     /silk/
	markdown:    rdiscount
	pygments:    true

### Javadoc Autolinking
A lot of Java type names are linked to the javadoc of that type (example: `Binder`). Those links are not contained in the page source. There they are a `<code></code>` block created by the normal markup for inline-code.
The below <a href="http://jquery.com/">jQuery</a> script fetches a JSON description of known types created by a Java-Doclet and transforms the blocks into the links you see.

{% highlight javascript %}
var typeURLs = [];
$(document).ready(function() {
	$.getJSON('/silk/assets/javadoc/types.json', function(data) {
    		$.each(data['types'], function(index, value) { 
			if (value != null) {
    				typeURLs[value.name]={type: value.type,url: "/silk/assets/javadoc/"
    					+value['package'].replace(/\./g, '/')+"/"+value.name+".html"};
			}
		});
		$('code').wrap(function() { 
			var type = typeURLs[$(this).text()];
			if (type) { return "<a href='"+type.url+"' class='javadoc "+type.type+"'></a>" } else { return ""}
		});
	});
});
{% endhighlight %}
