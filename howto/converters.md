---
layout : default
title: Converters using Services
---

# Converters using Services

Most applications deal with character wise input that has to be converted to typed values.

{% highlight java %}
interface Converter<T> {
	
	T convert(String input);
}
{% endhighlight %}

`ServiceMethod`s can be used to implement each converter with a single method.

{% highlight java %}
class Converters {
	
	Date parseDate(String input) { ... }
	
	CivicNo parseCivicNo(String input) { ... }
}
{% endhighlight %}

Allows:

{% highlight java %}
Converter<Date>
Converter<CivicNo>	
}
{% endhighlight %}

