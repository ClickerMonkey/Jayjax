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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.CharBuffer;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.WeakHashMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

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

        // Date
        JsonConverters.addConvert( Date.class, JsonConvertDate.DATE );
        JsonConverters.addConvert( java.sql.Date.class, JsonConvertDate.SQL_DATE );
        JsonConverters.addConvert( Time.class, JsonConvertDate.TIME );
        JsonConverters.addConvert( Timestamp.class, JsonConvertDate.TIMESTAMP );
        JsonConverters.addConvert( Calendar.class, JsonConvertDate.CALENDAR );
        
        // Collection (out only)
        JsonConverters.addConvert( Collection.class, JsonConvertCollection.INSTANCE );
        
        // List (out only)
        JsonConverters.addConvert( List.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( ArrayList.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( LinkedList.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( Stack.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( Vector.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( CopyOnWriteArrayList.class, JsonConvertCollection.INSTANCE );
        
        // Set (out only)
        JsonConverters.addConvert( Set.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( SortedSet.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( ConcurrentSkipListSet.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( CopyOnWriteArraySet.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( HashSet.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( LinkedHashSet.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( TreeSet.class, JsonConvertCollection.INSTANCE );
        
        // Queue (out only)
        JsonConverters.addConvert( Queue.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( PriorityQueue.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( ConcurrentLinkedQueue.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( BlockingQueue.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( LinkedBlockingQueue.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( ArrayBlockingQueue.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( SynchronousQueue.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( PriorityBlockingQueue.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( TransferQueue.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( LinkedTransferQueue.class, JsonConvertCollection.INSTANCE );
        
        // Deque (out only)
        JsonConverters.addConvert( Deque.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( ArrayDeque.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( BlockingDeque.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( LinkedBlockingDeque.class, JsonConvertCollection.INSTANCE );
        JsonConverters.addConvert( ConcurrentLinkedDeque.class, JsonConvertCollection.INSTANCE );
        
        // Map (<String, String> only)
        JsonConverters.addConvert( Map.class, JsonConvertMap.MAP );
        JsonConverters.addConvert( Map.class, JsonConvertMap.MAP );
        JsonConverters.addConvert( HashMap.class, JsonConvertMap.HASH_MAP );
        JsonConverters.addConvert( LinkedHashMap.class, JsonConvertMap.LINKED_HASH_MAP );
        JsonConverters.addConvert( ConcurrentHashMap.class, JsonConvertMap.CONCURRENT_HASH_MAP );
        JsonConverters.addConvert( ConcurrentSkipListMap.class, JsonConvertMap.CONCURRENT_SKIP_LIST_MAP );
        JsonConverters.addConvert( IdentityHashMap.class, JsonConvertMap.IDENTITY_HASH_MAP );
        JsonConverters.addConvert( TreeMap.class, JsonConvertMap.TREE_MAP );
        JsonConverters.addConvert( NavigableMap.class, JsonConvertMap.TREE_MAP );
        JsonConverters.addConvert( SortedMap.class, JsonConvertMap.TREE_MAP );
        JsonConverters.addConvert( WeakHashMap.class, JsonConvertMap.WEAK_HASH_MAP );
        JsonConverters.addConvert( Hashtable.class, JsonConvertMap.HASHTABLE );

        // Miscellaneous
        JsonConverters.addConvert( Pattern.class, JsonConvertMisc.PATTERN );
        JsonConverters.addConvert( UUID.class, JsonConvertMisc.UUID_OBJECT );
        JsonConverters.addConvert( Class.class, JsonConvertMisc.CLASS );
        
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
