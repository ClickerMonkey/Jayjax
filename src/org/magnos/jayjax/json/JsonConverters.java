package org.magnos.jayjax.json;

import java.util.concurrent.ConcurrentHashMap;



public class JsonConverters
{
    
    private static final ConcurrentHashMap<Class<?>, JsonConverter<?, ?>> converts
        = new ConcurrentHashMap<Class<?>, JsonConverter<?, ?>>();
    
    public static <T> void addConvert(Class<T> type, JsonConverter<T, ?> convert)
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
