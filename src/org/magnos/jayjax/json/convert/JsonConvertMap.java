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

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import org.magnos.jayjax.json.Json;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonObject;
import org.magnos.jayjax.json.JsonValue;


public abstract class JsonConvertMap<T extends Map<String, String>> extends JsonConverter<T, JsonObject>
{

	protected abstract T emptyMap();

	@Override
	public T missing(Class<?> expectedType)
	{
		return emptyMap();
	}
	
	@Override
	public T read( JsonObject value )
	{
		T map = emptyMap();

		for (Entry<String, JsonValue> e : value.getObject().entrySet())
		{
			map.put( e.getKey(), e.getValue().getObject().toString() );
		}

		return map;
	}

	@Override
	public JsonObject write( T value )
	{
		JsonObject object = new JsonObject();

		try
		{
			for (Entry<String, String> e : value.entrySet())
			{
				object.set( e.getKey(), Json.valueOf( e.getValue() ) );
			}
		}
		catch (IOException ex)
		{
			throw new RuntimeException( ex );
		}

		return object;
	}
	
	public static JsonConvertMap<Map<String, String>> MAP = new JsonConvertMap<Map<String,String>>() {
		protected Map<String, String> emptyMap() {
			return new HashMap<String, String>();
		}
	};
	
	public static JsonConvertMap<HashMap<String, String>> HASH_MAP = new JsonConvertMap<HashMap<String,String>>() {
		protected HashMap<String, String> emptyMap() {
			return new HashMap<String, String>();
		}
	};
	
	public static JsonConvertMap<LinkedHashMap<String, String>> LINKED_HASH_MAP = new JsonConvertMap<LinkedHashMap<String,String>>() {
		protected LinkedHashMap<String, String> emptyMap() {
			return new LinkedHashMap<String, String>();
		}
	};
	
	public static JsonConvertMap<ConcurrentHashMap<String, String>> CONCURRENT_HASH_MAP = new JsonConvertMap<ConcurrentHashMap<String,String>>() {
		protected ConcurrentHashMap<String, String> emptyMap() {
			return new ConcurrentHashMap<String, String>();
		}
	};
	
	public static JsonConvertMap<ConcurrentSkipListMap<String, String>> CONCURRENT_SKIP_LIST_MAP = new JsonConvertMap<ConcurrentSkipListMap<String,String>>() {
		protected ConcurrentSkipListMap<String, String> emptyMap() {
			return new ConcurrentSkipListMap<String, String>();
		}
	};
	
	public static JsonConvertMap<IdentityHashMap<String, String>> IDENTITY_HASH_MAP = new JsonConvertMap<IdentityHashMap<String,String>>() {
		protected IdentityHashMap<String, String> emptyMap() {
			return new IdentityHashMap<String, String>();
		}
	};
	
	public static JsonConvertMap<TreeMap<String, String>> TREE_MAP = new JsonConvertMap<TreeMap<String,String>>() {
		protected TreeMap<String, String> emptyMap() {
			return new TreeMap<String, String>();
		}
	};
	
	public static JsonConvertMap<WeakHashMap<String, String>> WEAK_HASH_MAP = new JsonConvertMap<WeakHashMap<String,String>>() {
		protected WeakHashMap<String, String> emptyMap() {
			return new WeakHashMap<String, String>();
		}
	};
	
	public static JsonConvertMap<Hashtable<String, String>> HASHTABLE = new JsonConvertMap<Hashtable<String,String>>() {
		protected Hashtable<String, String> emptyMap() {
			return new Hashtable<String, String>();
		}
	};
	

}
