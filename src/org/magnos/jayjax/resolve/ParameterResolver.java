
package org.magnos.jayjax.resolve;

import java.io.IOException;

import javax.servlet.ServletException;

import org.magnos.jayjax.ArgumentResolver;
import org.magnos.jayjax.Invocation;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonValue;
import org.magnos.jayjax.json.convert.JsonConverterFactory;


public class ParameterResolver extends ArgumentResolver
{

    protected final JsonConverter<Object, JsonValue> converter;

    public ParameterResolver( String name, Class<?> type )
    {
        super( name, type );

        this.converter = JsonConverterFactory.getConverter( (Class<Object>)type );
    }

    @Override
    public Object getArgument( Invocation invocation ) throws IOException, ServletException
    {
        JsonValue json = invocation.getParameters().getValue( name );

        if (json == null)
        {
            return converter.missing();
        }

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
