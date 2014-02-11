
package org.magnos.jayjax;

import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnos.jayjax.json.JsonObject;


public class Invocation
{

	private Function function;
	private Matcher matcher;
	private Object[] arguments;
	private JsonObject parameters;
	private HttpServletRequest request;
	private HttpServletResponse response;

	public Function getFunction()
	{
		return function;
	}

	public void setFunction( Function function )
	{
		this.function = function;
	}

	public Matcher getMatcher()
	{
		return matcher;
	}

	public void setMatcher( Matcher matcher )
	{
		this.matcher = matcher;
	}

	public Object[] getArguments()
	{
		return arguments;
	}

	public void setArguments( Object[] arguments )
	{
		this.arguments = arguments;
	}

	public JsonObject getParameters()
	{
		return parameters;
	}

	public void setParameters( JsonObject parameters )
	{
		this.parameters = parameters;
	}

	public HttpServletRequest getRequest()
	{
		return request;
	}

	public void setRequest( HttpServletRequest request )
	{
		this.request = request;
	}

	public HttpServletResponse getResponse()
	{
		return response;
	}

	public void setResponse( HttpServletResponse response )
	{
		this.response = response;
	}

}
