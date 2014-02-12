/* 
 * NOTICE OF LICENSE
 * 
 * This source file is subject to the Open Software License (OSL 3.0) that is 
 * bundled with this package in the file LICENSE.txt. It is also available 
 * through the world-wide-web at http://opensource.org/licenses/osl-3.0.php
 * If you did not receive a copy of the license and are unable to obtain it 
 * through the world-wide-web, please send an email to magnos.software@gmail.com 
 * so we can send you a copy immediately. If you use any of this software please
 * notify me via our website or email, your feedback is much appreciated. 
 * 
 * @copyright   Copyright (c) 2011 Magnos Software (http://www.magnos.org)
 * @license     http://opensource.org/licenses/osl-3.0.php
 * 				Open Software License (OSL 3.0)
 */

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
