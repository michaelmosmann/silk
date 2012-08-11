# Sandbox

Here is some Java code:
```
static class AutobindBindsModule
		extends BinderModule {

	@Override
	protected void declare() {
		autobind( Integer.class ).to( 42 );
	}

}
```