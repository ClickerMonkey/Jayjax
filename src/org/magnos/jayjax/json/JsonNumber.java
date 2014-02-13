/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

package org.magnos.jayjax.json;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A numerical value. The internal value can be one of:
 * <ul>
 * <li>{@link java.lang.Integer}</li>
 * <li>{@link java.lang.Long}</li>
 * <li>{@link java.lang.Float}</li>
 * </ul>
 * 
 * @author Philip Diffenderfer
 * 
 */
public class JsonNumber implements JsonValue
{

	private Number value;

	/**
	 * Instantiates a new JsonBoolean.
	 * 
	 * @param value
	 *        The initial value.
	 */
	public JsonNumber( Number value )
	{
		this.value = value;
	}

	/**
	 * Returns the current value.
	 * 
	 * @return The current value.
	 */
	public Number get()
	{
		return value;
	}

	/**
	 * Sets the new value.
	 * 
	 * @param value
	 *        The new value.
	 */
	public void set( Number value )
	{
		this.value = value;
	}

	@Override
	public Number getObject()
	{
		return value;
	}

	@Override
	public JsonType getType()
	{
		return JsonType.NUMBER;
	}

	@Override
	public String toJson()
	{
		return (value == null ? Json.NULL : value.toString());
	}

	@Override
	public void write( JsonWriter out ) throws IOException
	{
		out.write( value );
	}

	/**
	 * The regular expression for parsing a JSON number.
	 * 
	 * 1 = sign 2 = integer 4 = decimal 6 = exponent sign 7 = exponent number
	 * 
	 */
	private static Pattern REGEX = Pattern.compile( "^(-)?(|[0-9]*)(|\\.([0-9]+))(|[eE](|[+-])([0-9]+))$" );

	/**
	 * Parses a JsonNumber from the given String. If the given String is not a
	 * valid JSON number then this will return null.
	 * 
	 * @param x
	 *        The number to parse.
	 * @return The parsed JsonNumber or null if the String was not valid.
	 */
	public static JsonNumber fromString( String x )
	{
		if (x == null || x.length() == 0)
		{
			return null;
		}

		Matcher m = REGEX.matcher( x );

		if (!m.matches())
		{
			return null;
		}

		int sign = m.group( 1 ) != null ? -1 : 1;
		String integer = m.group( 2 );
		String decimal = m.group( 4 );
		String esign = m.group( 6 );
		String escale = m.group( 7 );

		if (decimal == null && (esign == null || esign.length() == 0 || esign.charAt( 0 ) == '+'))
		{
			long y = Long.parseLong( integer, 10 );
			
			if (escale != null)
			{
				int scale = Integer.parseInt( escale, 10 );
				while (--scale >= 0)
				{
					y *= 10;
				}
			}
			
			if (y <= Integer.MAX_VALUE)
			{
				return new JsonNumber( (int)y * sign );
			}
			
			return new JsonNumber( y * sign );
		}

		return new JsonNumber( Float.parseFloat( x ) );
	}

}
