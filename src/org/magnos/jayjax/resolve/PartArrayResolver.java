package org.magnos.jayjax.resolve;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.Part;

import org.magnos.jayjax.ArgumentResolver;
import org.magnos.jayjax.Invocation;

public class PartArrayResolver extends ArgumentResolver
{

	public PartArrayResolver( String name, Class<?> type )
	{
		super( name, type );
	}

	@Override
	public Object getArgument( Invocation invocation ) throws IOException, ServletException
	{
	    Collection<Part> parts = invocation.getRequest().getParts(); 
	    
	    return parts.toArray( new Part[ parts.size() ] );
	}

}
