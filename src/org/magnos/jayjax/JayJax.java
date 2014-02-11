
package org.magnos.jayjax;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.magnos.jayjax.xml.XmlLoader;


public class JayJax
{

	private static final List<Function> functions = new ArrayList<Function>();
	private static final ConcurrentHashMap<String, Controller> controllers = new ConcurrentHashMap<String, Controller>();
	private static final ConcurrentHashMap<String, Object> controllerCacheApplication = new ConcurrentHashMap<String, Object>();
	private static final ConcurrentHashMap<String, ThreadLocal<Object>> controllerCacheThread = new ConcurrentHashMap<String, ThreadLocal<Object>>();

	private static volatile boolean loaded;
	
	public static synchronized void initialize(ServletContext context) throws ServletException
	{
		if (!loaded)
		{
			try
			{
				InputStream in = context.getResourceAsStream( "/WEB-INF/jayjax.xml" );
				
				try
				{
					XmlLoader.load( in );
				}
				finally
				{
					in.close();
				}
				
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
		functions.addAll( controller.getFunctions() );
		controllers.put( controller.getName(), controller );
		controllerCacheThread.put( controller.getName(), new ThreadLocal<Object>() {
			protected Object initialValue()
			{
				return controller.getInstance();
			}
		} );
		controllerCacheApplication.put( controller.getName(), controller.getInstance() );
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

	public static List<Function> getFunctions()
	{
		return functions;
	}

}
