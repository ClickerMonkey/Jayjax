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

import java.util.HashMap;
import java.util.Map;

import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonString;


public class JsonConvertEnum<T extends Enum<T>> extends JsonConverter<T, JsonString>
{

    private Map<String, T> enumConstantMap;
    private JsonString[] enumStrings;
    
    public JsonConvertEnum(Class<T> enumClass)
    {
        final T[] enumConstants = enumClass.getEnumConstants();
        final int n = enumConstants.length;
        
        enumConstantMap = new HashMap<String, T>( n );
        enumStrings = new JsonString[ n ];
        
        for (int i = 0; i < n; i++)
        {
            T e = enumConstants[i];
            
            enumConstantMap.put( e.name(), e );
            enumStrings[i] = new JsonString( e.name() );
        }
    }
    
    @Override
    public T read( JsonString value )
    {
        return enumConstantMap.get( value.get() );
    }

    @Override
    public JsonString write( T value )
    {
        return enumStrings[ value.ordinal() ];
    }

}
