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

package org.magnos.jayjax.json.convert;

import java.nio.CharBuffer;

import javax.swing.text.Segment;

import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonString;
import org.magnos.jayjax.json.JsonValue;


public class JsonConvertString
{

	private static String toString(JsonValue json)
	{
		return json.getObject().toString();
	}
	
    public static final JsonConverter<String, JsonString> STRING = new JsonConverter<String, JsonString>() {
        public String read( JsonString value ) {
            return JsonConvertString.toString( value );
        }
        public JsonString write( String value ) {
            return new JsonString( value );
        }
    };

    public static final JsonConverter<StringBuilder, JsonString> STRING_BUILDER = new JsonConverter<StringBuilder, JsonString>() {
        public StringBuilder read( JsonString value ) {
            return new StringBuilder( JsonConvertString.toString( value ) );
        }
        public JsonString write( StringBuilder value ) {
            return new JsonString( value );
        }
    };

    public static final JsonConverter<StringBuffer, JsonString> STRING_BUFFER = new JsonConverter<StringBuffer, JsonString>() {
        public StringBuffer read( JsonString value ) {
            return new StringBuffer( JsonConvertString.toString( value ) );
        }
        public JsonString write( StringBuffer value ) {
            return new JsonString( value );
        }
    };

    public static final JsonConverter<CharSequence, JsonString> CHAR_SEQUENCE = new JsonConverter<CharSequence, JsonString>() {
        public CharSequence read( JsonString value ) {
            return JsonConvertString.toString( value );
        }
        public JsonString write( CharSequence value ) {
            return new JsonString( value );
        }
    };

    public static final JsonConverter<Appendable, JsonString> APPENDABLE = new JsonConverter<Appendable, JsonString>() {
        public Appendable read( JsonString value ) {
            return new StringBuilder( JsonConvertString.toString( value ) );
        }
        public JsonString write( Appendable value ) {
            return new JsonString( value );
        }
    };

    public static final JsonConverter<Segment, JsonString> SEGMENT = new JsonConverter<Segment, JsonString>() {
        public Segment read( JsonString value ) {
        	String x = JsonConvertString.toString( value );
            return new Segment( x.toCharArray(), 0, x.length() );
        }
        public JsonString write( Segment value ) {
            return new JsonString( value );
        }
    };

    public static final JsonConverter<CharBuffer, JsonString> CHAR_BUFFER = new JsonConverter<CharBuffer, JsonString>() {
        public CharBuffer read( JsonString value ) {
        	return CharBuffer.wrap( JsonConvertString.toString( value ) );
        }
        public JsonString write( CharBuffer value ) {
            return new JsonString( value );
        }
    };
    
}
