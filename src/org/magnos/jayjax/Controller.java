
package org.magnos.jayjax;

import java.lang.reflect.Constructor;
import java.util.List;


public class Controller
{

	private static final Class<?>[] NO_PARAMETERS = {};
	private static final Object[] NO_ARGUMENTS = {};

	public String name;
	public String sessionName;
	public boolean javascript;
	public Class<?> controllerClass;
	public Constructor<?> constructor;
	public ControllerScope scope = ControllerScope.REQUEST;
	public List<Function> functions;

	public Object getInstance()
	{
		try
		{
			return constructor.newInstance( NO_ARGUMENTS );	
		}
		catch (Exception e)
		{
			throw new RuntimeException( e );
		}
	}
	
	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}
	
	public String getSessionName()
	{
		return sessionName;
	}

	public void setSessionName( String sessionName )
	{
		this.sessionName = sessionName;
	}

	public boolean isJavascript()
	{
		return javascript;
	}

	public void setJavascript( boolean javascript )
	{
		this.javascript = javascript;
	}

	public Class<?> getControllerClass()
	{
		return controllerClass;
	}

	public void setControllerClass( Class<?> controllerClass ) throws NoSuchMethodException, SecurityException
	{
		this.controllerClass = controllerClass;
		this.constructor = controllerClass.getConstructor( NO_PARAMETERS );
	}

	public ControllerScope getScope()
	{
		return scope;
	}

	public void setScope( ControllerScope scope )
	{
		this.scope = scope;
	}

	public Constructor<?> getConstructor()
	{
		return constructor;
	}

	public List<Function> getFunctions()
	{
		return functions;
	}

	public void setFunctions( List<Function> functions )
	{
		this.functions = functions;
	}

}
