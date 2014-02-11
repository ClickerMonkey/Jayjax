
package org.magnos.jayjax;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnos.jayjax.json.Json;
import org.magnos.jayjax.json.JsonConverter;
import org.magnos.jayjax.json.JsonObject;
import org.magnos.jayjax.json.JsonValue;
import org.magnos.jayjax.json.convert.JsonConverterFactory;


public class FunctionServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "application/json";
	
	@Override
	public void init() throws ServletException
	{
		Jayjax.initialize( getServletContext() );
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
		finally
		{
		    Jayjax.setInvocation( null );
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
		finally
		{
		    Jayjax.setInvocation( null );
		}
	}

	protected void handle( HttpServletRequest request, HttpServletResponse response, int requestMethod ) throws Exception
	{
		String path = request.getPathInfo();
		JsonObject parameters = HttpJson.fromRequest( request );

		for (Function function : Jayjax.getFunctions())
		{
			Matcher matcher = function.getAction().matcher( path );

			if (matcher.matches())
			{
			    if (function.isSecure() && !request.isSecure())
			    {
			        throw new ServletException( "The invoked function is marked secure only, but the connection is not secure" );
			    }
			    
			    if ((function.getRequestMethods() & requestMethod) == 0)
			    {
			        throw new ServletException( "The invoked function is not valid for method " + request.getMethod() );
			    }
			    
				Invocation invocation = new Invocation();
				invocation.setFunction( function );
				invocation.setMatcher( matcher );
				invocation.setRequest( request );
				invocation.setResponse( response );
				invocation.setParameters( parameters );

				ArgumentResolver[] resolvers = function.getResolvers();

				Object[] arguments = new Object[resolvers.length];
				invocation.setArguments( arguments );
				
				Jayjax.setInvocation( invocation );

				for (int i = 0; i < resolvers.length; i++)
				{
					arguments[i] = resolvers[i].getArgument( invocation );
				}

                if (function.getValidator() != null)
                {
                    if (!function.getValidator().isValid( invocation ))
                    {
                        return;
                    }
                }
				
				Object controller = Jayjax.getControllerInstance( function.getController(), request );
				
				Method method = function.getMethod();
				
				Object result = method.invoke( controller, arguments );
				invocation.setResult( result );
				
				if (method.getReturnType() != Void.class)
				{
                    response.setContentType( CONTENT_TYPE );
                    
					if (result == null)
					{
					    response.setContentLength( Json.NULL.length() );
						response.getOutputStream().print( Json.NULL );
					}
					else
					{
						JsonConverter<Object, JsonValue> converter = JsonConverterFactory.getConverter( (Class<Object>)result.getClass() );
						JsonValue json = converter.write( result );
						String jsonString = json.toJson();
						
						response.setContentLength( jsonString.length() );
						response.getOutputStream().print( jsonString );
					}
				}

				break;
			}
		}
	}

}
