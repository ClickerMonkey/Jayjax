package org.magnos.jayjax.json.convert;

import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonValue;


public class JsonConvertJson extends JsonConverter<JsonValue, JsonValue>
{

    private static final JsonConvertJson INSTANCE = new JsonConvertJson();
    
    public static final <T extends JsonValue, JC extends JsonConverter<T, ?>> JC INSTANCE(Class<T> type)
    {
        return (JC) INSTANCE;
    }
    
    @Override
    public JsonValue read( JsonValue value )
    {
        return value;
    }

    @Override
    public JsonValue write( JsonValue value )
    {
        return value;
    }

}
