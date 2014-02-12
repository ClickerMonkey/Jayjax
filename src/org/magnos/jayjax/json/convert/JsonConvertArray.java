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
    public T missing()
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
