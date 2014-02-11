package org.magnos.jayjax.io.resolve;

import java.io.IOException;

import javax.servlet.ServletException;

import org.magnos.jayjax.Invocation;
import org.magnos.jayjax.io.ArgumentResolver;
import org.magnos.jayjax.io.convert.JsonConverterFactory;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonValue;

public class ParameterResolver extends ArgumentResolver
{

	public ParameterResolver( String name, Class<?> type )
	{
		super( name, type );
	}

	@Override
	public Object getArgument( Invocation invocation ) throws IOException, ServletException
	{
		JsonValue json = invocation.getParameters().getValue( name );
		JsonConverter<Object, JsonValue> converter = JsonConverterFactory.getConverter( (Class<Object>)type );
		
		return converter.read( json );
	}

}
