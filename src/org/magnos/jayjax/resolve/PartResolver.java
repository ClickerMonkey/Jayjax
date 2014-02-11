package org.magnos.jayjax.resolve;

import java.io.IOException;

import javax.servlet.ServletException;

import org.magnos.jayjax.ArgumentResolver;
import org.magnos.jayjax.Invocation;

public class PartResolver extends ArgumentResolver
{

	public PartResolver( String name, Class<?> type )
	{
		super( name, type );
	}

	@Override
	public Object getArgument( Invocation invocation ) throws IOException, ServletException
	{
		return invocation.getRequest().getPart( name );
	}

}
