package org.magnos.jayjax.json.convert;

import org.magnos.jayjax.json.JsonBoolean;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonNumber;
import org.magnos.jayjax.json.JsonString;
import org.magnos.jayjax.json.JsonValue;


public class JsonConvertPrimitive
{

    public static final JsonConverter<Byte, JsonNumber> BYTE = new JsonConverter<Byte, JsonNumber>() {
        public Byte read( JsonNumber value ) {
            return value.get().byteValue();
        }
        public JsonNumber write( Byte value ) {
            return new JsonNumber( value );
        }
    };

    public static final JsonConverter<Short, JsonNumber> SHORT = new JsonConverter<Short, JsonNumber>() {
        public Short read( JsonNumber value ) {
            return value.get().shortValue();
        }
        public JsonNumber write( Short value ) {
            return new JsonNumber( value );
        }
    };

    public static final JsonConverter<Integer, JsonNumber> INT = new JsonConverter<Integer, JsonNumber>() {
        public Integer read( JsonNumber value ) {
            return value.get().intValue();
        }
        public JsonNumber write( Integer value ) {
            return new JsonNumber( value );
        }
    };

    public static final JsonConverter<Long, JsonNumber> LONG = new JsonConverter<Long, JsonNumber>() {
        public Long read( JsonNumber value ) {
            return value.get().longValue();
        }
        public JsonNumber write( Long value ) {
            return new JsonNumber( value );
        }
    };

    public static final JsonConverter<Float, JsonNumber> FLOAT = new JsonConverter<Float, JsonNumber>() {
        public Float read( JsonNumber value ) {
            return value.get().floatValue();
        }
        public JsonNumber write( Float value ) {
            return new JsonNumber( value );
        }
    };

    public static final JsonConverter<Double, JsonNumber> DOUBLE = new JsonConverter<Double, JsonNumber>() {
        public Double read( JsonNumber value ) {
            return value.get().doubleValue();
        }
        public JsonNumber write( Double value ) {
            return new JsonNumber( value );
        }
    };
    
    public static final JsonConverter<Character, JsonValue> CHAR = new JsonConverter<Character, JsonValue>() {
        public Character read( JsonValue value ) {
            if (value instanceof JsonNumber) {
                return (char)((JsonNumber)value).get().intValue();
            } else if (value instanceof JsonString) {
                return ((JsonString)value).get().charAt( 0 );
            } else {
                throw new RuntimeException( value.getClass().getSimpleName() +" is not valid for a Character" );
            }
        }
        public JsonValue write( Character value ) {
            return new JsonString( value.toString() );
        }
    };
    
    public static final JsonConverter<Boolean, JsonBoolean> BOOLEAN = new JsonConverter<Boolean, JsonBoolean>() {
        public Boolean read( JsonBoolean value ) {
            return value.get();
        }
        public JsonBoolean write( Boolean value ) {
            return new JsonBoolean( value );
        }
    };
    
}
