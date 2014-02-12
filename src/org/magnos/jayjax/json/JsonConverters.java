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

import java.util.concurrent.ConcurrentHashMap;



public class JsonConverters
{
    
    private static final ConcurrentHashMap<Class<?>, JsonConverter<?, ?>> converts
        = new ConcurrentHashMap<Class<?>, JsonConverter<?, ?>>();
    
    public static void addConvert(Class<?> type, JsonConverter<?, ?> convert)
    {
        converts.put( type, convert );
    }
    
    public static <T> JsonConverter<T, JsonValue> getConvert(Class<T> type)
    {
        return (JsonConverter<T, JsonValue>)converts.get( type );
    }
    
    public static JsonValue convert(Object value)
    {
        if (value == null) 
        {
            return JsonNull.INSTANCE;
        }
        
        JsonConverter<Object, JsonValue> convert = (JsonConverter<Object, JsonValue>)converts.get( value.getClass() );
        
        return convert.write( value );
    }
    
    public static Object convert(JsonValue value)
    {
        if (value == null || value == JsonNull.INSTANCE) 
        {
            return null;
        }
        
        JsonConverter<Object, JsonValue> convert = (JsonConverter<Object, JsonValue>)converts.get( value.getClass() );
        
        return convert.read( value );
    }
    
}
