---
layout : default
title : Silk Sandbox
---
# Sandbox

Here is some Java code:

{% highlight java %}
static class AutobindBindsModule
		extends BinderModule {

	@Override
	protected void declare() {
		autobind( Integer.class ).to( 42 );
	}

}
{% endhighlight %}