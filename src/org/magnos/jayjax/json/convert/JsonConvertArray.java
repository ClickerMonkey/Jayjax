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

import java.lang.reflect.Array;

import org.magnos.jayjax.json.JsonArray;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonValue;


public class JsonConvertArray<T> extends JsonConverter<T, JsonArray>
{

    private Class<?> elementType;
    private JsonConverter<Object, JsonValue> elementConverter;
    
    public JsonConvertArray(Class<?> elementType)
    {
        this.elementType = elementType;
        this.elementConverter = JsonConverterFactory.getConverter( (Class<Object>)elementType );
    }

    @Override
    public T missing(Class<?> expectedType)
    {
        return (T) Array.newInstance( elementType, 0 );
    }
    
    @Override
    public T read( JsonArray value )
    {
        int n = value.length();
        
        Object array = Array.newInstance( elementType, n );
        
        for (int i = 0; i < n; i++)
        {
            JsonValue e = value.getValue( i );
            
            if (e != null)
            {
            	Array.set( array, i, elementConverter.read( e ) );
            }
        }
        
        return (T)array;
    }

    @Override
    public JsonArray write( T value )
    {
        int n = Array.getLength( value );
        
        JsonArray array = new JsonArray( new JsonValue[ n ] );
        
        for (int i = 0; i < n; i++)
        {
        	Object e = Array.get( value, i );
            
            if (e != null)
            {
                array.set( i, elementConverter.write( e ) );
            }
        }
        
        return array;
    }

}
