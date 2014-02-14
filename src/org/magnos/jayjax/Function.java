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

import java.lang.reflect.Method;
import java.util.regex.Pattern;



public class Function
{
	public static final int REQUEST_METHOD_GET = 1 << 0;
	public static final int REQUEST_METHOD_POST = 1 << 1;

	private String givenAction;
	private String givenMethod;
	private String givenInvoke;
	private String givenValidator;

	private Controller controller;
	private Pattern action;
	private String actionPrefix;
	private Method method;
	private ArgumentResolver[] resolvers;
	private Validator validator;
	private boolean secure;
	private boolean javascript;
	private int requestMethods;

	public String getGivenAction()
	{
		return givenAction;
	}

	public void setGivenAction( String givenAction )
	{
		this.givenAction = givenAction;
	}

	public String getGivenMethod()
	{
		return givenMethod;
	}

	public void setGivenMethod( String givenMethod )
	{
		this.givenMethod = givenMethod;
	}

	public String getGivenInvoke()
	{
		return givenInvoke;
	}

	public void setGivenInvoke( String givenInvoke )
	{
		this.givenInvoke = givenInvoke;
	}

	public String getGivenValidator()
	{
		return givenValidator;
	}

	public void setGivenValidator( String givenValidator )
	{
		this.givenValidator = givenValidator;
	}

	public Controller getController()
	{
		return controller;
	}

	public void setController( Controller controller )
	{
		this.controller = controller;
	}

	public Method getMethod()
	{
		return method;
	}

	public void setMethod( Method method )
	{
		this.method = method;
	}

	public ArgumentResolver[] getResolvers()
	{
		return resolvers;
	}

	public void setResolvers( ArgumentResolver[] resolvers )
	{
		this.resolvers = resolvers;
	}

	public Validator getValidator()
	{
		return validator;
	}

	public void setValidator( Validator validator )
	{
		this.validator = validator;
	}

	public boolean isSecure()
	{
		return secure;
	}

	public void setSecure( boolean secure )
	{
		this.secure = secure;
	}
	
    public boolean isJavascript()
    {
        return javascript;
    }

    public void setJavascript( boolean javascript )
    {
        this.javascript = javascript;
    }

    public int getRequestMethods()
	{
		return requestMethods;
	}

	public void setRequestMethods( int requestMethods )
	{
		this.requestMethods = requestMethods;
	}

	public Pattern getAction()
	{
		return action;
	}

	public void setAction( Pattern action )
	{
		this.action = action;
	}

	public String getActionPrefix()
	{
		return actionPrefix;
	}

	public void setActionPrefix( String actionPrefix )
	{
		this.actionPrefix = actionPrefix;
	}
	
}
