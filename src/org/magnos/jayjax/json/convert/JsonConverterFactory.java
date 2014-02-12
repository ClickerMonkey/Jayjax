
package org.magnos.jayjax.json.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import javax.swing.text.Segment;

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
        JsonConverters.addConvert( BigInteger.class, JsonConvertPrimitive.BIG_INTEGER );
        JsonConverters.addConvert( BigDecimal.class, JsonConvertPrimitive.BIG_DECIMAL );
        JsonConverters.addConvert( AtomicInteger.class, JsonConvertPrimitive.ATOMIC_INTEGER );
        JsonConverters.addConvert( AtomicLong.class, JsonConvertPrimitive.ATOMIC_LONG );
        
        // Strings
        JsonConverters.addConvert( String.class, JsonConvertString.STRING );
        JsonConverters.addConvert( StringBuilder.class, JsonConvertString.STRING_BUILDER );
        JsonConverters.addConvert( StringBuffer.class, JsonConvertString.STRING_BUFFER );
        JsonConverters.addConvert( CharSequence.class, JsonConvertString.CHAR_SEQUENCE );
        JsonConverters.addConvert( Appendable.class, JsonConvertString.APPENDABLE );
        JsonConverters.addConvert( Segment.class, JsonConvertString.SEGMENT );
        JsonConverters.addConvert( CharBuffer.class, JsonConvertString.CHAR_BUFFER );
        
        // List (out only)
        JsonConverters.addConvert( List.class, JsonConvertList.INSTANCE );
        JsonConverters.addConvert( ArrayList.class, JsonConvertList.INSTANCE );
        JsonConverters.addConvert( LinkedList.class, JsonConvertList.INSTANCE );
        JsonConverters.addConvert( Stack.class, JsonConvertList.INSTANCE );
        JsonConverters.addConvert( Vector.class, JsonConvertList.INSTANCE );
        
        // Set (out only)
        JsonConverters.addConvert( Set.class, JsonConvertSet.INSTANCE );
        JsonConverters.addConvert( ConcurrentSkipListSet.class, JsonConvertSet.INSTANCE );
        JsonConverters.addConvert( CopyOnWriteArraySet.class, JsonConvertSet.INSTANCE );
        JsonConverters.addConvert( HashSet.class, JsonConvertSet.INSTANCE );
        JsonConverters.addConvert( LinkedHashSet.class, JsonConvertSet.INSTANCE );
        JsonConverters.addConvert( TreeSet.class, JsonConvertSet.INSTANCE );
        
        // Map (<String, String> only)
        JsonConverters.addConvert( Map.class, JsonConvertMap.MAP );
        JsonConverters.addConvert( HashMap.class, JsonConvertMap.HASH_MAP );
        JsonConverters.addConvert( LinkedHashMap.class, JsonConvertMap.LINKED_HASH_MAP );
        JsonConverters.addConvert( ConcurrentHashMap.class, JsonConvertMap.CONCURRENT_HASH_MAP );
        JsonConverters.addConvert( ConcurrentSkipListMap.class, JsonConvertMap.CONCURRENT_SKIP_LIST_MAP );
        JsonConverters.addConvert( IdentityHashMap.class, JsonConvertMap.IDENTITY_HASH_MAP );
        JsonConverters.addConvert( TreeMap.class, JsonConvertMap.TREE_MAP );
        JsonConverters.addConvert( WeakHashMap.class, JsonConvertMap.WEAK_HASH_MAP );
        
        // JSON
        JsonConverters.addConvert( JsonValue.class, JsonConvertJson.INSTANCE );
        JsonConverters.addConvert( JsonEmpty.class, JsonConvertJson.INSTANCE );
        JsonConverters.addConvert( JsonNull.class, JsonConvertJson.INSTANCE );
        JsonConverters.addConvert( JsonBoolean.class, JsonConvertJson.INSTANCE );
        JsonConverters.addConvert( JsonString.class, JsonConvertJson.INSTANCE );
        JsonConverters.addConvert( JsonNumber.class, JsonConvertJson.INSTANCE );
        JsonConverters.addConvert( JsonArray.class, JsonConvertJson.INSTANCE );
        JsonConverters.addConvert( JsonObject.class, JsonConvertJson.INSTANCE );
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
