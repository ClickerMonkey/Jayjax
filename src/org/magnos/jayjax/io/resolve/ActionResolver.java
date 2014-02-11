
package org.magnos.jayjax.io.resolve;

import java.io.IOException;

import javax.servlet.ServletException;

import org.magnos.jayjax.Invocation;
import org.magnos.jayjax.io.ArgumentResolver;
import org.magnos.jayjax.io.convert.JsonConverterFactory;
import org.magnos.jayjax.json.Json;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonValue;


public class ActionResolver extends ArgumentResolver
{

	protected final int group;

	public ActionResolver( String name, Class<?> type, int group )
	{
		super( name, type );

		this.group = group;
	}

	@Override
	public Object getArgument( Invocation invocation ) throws IOException, ServletException
	{
		String value = invocation.getMatcher().group( group );
		JsonValue json = Json.valueOf( value );
		JsonConverter<Object, JsonValue> converter = JsonConverterFactory.getConverter( (Class<Object>)type );
		
		return converter.read( json );
	}
	
}
