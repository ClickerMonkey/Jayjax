package org.magnos.jayjax.io.convert;

import java.util.HashMap;
import java.util.Map;

import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonString;


public class JsonConvertEnum<T extends Enum<T>> implements JsonConverter<T, JsonString>
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
