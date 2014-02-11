
package org.magnos.jayjax.resolve;

import java.io.IOException;

import javax.servlet.ServletException;

import org.magnos.jayjax.ArgumentResolver;
import org.magnos.jayjax.Invocation;
import org.magnos.jayjax.json.Json;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonValue;
import org.magnos.jayjax.json.convert.JsonConverterFactory;


public class ActionResolver extends ArgumentResolver
{

    protected final int group;
    protected final JsonConverter<Object, JsonValue> converter;

    public ActionResolver( String name, Class<?> type, int group )
    {
        super( name, type );

        this.group = group;
        this.converter = JsonConverterFactory.getConverter( (Class<Object>)type );
    }

    @Override
    public Object getArgument( Invocation invocation ) throws IOException, ServletException
    {
        String value = invocation.getMatcher().group( group );
        JsonValue json = Json.valueOf( value );

        switch (json.getType())
        {
        case NULL:
            return null;
        case EMPTY:
            return converter.empty();
        default:
            return converter.read( json );
        }
    }

}
