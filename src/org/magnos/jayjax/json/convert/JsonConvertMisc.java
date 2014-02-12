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

import java.util.UUID;
import java.util.regex.Pattern;

import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonString;



public class JsonConvertMisc
{

	public static final JsonConverter<Pattern, JsonString> PATTERN = new JsonConverter<Pattern, JsonString>() {
		public JsonString write( Pattern value ) {
			return new JsonString( value.pattern() );
		}
		public Pattern read( JsonString value ) {
			return Pattern.compile( value.get() );
		}
	};
	
	public static final JsonConverter<UUID, JsonString> UUID_OBJECT = new JsonConverter<UUID, JsonString>() {
		public JsonString write( UUID value ) {
			return new JsonString( value.toString() );
		}
		public UUID read( JsonString value ) {
			return UUID.fromString( value.get() );
		}
	}; 
	
	public static final JsonConverter<Class<?>, JsonString> CLASS = new JsonConverter<Class<?>, JsonString>() {
		public JsonString write( Class<?> value ) {
			return new JsonString( value.getCanonicalName() );
		}
		public Class<?> read( JsonString value ) {
			try {
				return Class.forName( value.get() );
			} catch (Exception e) {
				throw new RuntimeException( e );
			}
		}
	}; 

}
