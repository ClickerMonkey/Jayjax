package org.magnos.jayjax.json.convert;

import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonValue;


public class JsonConvertJson extends JsonConverter<JsonValue, JsonValue>
{

    public static final JsonConvertJson INSTANCE = new JsonConvertJson();
    
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
