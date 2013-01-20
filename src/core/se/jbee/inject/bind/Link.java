/*
 *  Copyright (c) 2012, Jan Bernitt 
 *			
 *  Licensed under the Apache License, Version 2.0, http://www.apache.org/licenses/LICENSE-2.0
 */
package se.jbee.inject.bind;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import se.jbee.inject.Expiry;
import se.jbee.inject.Precision;
import se.jbee.inject.Repository;
import se.jbee.inject.Resource;
import se.jbee.inject.Scope;
import se.jbee.inject.Source;
import se.jbee.inject.Supplier;
import se.jbee.inject.util.Scoped;
import se.jbee.inject.util.Suppliable;
import se.jbee.inject.util.TypeReflector;

public final class Link {

	public static final ConstructionStrategy DEFAULE_CONSTRUCTION_STRATEGY = new BuildinConstructionStrategy();
	public static final Linker<Suppliable<?>> BUILDIN = linker( defaultExpiration() );

	private static Linker<Suppliable<?>> linker( Map<Scope, Expiry> expiryByScope ) {
		return new SuppliableLinker( expiryByScope );
	}

	private static IdentityHashMap<Scope, Expiry> defaultExpiration() {
		IdentityHashMap<Scope, Expiry> map = new IdentityHashMap<Scope, Expiry>();
		map.put( Scoped.APPLICATION, Expiry.NEVER );
		map.put( Scoped.INJECTION, Expiry.expires( 1000 ) );
		map.put( Scoped.THREAD, Expiry.expires( 500 ) );
		map.put( Scoped.DEPENDENCY_TYPE, Expiry.NEVER );
		map.put( Scoped.TARGET_INSTANCE, Expiry.NEVER );
		return map;
	}

	private Link() {
		throw new UnsupportedOperationException( "util" );
	}

	private static class BuildinConstructionStrategy
			implements ConstructionStrategy {

		BuildinConstructionStrategy() {
			// make visible
		}

		@Override
		public <T> Constructor<T> constructorFor( Class<T> type ) {
			try {
				return TypeReflector.defaultConstructor( type );
			} catch ( RuntimeException e ) {
				return null;
			}
		}

	}

	private static class SuppliableLinker
			implements Linker<Suppliable<?>> {

		private final Map<Scope, Expiry> expiryByScope;

		SuppliableLinker( Map<Scope, Expiry> expiryByScope ) {
			super();
			this.expiryByScope = expiryByScope;
		}

		@Override
		public Suppliable<?>[] link( ConstructionStrategy strategy, Module... modules ) {
			return link( cleanedUp( bindingsFrom( modules, strategy ) ) );
		}

		private Suppliable<?>[] link( Binding<?>[] bindings ) {
			Map<Scope, Repository> repositories = initRepositories( bindings );
			Suppliable<?>[] suppliables = new Suppliable<?>[bindings.length];
			for ( int i = 0; i < bindings.length; i++ ) {
				Binding<?> binding = bindings[i];
				Scope scope = binding.scope;
				Expiry expiry = expiryByScope.get( scope );
				if ( expiry == null ) {
					expiry = Expiry.NEVER;
				}
				suppliables[i] = binding.suppliableIn( repositories.get( scope ), expiry );
			}
			return suppliables;
		}

		private Map<Scope, Repository> initRepositories( Binding<?>[] bindings ) {
			Map<Scope, Repository> repositories = new IdentityHashMap<Scope, Repository>();
			for ( Binding<?> i : bindings ) {
				Repository repository = repositories.get( i.scope );
				if ( repository == null ) {
					repositories.put( i.scope, i.scope.init() );
				}
			}
			// TODO snapshot wrappers  
			return repositories;
		}

		private Binding<?>[] cleanedUp( Binding<?>[] bindings ) {
			if ( bindings.length <= 1 ) {
				return bindings;
			}
			List<Binding<?>> res = new ArrayList<Binding<?>>( bindings.length );
			Arrays.sort( bindings );
			res.add( bindings[0] );
			int lastIndependend = 0;
			for ( int i = 1; i < bindings.length; i++ ) {
				Binding<?> one = bindings[lastIndependend];
				Binding<?> other = bindings[i];
				boolean equalResource = one.resource.equalTo( other.resource );
				if ( !equalResource || !other.source.getType().replacedBy( one.source.getType() ) ) {
					res.add( other );
					lastIndependend = i;
				} else if ( one.source.getType().clashesWith( other.source.getType() ) ) {
					throw new IllegalStateException( "Duplicate binds:" + one + "," + other );
				}
			}
			return res.toArray( new Binding[res.size()] );
		}

		private Binding<?>[] bindingsFrom( Module[] modules, ConstructionStrategy strategy ) {
			ListBindings bindings = new ListBindings();
			for ( Module m : modules ) {
				m.declare( bindings, strategy );
			}
			return bindings.list.toArray( new Binding<?>[0] );
		}

	}

	private static class ListBindings
			implements Bindings {

		final List<Binding<?>> list = new ArrayList<Binding<?>>( 100 );

		ListBindings() {
			// make visible
		}

		@Override
		public <T> void add( Resource<T> resource, Supplier<? extends T> supplier, Scope scope,
				Source source ) {
			list.add( new Binding<T>( resource, supplier, scope, source ) );
		}

	}

	private static final class Binding<T>
			implements Comparable<Binding<?>> {

		final Resource<T> resource;
		final Supplier<? extends T> supplier;
		final Scope scope;
		final Source source;

		Binding( Resource<T> resource, Supplier<? extends T> supplier, Scope scope, Source source ) {
			super();
			this.resource = resource;
			this.supplier = supplier;
			this.scope = scope;
			this.source = source;
		}

		@Override
		public int compareTo( Binding<?> other ) {
			int res = Precision.comparePrecision( resource.getInstance(),
					other.resource.getInstance() );
			if ( res != 0 ) {
				return res;
			}
			res = Precision.comparePrecision( resource.getTarget(), other.resource.getTarget() );
			if ( res != 0 ) {
				return res;
			}
			res = Precision.comparePrecision( source, other.source );
			if ( res != 0 ) {
				return res;
			}
			return -1; // keep order
		}

		@Override
		public String toString() {
			return source + " / " + resource + " / " + scope;
		}

		Suppliable<T> suppliableIn( Repository repository, Expiry expiration ) {
			return new Suppliable<T>( resource, supplier, repository, expiration, source );
		}

	}
}