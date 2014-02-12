package org.magnos.jayjax.json.convert;

import java.util.Collection;

import org.magnos.jayjax.json.JsonArray;
import org.magnos.jayjax.json.JsonConverter;
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
                JsonConverter<Object, JsonValue> converter = JsonConverterFactory.getConverter( (Class<Object>)e.getClass() );
                
                array.set( index, converter.write( e ) );
            }
        	
        	index++;
        }
        
        return array;
    }
    
}
