package org.magnos.jayjax.io.resolve;

import java.io.IOException;

import javax.servlet.ServletException;

import org.magnos.jayjax.Invocation;
import org.magnos.jayjax.io.ArgumentResolver;

public class FileResolver extends ArgumentResolver
{

	public FileResolver( String name, Class<?> type )
	{
		super( name, type );
	}

	@Override
	public Object getArgument( Invocation invocation ) throws IOException, ServletException
	{
		return invocation.getRequest().getPart( name );
	}

}
