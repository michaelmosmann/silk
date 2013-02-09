/*
 *  Copyright (c) 2012, Jan Bernitt 
 *			
 *  Licensed under the Apache License, Version 2.0, http://www.apache.org/licenses/LICENSE-2.0
 */
package se.jbee.inject.bind;

import se.jbee.inject.Scope;
import se.jbee.inject.bind.Binder.ScopedBinder;
import se.jbee.inject.util.Scoped;

/**
 * The default utility {@link Module} almost always used.
 * 
 * @author Jan Bernitt (jan@jbee.se)
 */
public abstract class BinderModule
		extends AbstractBinderModule
		implements Bundle, Module {

	protected BinderModule() {
		super();
	}

	protected BinderModule( Scope inital ) {
		super( inital );
	}

	@Override
	public final void bootstrap( Bootstrapper bootstrap ) {
		bootstrap.install( this );
	}

	@Override
	public final void declare( Bindings bindings, Inspector inspector ) {
		init( bindings, inspector );
		declare();
	}

	protected abstract void declare();

	public <E extends Enum<E> & Extension<E, ? super T>, T> void extend( Class<E> extension,
			Class<? extends T> type ) {
		app().extend( extension, type );
	}

	public <E extends Enum<E> & Extension<E, ? super T>, T> void extend( E extension,
			Class<? extends T> type ) {
		app().extend( extension, type );
	}

	private ScopedBinder app() {
		return per( Scoped.APPLICATION );
	}

}
