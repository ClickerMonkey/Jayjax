
package org.magnos.jayjax;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnos.jayjax.io.ArgumentResolver;
import org.magnos.jayjax.io.convert.JsonConverterFactory;
import org.magnos.jayjax.json.Json;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonObject;
import org.magnos.jayjax.json.JsonValue;


public class FunctionServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;

	@Override
	public void init() throws ServletException
	{
		JayJax.initialize( getServletContext() );
	}

	@Override
	protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		try
		{
			handle( request, response, Function.REQUEST_METHOD_POST );	
		}
		catch (ServletException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new ServletException( e );
		}
	}

	@Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		try
		{
			handle( request, response, Function.REQUEST_METHOD_GET );	
		}
		catch (ServletException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			throw e;
		}
		catch (Exception e)
		{
			throw new ServletException( e );
		}
	}

	protected void handle( HttpServletRequest request, HttpServletResponse response, int requestMethod ) throws Exception
	{
		String path = request.getPathInfo();
		JsonObject parameters = HttpJson.fromRequest( request );

		for (Function function : JayJax.getFunctions())
		{
			Matcher matcher = function.getAction().matcher( path );

			if (matcher.matches())
			{
				Invocation invocation = new Invocation();
				invocation.setFunction( function );
				invocation.setMatcher( matcher );
				invocation.setRequest( request );
				invocation.setResponse( response );
				invocation.setParameters( parameters );

				ArgumentResolver[] resolvers = function.getResolvers();

				Object[] arguments = new Object[resolvers.length];
				invocation.setArguments( arguments );

				for (int i = 0; i < resolvers.length; i++)
				{
					arguments[i] = resolvers[i].getArgument( invocation );
				}

				Object controller = JayJax.getControllerInstance( function.getController(), request );
				
				Method method = function.getMethod();
				
				Object result = method.invoke( controller, arguments );

				if (method.getReturnType() != Void.class)
				{
					if (result == null)
					{
						response.getOutputStream().print( Json.NULL );
					}
					else
					{
						JsonConverter<Object, JsonValue> converter = JsonConverterFactory.getConverter( (Class<Object>)result.getClass() );
						JsonValue json = converter.write( result );
						response.getOutputStream().print( json.toJson() );
					}
				}

				break;
			}
		}

	}

}
