package org.magnos.jayjax.json.convert;

import java.lang.reflect.Array;

import org.magnos.jayjax.json.JsonArray;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonString;
import org.magnos.jayjax.json.JsonValue;


public class JsonConvertArray<T> extends JsonConverter<T, JsonValue>
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
    public T read( JsonValue value )
    {
        if (value instanceof JsonString)
        {
            if (((JsonString)value).get().length() == 0)
            {
                return (T) Array.newInstance( elementType, 0 );
            }
            else
            {
                throw new RuntimeException( "An array cannot be read from a string value" );
            }
        }
        else if (!(value instanceof JsonArray))
        {
            throw new RuntimeException( "An array type was expected, this was found instead: " + value.getClass().getSimpleName() );
        }
        
        JsonArray valueArray = (JsonArray) value;
        
        int n = valueArray.length();
        
        Object array = Array.newInstance( elementType, n );
        
        for (int i = 0; i < n; i++)
        {
            JsonValue e = valueArray.getValue( i );
            
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
