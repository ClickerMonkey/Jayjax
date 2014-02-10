package org.magnos.jayjax.io.convert;

import java.lang.reflect.Array;

import org.magnos.jayjax.json.JsonArray;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonValue;


public class JsonConvertArray<T> implements JsonConverter<T[], JsonArray>
{

    private Class<T> elementType;
    private JsonConverter<T, JsonValue> elementConverter;
    
    public JsonConvertArray(Class<T> elementType)
    {
        this.elementType = elementType;
        this.elementConverter = JsonConverterFactory.getConverter( elementType );
    }
    
    @Override
    public T[] read( JsonArray value )
    {
        int n = value.length();
        
        T[] elements = (T[])Array.newInstance( elementType, n );
        
        for (int i = 0; i < n; i++)
        {
            JsonValue e = value.getValue( i );
            
            if (e != null)
            {
                elements[i] = elementConverter.read( e );
            }
        }
        
        return elements;
    }

    @Override
    public JsonArray write( T[] value )
    {
        int n = value.length;
        
        JsonArray array = new JsonArray( new JsonValue[ n ] );
        
        for (int i = 0; i < n; i++)
        {
            T e = value[i];
            
            if (e != null)
            {
                array.set( i, elementConverter.write( e ) );
            }
        }
        
        return array;
    }

}
