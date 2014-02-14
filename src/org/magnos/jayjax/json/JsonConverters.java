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

import org.magnos.jayjax.json.convert.JsonConvertArray;
import org.magnos.jayjax.json.convert.JsonConvertCollection;
import org.magnos.jayjax.json.convert.JsonConvertDate;
import org.magnos.jayjax.json.convert.JsonConvertEnum;
import org.magnos.jayjax.json.convert.JsonConvertJson;
import org.magnos.jayjax.json.convert.JsonConvertMap;
import org.magnos.jayjax.json.convert.JsonConvertMisc;
import org.magnos.jayjax.json.convert.JsonConvertObject;
import org.magnos.jayjax.json.convert.JsonConvertPrimitive;
import org.magnos.jayjax.json.convert.JsonConvertString;


public class JsonConverters
{

	private static final ConcurrentHashMap<Class<?>, JsonConverter<Object, JsonValue>> converts = new ConcurrentHashMap<Class<?>, JsonConverter<Object, JsonValue>>();
	
	static
	{
		// Primitives
		addConvert( byte.class, JsonConvertPrimitive.BYTE );
		addConvert( Byte.class, JsonConvertPrimitive.BYTE );
		addConvert( short.class, JsonConvertPrimitive.SHORT );
		addConvert( Short.class, JsonConvertPrimitive.SHORT );
		addConvert( int.class, JsonConvertPrimitive.INT );
		addConvert( Integer.class, JsonConvertPrimitive.INT );
		addConvert( long.class, JsonConvertPrimitive.LONG );
		addConvert( Long.class, JsonConvertPrimitive.LONG );
		addConvert( float.class, JsonConvertPrimitive.FLOAT );
		addConvert( Float.class, JsonConvertPrimitive.FLOAT );
		addConvert( double.class, JsonConvertPrimitive.DOUBLE );
		addConvert( Double.class, JsonConvertPrimitive.DOUBLE );
		addConvert( char.class, JsonConvertPrimitive.CHAR );
		addConvert( Character.class, JsonConvertPrimitive.CHAR );
		addConvert( boolean.class, JsonConvertPrimitive.BOOLEAN );
		addConvert( Boolean.class, JsonConvertPrimitive.BOOLEAN );
		addConvert( BigInteger.class, JsonConvertPrimitive.BIG_INTEGER );
		addConvert( BigDecimal.class, JsonConvertPrimitive.BIG_DECIMAL );
		addConvert( AtomicInteger.class, JsonConvertPrimitive.ATOMIC_INTEGER );
		addConvert( AtomicLong.class, JsonConvertPrimitive.ATOMIC_LONG );

		// Strings
		addConvert( String.class, JsonConvertString.STRING );
		addConvert( StringBuilder.class, JsonConvertString.STRING_BUILDER );
		addConvert( StringBuffer.class, JsonConvertString.STRING_BUFFER );
		addConvert( CharSequence.class, JsonConvertString.CHAR_SEQUENCE );
		addConvert( Appendable.class, JsonConvertString.APPENDABLE );
		addConvert( Segment.class, JsonConvertString.SEGMENT );
		addConvert( CharBuffer.class, JsonConvertString.CHAR_BUFFER );

		// Date
		addConvert( Date.class, JsonConvertDate.DATE );
		addConvert( java.sql.Date.class, JsonConvertDate.SQL_DATE );
		addConvert( Time.class, JsonConvertDate.TIME );
		addConvert( Timestamp.class, JsonConvertDate.TIMESTAMP );
		addConvert( Calendar.class, JsonConvertDate.CALENDAR );

		// Collection (out only)
		addConvert( Collection.class, JsonConvertCollection.INSTANCE );

		// List (out only)
		addConvert( List.class, JsonConvertCollection.INSTANCE );
		addConvert( ArrayList.class, JsonConvertCollection.INSTANCE );
		addConvert( LinkedList.class, JsonConvertCollection.INSTANCE );
		addConvert( Stack.class, JsonConvertCollection.INSTANCE );
		addConvert( Vector.class, JsonConvertCollection.INSTANCE );
		addConvert( CopyOnWriteArrayList.class, JsonConvertCollection.INSTANCE );

		// Set (out only)
		addConvert( Set.class, JsonConvertCollection.INSTANCE );
		addConvert( SortedSet.class, JsonConvertCollection.INSTANCE );
		addConvert( ConcurrentSkipListSet.class, JsonConvertCollection.INSTANCE );
		addConvert( CopyOnWriteArraySet.class, JsonConvertCollection.INSTANCE );
		addConvert( HashSet.class, JsonConvertCollection.INSTANCE );
		addConvert( LinkedHashSet.class, JsonConvertCollection.INSTANCE );
		addConvert( TreeSet.class, JsonConvertCollection.INSTANCE );

		// Queue (out only)
		addConvert( Queue.class, JsonConvertCollection.INSTANCE );
		addConvert( PriorityQueue.class, JsonConvertCollection.INSTANCE );
		addConvert( ConcurrentLinkedQueue.class, JsonConvertCollection.INSTANCE );
		addConvert( BlockingQueue.class, JsonConvertCollection.INSTANCE );
		addConvert( LinkedBlockingQueue.class, JsonConvertCollection.INSTANCE );
		addConvert( ArrayBlockingQueue.class, JsonConvertCollection.INSTANCE );
		addConvert( SynchronousQueue.class, JsonConvertCollection.INSTANCE );
		addConvert( PriorityBlockingQueue.class, JsonConvertCollection.INSTANCE );
		addConvert( TransferQueue.class, JsonConvertCollection.INSTANCE );
		addConvert( LinkedTransferQueue.class, JsonConvertCollection.INSTANCE );

		// Deque (out only)
		addConvert( Deque.class, JsonConvertCollection.INSTANCE );
		addConvert( ArrayDeque.class, JsonConvertCollection.INSTANCE );
		addConvert( BlockingDeque.class, JsonConvertCollection.INSTANCE );
		addConvert( LinkedBlockingDeque.class, JsonConvertCollection.INSTANCE );
		addConvert( ConcurrentLinkedDeque.class, JsonConvertCollection.INSTANCE );

		// Map (<String, String> only)
		addConvert( Map.class, JsonConvertMap.MAP );
		addConvert( Map.class, JsonConvertMap.MAP );
		addConvert( HashMap.class, JsonConvertMap.HASH_MAP );
		addConvert( LinkedHashMap.class, JsonConvertMap.LINKED_HASH_MAP );
		addConvert( ConcurrentHashMap.class, JsonConvertMap.CONCURRENT_HASH_MAP );
		addConvert( ConcurrentSkipListMap.class, JsonConvertMap.CONCURRENT_SKIP_LIST_MAP );
		addConvert( IdentityHashMap.class, JsonConvertMap.IDENTITY_HASH_MAP );
		addConvert( TreeMap.class, JsonConvertMap.TREE_MAP );
		addConvert( NavigableMap.class, JsonConvertMap.TREE_MAP );
		addConvert( SortedMap.class, JsonConvertMap.TREE_MAP );
		addConvert( WeakHashMap.class, JsonConvertMap.WEAK_HASH_MAP );
		addConvert( Hashtable.class, JsonConvertMap.HASHTABLE );

		// Miscellaneous
		addConvert( Pattern.class, JsonConvertMisc.PATTERN );
		addConvert( UUID.class, JsonConvertMisc.UUID_OBJECT );
		addConvert( Class.class, JsonConvertMisc.CLASS );

		// JSON
		addConvert( JsonValue.class, JsonConvertJson.INSTANCE );
		addConvert( JsonEmpty.class, JsonConvertJson.INSTANCE );
		addConvert( JsonNull.class, JsonConvertJson.INSTANCE );
		addConvert( JsonBoolean.class, JsonConvertJson.INSTANCE );
		addConvert( JsonString.class, JsonConvertJson.INSTANCE );
		addConvert( JsonNumber.class, JsonConvertJson.INSTANCE );
		addConvert( JsonArray.class, JsonConvertJson.INSTANCE );
		addConvert( JsonObject.class, JsonConvertJson.INSTANCE );
	}

	public static void addConvert( Class<?> type, JsonConverter<?, ?> convert )
	{
		converts.put( type, (JsonConverter<Object, JsonValue>) convert );
	}

	public static JsonConverter<Object, JsonValue> getConvert( Class<?> type )
	{
		return converts.get( type );
	}

	public static JsonValue convert( Object value )
	{
		if (value == null)
		{
			return JsonNull.INSTANCE;
		}

		JsonConverter<Object, JsonValue> convert = getConverter( value.getClass() );

		return convert.write( value );
	}

	@SuppressWarnings ("rawtypes" )
	public static JsonConverter<Object, JsonValue> getConverter( Class<?> type )
	{
		JsonConverter<Object, JsonValue> converter = getConvert( type );

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

			addConvert( type, converter );
		}

		return converter;
	}

}
