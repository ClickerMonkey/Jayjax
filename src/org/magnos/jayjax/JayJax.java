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

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.magnos.jayjax.xml.XmlLoader;


public class Jayjax
{

	public static final String CONFIGURATION_FILE = "/WEB-INF/jayjax.xml";
	
	private static Pattern functionPrefixPattern;
	private static final List<Function> functions = new ArrayList<Function>();
	private static final ConcurrentHashMap<String, List<Function>> functionPrefixMap = new ConcurrentHashMap<String, List<Function>>();
	
	private static final ConcurrentHashMap<String, Controller> controllers = new ConcurrentHashMap<String, Controller>();
	private static final ConcurrentHashMap<String, Object> controllerCacheApplication = new ConcurrentHashMap<String, Object>();
	private static final ConcurrentHashMap<String, ThreadLocal<Object>> controllerCacheThread = new ConcurrentHashMap<String, ThreadLocal<Object>>();

	private static final ThreadLocal<Invocation> invocationLocal = new ThreadLocal<Invocation>();
	
	private static final List<JayjaxListener> listenerList = new ArrayList<JayjaxListener>();
	
	private static volatile boolean loaded;
	
	public static synchronized void initialize(ServletContext context) throws ServletException
	{
		if (!loaded)
		{
			try
			{
				InputStream in = context.getResourceAsStream( CONFIGURATION_FILE );

				try
				{
					XmlLoader.load( in );
				}
				finally
				{
					in.close();
				}
				
				prepareFunctions();
				
				loaded = true;
			}
			catch (Exception e)
			{
				throw new ServletException( e );
			}
		}
	}
	
	public static Controller getController( String name )
	{
		return controllers.get( name );
	}
	
	public static void addController( final Controller controller )
	{
		for (Function f : controller.getFunctions())
		{
			List<Function> similar = functionPrefixMap.get( f.getActionPrefix() );
			
			if (similar == null)
			{
				similar = new ArrayList<Function>();
				functionPrefixMap.put( f.getActionPrefix(), similar );
			}
			
			similar.add( f );
			
			functions.add( f );
		}
		
		controllers.put( controller.getName(), controller );
		
		switch (controller.getScope()) 
		{
		case APPLICATION:
		    controllerCacheApplication.put( controller.getName(), controller.getInstance() );
		    break;
		    
		case THREAD:
		    controllerCacheThread.put( controller.getName(), new ThreadLocal<Object>() {
	            protected Object initialValue()
	            {
	                return controller.getInstance();
	            }
	        } );
		    break;
		    
		default:
		    break; // nothing
		}
	}

	public static Object getControllerInstance( Controller controller, HttpServletRequest request )
	{
		switch (controller.getScope())
		{
		case APPLICATION:
			return controllerCacheApplication.get( controller.getName() );
			
		case THREAD:
			return controllerCacheThread.get( controller.getName() ).get();
			
		case SESSION:
			HttpSession session = request.getSession( true );
			Object instance = session.getAttribute( controller.getSessionName() );
			if (instance == null)
			{
				instance = controller.getInstance();
				session.setAttribute( controller.getSessionName(), instance );
			}
			return instance;
			
		case REQUEST:
			return controller.getInstance();
		}

		return null;
	}

	public static void prepareFunctions()
	{
		Set<String> prefixes = new HashSet<String>();
		
		for (Function f : functions)
		{
			prefixes.add( f.getActionPrefix() );
		}
		
		List<String> prefixesOrdered = new ArrayList<String>();
		prefixesOrdered.addAll( prefixes );
		Collections.sort( prefixesOrdered, new Comparator<String>() {
			public int compare( String a, String b ) {
				return b.length() - a.length();
			}
		});
		
		StringBuilder sb = new StringBuilder();
		sb.append( "(" );
		
		for (String prefix : prefixesOrdered)
		{
			if (sb.length() > 1)
			{
				sb.append( "|" );
			}
			
			sb.append( prefix );
		}
		
		sb.append( ")" );
		
		functionPrefixPattern = Pattern.compile( sb.toString() );
	}
	
	public static Function getFunction(String url)
	{
		Matcher matchPrefix = functionPrefixPattern.matcher( url );
		
		if (!matchPrefix.find())
		{
			System.out.println( url );
			System.out.println( functionPrefixPattern.pattern() );
			
			return null;
		}
		
		List<Function> functions = functionPrefixMap.get( matchPrefix.group() );
		
		if (functions.size() == 1)
		{
			return functions.get( 0 );
		}
		
		for (Function f : functions)
		{
			Matcher matchFull = f.getAction().matcher( url );
			
			if (matchFull.matches())
			{
				return f;
			}
		}
		
		return null;
	}
	

	public static List<Function> getFunctions()
	{
		return functions;
	}
	
	public static Invocation newInvocation()
	{
		Invocation invocation = new Invocation();
		
	    invocationLocal.set( invocation );
	    
	    return invocation;
	}
	
	public static void clearInvocation()
	{
		invocationLocal.remove();
	}
	
	public static Invocation getInvocation()
	{
	    return invocationLocal.get();
	}
    
    public static <T> T getSession(String name)
    {
        return (T) invocationLocal.get().getRequest().getSession( true ).getAttribute( name );
    }
    
    public static <T> T getSession(String name, Class<T> type)
    {
        Object value = invocationLocal.get().getRequest().getSession( true ).getAttribute( name );
        
        if (value == null || value.getClass() != type)
        {
            return null;
        }
        
        return (T)value;
    }
    
    public static void setSession(String name, Object value)
    {
        invocationLocal.get().getRequest().getSession( true ).setAttribute( name, value );
    }
    
    public static boolean hasSession()
    {
        return invocationLocal.get() != null;
    }
    
    public static void addListener(JayjaxListener listener)
    {
    	listenerList.add( listener );
    }
    
    public static void removeListener(JayjaxListener listener)
    {
    	listenerList.remove( listener );
    }
    
    public static void notifyMessage(JayjaxMessage message, Throwable e) throws JayjaxException
    {
    	Invocation invocation = getInvocation();
    	
    	if (listenerList.isEmpty())
    	{
    		throw new RuntimeException( message.name(), e );
    	}
    	
		for (JayjaxListener listener : listenerList)
    	{
			try
	    	{
				listener.onMessage( message, invocation, e );
	    	}
	    	catch (Throwable ex)
	    	{
	    		listener.onMessage( JayjaxMessage.LISTENER_ERROR, invocation, ex );
	    	}
    	}	
    	
    	throw new JayjaxException();
    }

}
