
package org.magnos.jayjax;

import java.io.IOException;

import javax.servlet.ServletException;



public abstract class ArgumentResolver
{

	protected final String name;
	protected final Class<?> type;

	public ArgumentResolver( String name, Class<?> type )
	{
		this.name = name;
		this.type = type;
	}

	public abstract Object getArgument( Invocation invocation ) throws IOException, ServletException;

	public String getName()
	{
		return name;
	}

    public Class<?> getType()
    {
        return type;
    }
	
}
