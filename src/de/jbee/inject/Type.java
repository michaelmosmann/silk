package de.jbee.inject;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;

/**
 * 
 * @author Jan Bernitt (jan.bernitt@gmx.de)
 */
public final class Type<T>
		implements Comparable<Type<?>> {

	public static Type<?> fieldType( Field field ) {
		return type( field.getType(), field.getGenericType() );
	}

	public static <T> Type<T> instanceType( Class<T> rawType, T instance ) {
		Class<?> type = instance.getClass();
		if ( type == rawType ) { // there will be no generic type arguments
			return new Type<T>( rawType );
		}
		java.lang.reflect.Type superclass = type.getGenericSuperclass();
		// TODO check if this is or has the raw type
		java.lang.reflect.Type[] interfaces = type.getGenericInterfaces();
		// TODO check if one of them is or has the raw type
		return null;
	}

	public static <T> Type<T> rawType( Class<T> type ) {
		return new Type<T>( type );
	}

	public static <T> Type<T> type( Class<T> rawType, java.lang.reflect.Type type ) {
		if ( type instanceof Class<?> ) {
			return rawType( rawType );
		}
		if ( type instanceof ParameterizedType ) {
			ParameterizedType pt = (ParameterizedType) type;
			if ( pt.getRawType() != rawType ) {
				throw new IllegalArgumentException( "The given raw type " + rawType
						+ " is not the raw type of the given type: " + type );
			}
			return new Type<T>( rawType, types( pt.getActualTypeArguments() ) );
		}
		throw notSupportedYet( type );
	}

	private static UnsupportedOperationException notSupportedYet( java.lang.reflect.Type type ) {
		return new UnsupportedOperationException( "Type has no support yet: " + type );
	}

	private static Type<?>[] types( java.lang.reflect.Type[] arguments ) {
		Type<?>[] args = new Type<?>[arguments.length];
		for ( int i = 0; i < arguments.length; i++ ) {
			args[i] = type( arguments[i] );
		}
		return args;
	}

	private static Type<?> type( java.lang.reflect.Type type ) {
		if ( type instanceof Class<?> ) {
			return rawType( (Class<?>) type );
		}
		if ( type instanceof ParameterizedType ) {
			return parameterizedtype( (ParameterizedType) type );
		}
		if ( type instanceof TypeVariable<?> ) {
			// TODO
		}
		throw notSupportedYet( type );
	}

	private static <T> Type<?> parameterizedtype( ParameterizedType type ) {
		@SuppressWarnings ( "unchecked" )
		Class<T> rawType = (Class<T>) type.getRawType();
		return new Type<T>( rawType, types( type.getActualTypeArguments() ) );
	}

	private final Class<T> rawType;
	private final Type<?>[] args;

	/**
	 * Used to model lower bound wildcard types like <code>? extends Foo</code>
	 */
	private final boolean lowerBound;

	private Type( boolean lowerBound, Class<T> rawType, Type<?>[] arguments ) {
		this.rawType = rawType;
		this.args = arguments;
		this.lowerBound = lowerBound;
	}

	private Type( Class<T> rawType, Type<?>[] arguments ) {
		this( false, rawType, arguments );
	}

	private Type( Class<T> rawType ) {
		this( false, rawType, new Type<?>[0] );
	}

	public Type<? extends T> asLowerBound() {
		return new Type<T>( true, rawType, args );
	}

	public Type<? extends T> exact() {
		return new Type<T>( false, rawType, args );
	}

	public boolean equalTo( Type<?> other ) {
		if ( rawType != other.rawType ) {
			return false;
		}
		if ( args.length != other.args.length ) {
			return false;
		}
		for ( int i = 0; i < args.length; i++ ) {
			if ( !args[i].equalTo( other.args[i] ) ) {
				return false;
			}
		}
		return true;
	}

	public Type<?> getElementType() {
		Type<?> elemRawType = getElementRawType();
		return elemRawType == this
			? this
			: elemRawType.parametized( args );
	}

	private Type<?> getElementRawType() {
		return rawType.isArray()
			? new Type( rawType.getComponentType() )
			: this;
	}

	public Class<T> getRawType() {
		return rawType;
	}

	public Type<?>[] getArguments() {
		return args;
	}

	public boolean isAssignableTo( Type<?> other ) {
		if ( !other.rawType.isAssignableFrom( rawType ) ) {
			return false;
		}
		if ( !isArgumented() ) {
			return true; //raw type is ok - no parameters to check
		}

		if ( other.rawType == rawType ) { // both have the same rawType
			return allArgumentsAreAssignableTo( other );
		}

		// this raw type is extending the rawType passed - check if it is implemented direct or passed

		// there is another trivial case: type has ? extends object for all parameters - that means will allow all 
		return true;
	}

	public boolean allArgumentsAreAssignableTo( Type<?> other ) {
		for ( int i = 0; i < args.length; i++ ) {
			if ( !args[i].asArgumentAssignableTo( other.args[i] ) ) {
				return false;
			}
		}
		return true;
	}

	private boolean asArgumentAssignableTo( Type<?> other ) {
		if ( rawType == other.rawType ) {
			return !isArgumented() || allArgumentsAreAssignableTo( other );
		}
		return other.isLowerBound() && isAssignableTo( other.exact() );
	}

	/**
	 * @return true if this type describes the lower bound of the required types.
	 */
	public boolean isLowerBound() {
		return lowerBound;
	}

	public boolean isArgumented() {
		return args.length > 0;
	}

	public boolean isParameterized() {
		return rawType.getTypeParameters().length > 0;
	}

	public boolean isUnidimensionalArray() {
		return rawType.isArray() && !rawType.getComponentType().isArray();
	}

	public boolean morePreciseThan( Type<? extends T> other ) {
		if ( !isArgumented() ) {
			return false; // it's equal
		}
		//TODO
		return true;
	}

	/**
	 * @return A {@link Type} having as its type arguments {@link #asLowerBound()}s.
	 */
	public Type<T> parametizedAsLowerBounds() { //TODO recursive version or one with a depth ?
		if ( !isArgumented() || allArgumentsAreLowerBounds() ) {
			return this;
		}
		Type<?>[] arguments = new Type<?>[args.length];
		for ( int i = 0; i < args.length; i++ ) {
			arguments[i] = args[i].asLowerBound();
		}
		return new Type<T>( lowerBound, rawType, arguments );
	}

	public boolean allArgumentsAreLowerBounds() {
		int c = 0;
		for ( int i = 0; i < args.length; i++ ) {
			if ( args[i].isLowerBound() ) {
				c++;
			}
		}
		return c == args.length;
	}

	public Type<T> parametized( Class<?>... arguments ) {
		Type<?>[] typeArgs = new Type<?>[arguments.length];
		for ( int i = 0; i < arguments.length; i++ ) {
			typeArgs[i] = rawType( arguments[i] );
		}
		return parametized( typeArgs );
	}

	public Type<T> parametized( Type<?>... arguments ) {
		validateArguments( arguments );
		return new Type<T>( lowerBound, rawType, arguments );
	}

	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();
		toString( b );
		return b.toString();
	}

	void toString( StringBuilder b ) {
		if ( isLowerBound() ) {
			b.append( "? extends " );
		}
		b.append( rawType.getCanonicalName() );
		if ( isArgumented() ) {
			b.append( '<' );
			args[0].toString( b );
			for ( int i = 1; i < args.length; i++ ) {
				b.append( ',' );
				args[i].toString( b );
			}
			b.append( '>' );
		}
	}

	private void validateArguments( Type<?>... arguments ) {
		if ( arguments.length == 0 ) {
			return; // its treated as raw-type
		}
		TypeVariable<Class<T>>[] params = rawType.getTypeParameters();
		if ( params.length != arguments.length ) {
			if ( isUnidimensionalArray() ) {
				getElementRawType().validateArguments( arguments );
				return;
			}
			//OPEN maybe we can allow to specify less than params - all not specified will be ?
			throw new IllegalArgumentException( "Invalid nuber of type arguments" );
		}
		// TODO check bounds fulfilled by arguments
	}

	public Type<?> asSupertype( Class<? super T> supertype ) {
		if ( supertype == rawType ) {
			return this;
		}
		Class<? super T> superclass = rawType.getSuperclass();
		java.lang.reflect.Type genericSuperclass = rawType.getGenericSuperclass();
		while ( superclass != null && superclass != supertype ) {
			genericSuperclass = superclass.getGenericSuperclass();
			superclass = superclass.getSuperclass();
		}
		if ( superclass == supertype ) {
			return type( superclass, genericSuperclass );
		}
		Class<?>[] interfaces = rawType.getInterfaces();
		java.lang.reflect.Type[] genericInterfaces = rawType.getGenericInterfaces();
		for ( int i = 0; i < interfaces.length; i++ ) {
			if ( interfaces[i] == supertype ) {
				return type( interfaces[i], genericInterfaces[i] );
			}
		}
		throw new RuntimeException( "Couldn't find supertype " + supertype + " for type: " + this );
	}

	@Override
	public int compareTo( Type<?> other ) {
		// TODO Auto-generated method stub
		return 0;
	}
}
