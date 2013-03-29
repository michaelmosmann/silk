package se.jbee.inject.bind;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static se.jbee.inject.Dependency.dependency;
import static se.jbee.inject.Name.named;
import static se.jbee.inject.util.Typecast.providerTypeOf;

import org.junit.Test;

import se.jbee.inject.Dependency;
import se.jbee.inject.Injector;
import se.jbee.inject.bind.BinderModule;
import se.jbee.inject.bind.BuildinBundle;
import se.jbee.inject.bootstrap.Bootstrap;
import se.jbee.inject.bootstrap.BootstrapperBundle;
import se.jbee.inject.bootstrap.Inspect;
import se.jbee.inject.util.Provider;
import se.jbee.inject.util.Scoped;

/**
 * This test demonstrates how to switch between different implementations during runtime dependent
 * on a setting in some configuration object.
 * 
 * @author Jan Bernitt (jan@jbee.se)
 */
public class TestConfigurationDependentBinds {

	private static interface Validator {

		boolean valid( String input );
	}

	private static enum ValidationStrength {
		PERMISSIVE,
		STRICT
	}

	private static class Permissive
			implements Validator {

		@Override
		public boolean valid( String input ) {
			return true; // just for testing
		}

	}

	private static class Strict
			implements Validator {

		@Override
		public boolean valid( String input ) {
			return false; // just for testing
		}

	}

	private static class Configuration {

		private ValidationStrength validationStrength;

		/**
		 * Will be detected as service method and thereby used
		 */
		@SuppressWarnings ( "unused" )
		public ValidationStrength getValidationStrength() {
			return validationStrength;
		}

		public void setValidationStrength( ValidationStrength validationStrength ) {
			this.validationStrength = validationStrength;
		}

	}

	/*
	 * Module and Bundle code to setup scenario
	 */

	private static class ConfigurationDependentBindsModule
			extends BinderModule {

		@Override
		protected void declare() {
			per( Scoped.INJECTION ).bind( Validator.class ).toConfiguration(
					ValidationStrength.class );
			bind( named( (ValidationStrength) null ), Validator.class ).to( Permissive.class );
			bind( named( ValidationStrength.PERMISSIVE ), Validator.class ).to( Permissive.class );
			bind( named( ValidationStrength.STRICT ), Validator.class ).to( Strict.class );
			construct( Configuration.class );
			per( Scoped.INJECTION ).bind( Inspect.all().methods() ).in( Configuration.class );
		}
	}

	private static class ConfigurationDependentBindsBundle
			extends BootstrapperBundle {

		@Override
		protected void bootstrap() {
			install( ConfigurationDependentBindsModule.class );
			install( BuildinBundle.PROVIDER );
		}

	}

	@Test
	public void thatReconfigurationIsResolvedToAnotherImplementation() {
		Injector injector = Bootstrap.injector( ConfigurationDependentBindsModule.class );
		Dependency<Validator> dependency = dependency( Validator.class );
		Configuration config = injector.resolve( dependency( Configuration.class ) );
		Validator v = injector.resolve( dependency );
		String input = "input";
		assertTrue( v.valid( input ) ); // default was PERMISSIVE
		config.setValidationStrength( ValidationStrength.STRICT );
		v = injector.resolve( dependency );
		assertFalse( v.valid( input ) ); // STRICT
		config.setValidationStrength( ValidationStrength.PERMISSIVE );
		v = injector.resolve( dependency );
		assertTrue( v.valid( input ) ); // PERMISSIVE
	}

	@Test
	public void thatReconfigurationIsProvidedToAnotherImplementation() {
		Injector injector = Bootstrap.injector( ConfigurationDependentBindsBundle.class );
		Configuration config = injector.resolve( dependency( Configuration.class ) );
		Provider<Validator> v = injector.resolve( dependency( providerTypeOf( Validator.class ) ) );
		String input = "input";
		assertTrue( v.provide().valid( input ) );
		config.setValidationStrength( ValidationStrength.STRICT );
		assertFalse( v.provide().valid( input ) );
		config.setValidationStrength( ValidationStrength.PERMISSIVE );
		assertTrue( v.provide().valid( input ) );
	}
}
