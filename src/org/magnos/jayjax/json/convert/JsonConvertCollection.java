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

import java.util.Collection;

import org.magnos.jayjax.json.JsonArray;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonConverters;
import org.magnos.jayjax.json.JsonValue;


public class JsonConvertCollection extends JsonConverter<Collection<Object>, JsonArray>
{
    
    public static final JsonConvertCollection INSTANCE = new JsonConvertCollection();
    
    @Override
    public Collection<Object> read( JsonArray value )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonArray write( Collection<Object> value )
    {
        int n = value.size();
        JsonArray array = new JsonArray( new JsonValue[ n ] );
        int index = 0;
        
        for (Object e : value)
        {
        	if (e != null)
            {
                JsonConverter<Object, JsonValue> converter = JsonConverters.getConverter( e.getClass() );
                
                array.set( index, converter.write( e ) );
            }
        	
        	index++;
        }
        
        return array;
    }
    
}
