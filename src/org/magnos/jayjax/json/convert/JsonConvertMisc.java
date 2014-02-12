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
