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
import java.io.InputStream;

import org.magnos.jayjax.io.CharacterReader;
import org.magnos.jayjax.io.CharacterSet;


/**
 * The main JSON class which contains constants and simple parsing methods.
 * 
 * @author Philip Diffenderfer
 * 
 */
public class Json
{

    public static final char OBJECT_START = '{';
    public static final char OBJECT_END = '}';
    public static final char ARRAY_START = '[';
    public static final char ARRAY_END = ']';
    public static final char STRING_START = '"';
    public static final char STRING_END = '"';
    public static final char ARRAY_SEPARATOR = ',';
    public static final char MEMBER_SEPARATOR = ':';

    public static final String TRUE = "true";
    public static final String FALSE = "false";
    public static final String NULL = "null";
    public static final String EMPTY = "";

    public static final char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    public static final CharacterSet SET_STRING_STOP = new CharacterSet( STRING_END );
    public static final CharacterSet SET_CONSTANT_STOP = new CharacterSet( new char[][] { { '\0', ' ' }, { MEMBER_SEPARATOR, ARRAY_END, STRING_END, OBJECT_END, ARRAY_SEPARATOR } } );
    public static final CharacterSet SET_MEMBER_STOP = new CharacterSet( new char[][] { { '\0', ' ' }, { MEMBER_SEPARATOR } } );

    private static final int ASCII_MAX = 128;
    private static final JsonFormat format = new JsonFormat();

    /**
     * Returns the JsonValue for the data contained in the given {@link String}.
     * 
     * @param x
     *        The String to parse for JSON data.
     * @return The parsed {@link JsonValue} implementation or null if the data
     *         was invalid.
     * @throws IOException
     *         An error occurred parsing the JSON data.
     */
    public static JsonValue valueOf( String x ) throws IOException
    {
        return format.readValueFromStream( CharacterReader.forString( x ) );
    }

    /**
     * Returns a JsonValue of a specified type for the data contained in the
     * given {@link String}.
     * 
     * @param x
     *        The String to parse for JSON data.
     * @param expectedType
     *        The expected {@link JsonValue} implementation.
     * @return The parsed {@link JsonValue} implementation or null if the data
     *         was invalid.
     * @throws IOException
     *         An error occurred parsing the JSON data.
     * @throws ClassCastException
     *         The expected type does not match the actual type parsed.
     */
    public static <J extends JsonValue> J valueOf( String x, Class<J> expectedType ) throws IOException
    {
        return (J)format.readValueFromStream( CharacterReader.forString( x ) );
    }

    public static JsonValue valueOf( InputStream in ) throws IOException
    {
        return format.readValueFromStream( CharacterReader.forStream( in ) );
    }

    public static JsonValue valueOf( CharacterReader reader ) throws IOException
    {
        return format.readValueFromStream( reader );
    }

    public static JsonValue valueOf( byte[] x ) throws IOException
    {
        return format.readValueFromStream( CharacterReader.forBytes( x ) );
    }

    public static <J extends JsonValue> J valueOf( byte[] x, Class<J> expectedType ) throws IOException
    {
        return (J)format.readValueFromStream( CharacterReader.forBytes( x ) );
    }

    public static JsonValue valueOf( char[] x ) throws IOException
    {
        return format.readValueFromStream( CharacterReader.forChars( x ) );
    }

    public static <J extends JsonValue> J valueOf( char[] x, Class<J> expectedType ) throws IOException
    {
        return (J)format.readValueFromStream( CharacterReader.forChars( x ) );
    }

    public static String toString( JsonValue value )
    {
        StringBuilder out = new StringBuilder();

        try
        {
            value.write( JsonWriter.forAppender( out ) );
        }
        catch (Exception e)
        {
            return null;
        }

        return out.toString();
    }

    /**
     * Encodes the given {@link CharSequence} as a JSON string data type. This
     * converts characters like double quotes, tabs, newline, to their escaped
     * alternatives. This also converts Unicode characters into it's encoded
     * alternative.
     * 
     * @param chars
     *        The {@link CharSequence} to encode.
     * @return The encoded {@link CharSequence} as a {@link String}.
     */
    public static String encode( CharSequence chars )
    {
        StringBuilder out = new StringBuilder();

        for (int i = 0; i < chars.length(); i++)
        {
            char c = chars.charAt( i );

            switch (c)
            {
            case '"':
                out.append( "\\\"" );
                break;
            case '\t':
                out.append( "\\t" );
                break;
            case '\n':
                out.append( "\\n" );
                break;
            case '\r':
                out.append( "\\r" );
                break;
            case '\f':
                out.append( "\\f" );
                break;
            case '\b':
                out.append( "\\b" );
                break;
            case '\\':
                out.append( "\\\\" );
                break;
            default:
                if (c < ASCII_MAX)
                {
                    out.append( c );
                }
                else
                {
                    out.append( "\\u" ).append( toHex( c >> 12 ) ).append( toHex( c >> 8 ) ).append( toHex( c >> 4 ) ).append( toHex( c >> 0 ) );
                }
                break;
            }
        }

        return out.toString();
    }

    /**
     * Converts the integer into the hex character equivalent.
     * 
     * @param b
     *        The integer to convert into a hex character.
     * @return The resulting hex character.
     */
    private static char toHex( int b )
    {
        return HEX[b & 0xF];
    }

    /**
     * Surrounds the given {@link String} by the quotes used in the JSON string
     * data type.
     * 
     * @param x
     *        The {@link String} to quote.
     * @return The quoted {@link String}.
     */
    public static String quote( String x )
    {
        return (x == null ? NULL : STRING_START + x + STRING_END);
    }

}
