package org.magnos.jayjax.json.convert;

import java.util.Set;

import org.magnos.jayjax.json.JsonArray;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonValue;


public class JsonConvertSet extends JsonConverter<Set<Object>, JsonArray>
{
    
    public static final JsonConvertSet INSTANCE = new JsonConvertSet();
    
    @Override
    public Set<Object> read( JsonArray value )
    {
        throw new UnsupportedOperationException();
    }

    @Override
    public JsonArray write( Set<Object> value )
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
