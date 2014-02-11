
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

}
