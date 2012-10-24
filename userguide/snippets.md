---
layout : userguide
title : Show me some code!
---
# Show me some code!

When you are familiar with DI as a concept and the _google guice_ library as an example the following code snippets will give you a good idea how Silk is used.

**Note:** Code is shown together but doesn't appear in a single source in a real implementation! 
Also `injector.resolve` is used to show what would be injected but of cause this call is not done manually except for the root instance of an application.

{% highlight java %}

// define a module
class RootModule extends BinderModule { 
	@Override
	protected void declare() {
		bind(int.class).to(42); // the answer is 42	
	}
}

// bootstrap a injector
Injector injector = Bootstrap.injector(RootModule.class);

// get the answer
int answer = injector.resolve( Dependency.dependency(int.class) ); 
// = 42

// or as wrapper
Integer answer = injector.resolve( Dependency.dependency(Integer.class) ); 
// = 42

// or as array 
Integer[] answers = injector.resolve( Dependency.dependency(Integer[].class) ); 
// = [ 42 ]

// together with another module...
class OtherModule extends BinderModule {
	@Override
	protected void declare() {
		bind(named("lucky number"), int.class).to(13);	
	}
}

// ...the array would look like:
Integer[] answers = injector.resolve( Dependency.dependency(Integer[].class) ); 
// = [ 42, 13 ]

// but how do we bootstrap 2 modules ? we use a bundle to connect them and bootstrap this
class RootBundle extends BootstrapperBundle {
	@Override
	protected void bootstrap() {
		install(RootModule.class);
		install(OtherModule.class);
	}
}
Injector injector = Bootstrap.injector(RootBundle.class); // the root is our new bundle

// like List's more than array ? install them:
protected void bootstrap() {
	install(RootModule.class);
	install(OtherModule.class);
	install(BuildinBundle.LIST);
}
List<Integer> answers = injector.resolve( Dependency.dependency(Typecast.listTypeOf(Integer.class)) ); 
// = [ 42, 13 ]

// or Set's ?
protected void bootstrap() {
	//...
	install(BuildinBundle.SET);
}
Set<Integer> answers = injector.resolve( Dependency.dependency(Typecast.setTypeOf(Integer.class)) ); 
// = { 42, 13 }

// some believe 7 is the lucky number - why not:
protected void declare() {
	inPackageOf(Some.class).bind(named("lucky number"), int.class).to(7);
	// and/or
	injectingInto(Some.class).bind(named("lucky number"), int.class).to(7);
}

// you think 6 is the lucky number while your team has a different opinion ? Use your own root
class MyRootBundle extends BootstrapperBundle {
	@Override
	protected void bootstrap() {
		install(RootBundle.class);  // get what all get
		uninstall(OtherModule.class); // but not the lucky number 13
		install(MyOtherModule.class); // instead it should be 6
	}
}
class MyOtherModule extends BinderModule {
	@Override
	protected void declare() {
		bind(named("lucky number"), int.class).to(6); // YES! 6!
		//... alternatives for other you want to customize...
	}
}
Injector injector = Bootstrap.injector(MyRootBundle.class); // now YOUR root bundle

//

{% endhighlight %}