package org.magnos.jayjax.json.convert;

import java.util.List;

import org.magnos.jayjax.json.JsonArray;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonValue;


public class JsonConvertList extends JsonConverter<List<Object>, JsonArray>
{
    
    private static final JsonConvertList INSTANCE = new JsonConvertList();
    
    public static final <T, JC extends JsonConverter<T, ?>> JC INSTANCE(Class<T> type)
    {
        return (JC) INSTANCE;
    }
    
    @Override
    public List<Object> read( JsonArray value )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonArray write( List<Object> value )
    {
        int n = value.size();
        JsonArray array = new JsonArray( new JsonValue[ n ] );
        
        for (int i = 0; i < n; i++)
        {
            Object e = value.get( i );
            
            if (e != null)
            {
                JsonConverter<Object, JsonValue> converter = JsonConverterFactory.getConverter( (Class<Object>)e.getClass() );
                
                array.set( i, converter.write( e ) );
            }
        }
        
        return array;
    }
    
}
