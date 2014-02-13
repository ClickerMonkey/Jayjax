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

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.regex.Matcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnos.jayjax.json.Json;
import org.magnos.jayjax.json.JsonConverter;
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
		catch (JayjaxException e)
		{
			// only thrown after a listener is notified of issue
		}
		catch (ServletException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			throw e;
		}
		catch (Throwable e)
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
		catch (JayjaxException e)
		{
			// only thrown after a listener is notified of issue
		}
		catch (ServletException e)
		{
			throw e;
		}
		catch (IOException e)
		{
			throw e;
		}
		catch (Throwable e)
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
		Invocation invocation = new Invocation();
		invocation.setRequest( request );
		invocation.setResponse( response );
		Jayjax.setInvocation( invocation );
		
		String path = request.getPathInfo();
		
		try
		{
			invocation.setParameters( HttpJson.fromRequest( request ) );	
		}
		catch (Throwable e)
		{
			Jayjax.notifyMessage( JayjaxMessage.PARSE_REQUEST_VALUES_ERROR, e );
		}
		
		for (Function function : Jayjax.getFunctions())
		{
			Matcher matcher = function.getAction().matcher( path );

			if (matcher.matches())
			{
				invocation.setMatcher( matcher );
				invocation.setFunction( function );
				
			    if (function.isSecure() && !request.isSecure())
			    {
			    	Jayjax.notifyMessage( JayjaxMessage.SECURE_FUNCTION_UNSECURE_CONNECTION, null );
			    }
			    
			    if ((function.getRequestMethods() & requestMethod) == 0)
			    {
			    	Jayjax.notifyMessage( JayjaxMessage.WRONG_METHOD_FOR_FUNCTION, null );
			    }

				ArgumentResolver[] resolvers = function.getResolvers();

				Object[] arguments = new Object[resolvers.length];
				invocation.setArguments( arguments );
				
				try
				{
					for (int i = 0; i < resolvers.length; i++)
					{
						arguments[i] = resolvers[i].getArgument( invocation );
					}	
				}
				catch (Throwable e)
				{
			    	Jayjax.notifyMessage( JayjaxMessage.BUILD_FUNCTION_ARGUMENTS_ERROR, e );
				}

                if (function.getValidator() != null)
                {
                	try
                	{
                		if (!function.getValidator().isValid( invocation ))
                        {
                            return;
                        }	
                	}
                	catch (Throwable e)
                	{
                		Jayjax.notifyMessage( JayjaxMessage.VALIDATION_ERROR, e );	
                	}
                }
				
                Object controller = null;
                
                try
                {
                	controller = Jayjax.getControllerInstance( function.getController(), request );	
                }
                catch (Throwable e)
                {
                	Jayjax.notifyMessage( JayjaxMessage.CONTROLLER_INSTANTIATION_ERROR, e );
                }
				
				Method method = function.getMethod();
				
				Object result = null; 
				
				try
				{
					result = method.invoke( controller, arguments );	
				}
				catch (Throwable e)
				{
					Jayjax.notifyMessage( JayjaxMessage.FUNCTION_INVOCATION_ERROR, e );
				}
				
				invocation.setResult( result );
				
				if (method.getReturnType() != Void.class)
				{
                    response.setContentType( CONTENT_TYPE );
                    
					if (result == null)
					{
						try
						{
						    response.setContentLength( Json.NULL.length() );
							response.getOutputStream().print( Json.NULL );
						}
						catch (Throwable e)
						{
							Jayjax.notifyMessage( JayjaxMessage.RESPONSE_WRITING_ERROR, e );
						}
					}
					else
					{
						String jsonString = "";
						
						try
						{
							JsonConverter<Object, JsonValue> converter = JsonConverterFactory.getConverter( (Class<Object>)result.getClass() );
							JsonValue json = converter.write( result );
							jsonString = json.toJson();	
						}
						catch (Throwable e)
						{
							Jayjax.notifyMessage( JayjaxMessage.SERIALIZATION_ERROR, e );
						}

						try
						{
							response.setContentLength( jsonString.length() );
							response.getOutputStream().print( jsonString );	
						}
						catch (Throwable e)
						{
							Jayjax.notifyMessage( JayjaxMessage.RESPONSE_WRITING_ERROR, e );
						}
					}
				}

				return;
			}
		}
		
		Jayjax.notifyMessage( JayjaxMessage.ACTION_DOES_NOT_MAP_TO_FUNCTION, null );
	}

}
