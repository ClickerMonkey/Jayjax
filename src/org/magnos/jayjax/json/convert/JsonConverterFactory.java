
package org.magnos.jayjax.json.convert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;
import java.util.Vector;

import org.magnos.jayjax.json.JsonArray;
import org.magnos.jayjax.json.JsonBoolean;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonConverters;
import org.magnos.jayjax.json.JsonEmpty;
import org.magnos.jayjax.json.JsonNull;
import org.magnos.jayjax.json.JsonNumber;
import org.magnos.jayjax.json.JsonObject;
import org.magnos.jayjax.json.JsonString;
import org.magnos.jayjax.json.JsonValue;


public class JsonConverterFactory
{

    static
    {
        // Primitives
        JsonConverters.addConvert( byte.class, JsonConvertPrimitive.BYTE );
        JsonConverters.addConvert( Byte.class, JsonConvertPrimitive.BYTE );
        JsonConverters.addConvert( short.class, JsonConvertPrimitive.SHORT );
        JsonConverters.addConvert( Short.class, JsonConvertPrimitive.SHORT );
        JsonConverters.addConvert( int.class, JsonConvertPrimitive.INT );
        JsonConverters.addConvert( Integer.class, JsonConvertPrimitive.INT );
        JsonConverters.addConvert( long.class, JsonConvertPrimitive.LONG );
        JsonConverters.addConvert( Long.class, JsonConvertPrimitive.LONG );
        JsonConverters.addConvert( float.class, JsonConvertPrimitive.FLOAT );
        JsonConverters.addConvert( Float.class, JsonConvertPrimitive.FLOAT );
        JsonConverters.addConvert( double.class, JsonConvertPrimitive.DOUBLE );
        JsonConverters.addConvert( Double.class, JsonConvertPrimitive.DOUBLE );
        JsonConverters.addConvert( char.class, JsonConvertPrimitive.CHAR );
        JsonConverters.addConvert( Character.class, JsonConvertPrimitive.CHAR );
        JsonConverters.addConvert( boolean.class, JsonConvertPrimitive.BOOLEAN );
        JsonConverters.addConvert( Boolean.class, JsonConvertPrimitive.BOOLEAN );

        // Strings
        JsonConverters.addConvert( String.class, JsonConvertString.STRING );
        JsonConverters.addConvert( StringBuilder.class, JsonConvertString.STRING_BUILDER );
        JsonConverters.addConvert( StringBuffer.class, JsonConvertString.STRING_BUFFER );
        JsonConverters.addConvert( CharSequence.class, JsonConvertString.CHAR_SEQUENCE );
        JsonConverters.addConvert( Appendable.class, JsonConvertString.APPENDABLE );

        // List
        JsonConverters.addConvert( List.class, JsonConvertList.INSTANCE( List.class ) );
        JsonConverters.addConvert( ArrayList.class, JsonConvertList.INSTANCE( ArrayList.class ) );
        JsonConverters.addConvert( LinkedList.class, JsonConvertList.INSTANCE( LinkedList.class ) );
        JsonConverters.addConvert( Stack.class, JsonConvertList.INSTANCE( Stack.class ) );
        JsonConverters.addConvert( Vector.class, JsonConvertList.INSTANCE( Vector.class ) );
        
        // JSON
        JsonConverters.addConvert( JsonEmpty.class, JsonConvertJson.INSTANCE( JsonEmpty.class ) );
        JsonConverters.addConvert( JsonNull.class, JsonConvertJson.INSTANCE( JsonNull.class ) );
        JsonConverters.addConvert( JsonBoolean.class, JsonConvertJson.INSTANCE( JsonBoolean.class ) );
        JsonConverters.addConvert( JsonString.class, JsonConvertJson.INSTANCE( JsonString.class ) );
        JsonConverters.addConvert( JsonNumber.class, JsonConvertJson.INSTANCE( JsonNumber.class ) );
        JsonConverters.addConvert( JsonArray.class, JsonConvertJson.INSTANCE( JsonArray.class ) );
        JsonConverters.addConvert( JsonObject.class, JsonConvertJson.INSTANCE( JsonObject.class ) );
    }

    @SuppressWarnings ("rawtypes" )
    public static <T> JsonConverter<T, JsonValue> getConverter( Class<T> type )
    {
        JsonConverter<T, JsonValue> converter = JsonConverters.getConvert( type );

        if (converter == null)
        {
            if (type.isEnum())
            {
                converter = new JsonConvertEnum( type );
            }
            else if (type.isArray())
            {
                converter = new JsonConvertArray( type.getComponentType() );
            }
            else if (!type.isAnnotation() && !type.isInterface())
            {
                converter = new JsonConvertObject( type );
            }
            else
            {
                throw new RuntimeException( "Cannot create a converter for an annotation or interface " + type.getSimpleName() );
            }

            JsonConverters.addConvert( type, converter );
        }

        return converter;
    }

}
