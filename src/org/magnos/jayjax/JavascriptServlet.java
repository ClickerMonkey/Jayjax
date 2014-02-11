
package org.magnos.jayjax;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnos.jayjax.io.ArgumentResolver;
import org.magnos.jayjax.io.resolve.ParameterResolver;


public class JavascriptServlet extends HttpServlet
{

	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "application/javascript";
	private ConcurrentHashMap<String, JavascriptFile> javascriptCache = new ConcurrentHashMap<String, JavascriptFile>();

	class JavascriptFile
	{
		String contents;
		long timestamp;
	}

	@Override
	public void init() throws ServletException
	{
		JayJax.initialize( getServletContext() );
	}

	@Override
	protected long getLastModified( HttpServletRequest request )
	{
		String controllerName = request.getPathInfo().substring( 1 );
		JavascriptFile file = javascriptCache.get( controllerName );

		return file == null ? -1L : file.timestamp;
	}

	@Override
	protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
	{
		String controllerName = request.getPathInfo().substring( 1 );
		JavascriptFile cached = javascriptCache.get( controllerName );

		if (cached == null)
		{
			cached = buildControllerJavascript( controllerName );

			if (cached != null)
			{
				javascriptCache.put( controllerName, cached );
			}
		}

		if (cached == null)
		{
			response.setStatus( HttpURLConnection.HTTP_NOT_FOUND );
		}
		else
		{
			response.setContentType( CONTENT_TYPE );
			response.getOutputStream().print( cached.contents );
		}
	}

	protected JavascriptFile buildControllerJavascript( String name )
	{
		Controller controller = JayJax.getController( name );

		if (controller == null || !controller.isJavascript())
		{
			return null;
		}

		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		PrintStream out = new PrintStream( stream );

		out.format( "var %s = %s || {};\n", name, name );

		for (Function f : controller.getFunctions())
		{
			out.format( "%s.%s = function(%ssettings) {\n", name, f.getMethod().getName(), getArgumentList( f ) );
			out.format( "  settings.url = '/jayjax/jj%s';\n", getURL( f ) );
			out.format( "  settings.dataType = 'json';\n" );
			out.format( "  settings.type = '%s';\n", f.getGivenMethod() );
			out.format( "  settings.data = {%s};\n", getArgumentData( f ) );
			out.format( "  $.ajax(settings);\n" );
			out.format( "};\n" );
		}

		JavascriptFile file = new JavascriptFile();
		file.contents = stream.toString();
		file.timestamp = System.currentTimeMillis();
		return file;
	}

	protected String getURL( Function f )
	{
		String action = f.getGivenAction();

		if (action.indexOf( '(' ) == -1)
		{
			return action;
		}

		ArgumentResolver[] resolvers = f.getResolvers();
		List<String> split = groupSplit( action );

		StringBuilder sb = new StringBuilder();
		sb.append( split.get( 0 ) );

		for (int i = 1; i < split.size(); i += 2)
		{
			String resolverName = split.get( i );

			int resolverIndex = -1;
			for (int k = 0; k < resolvers.length; k++)
			{
				if (resolvers[k].getName().equals( resolverName ))
				{
					resolverIndex = k;
					break;
				}
			}

			sb.append( "'+a" ).append( resolverIndex ).append( "+'" );
			sb.append( split.get( i + 1 ) );
		}

		return sb.toString();
	}

	protected List<String> groupSplit( String pattern )
	{
		List<String> groups = new ArrayList<String>();

		int start = next( 0, pattern );
		int depth = 0;
		int opens = 0;
		int lastEnd = 0;

		while (start != -1)
		{
			char c = pattern.charAt( start );
			if (c == '(')
			{
				if (depth == 0)
				{
					groups.add( pattern.substring( lastEnd, start ) );
				}
				opens++;
				depth++;
			}
			else if (c == ')')
			{
				depth--;
				if (depth == 0)
				{
					lastEnd = start + 1;
				}
			}
			if (depth == 0)
			{
				groups.add( "#" + opens );
			}
			start = next( start + 1, pattern );
		}

		groups.add( pattern.substring( lastEnd ) );

		return groups;
	}

	protected int next( int start, String x )
	{
		int a = nextChar( start, '(', x );
		int b = nextChar( start, ')', x );

		return (a == -1 ? b : (b == -1 ? a : Math.min( a, b )));
	}

	protected int nextChar( int start, char needle, String haystack )
	{
		int i = haystack.indexOf( needle, start );

		while (i != -1 && (i == 0 || haystack.charAt( i - 1 ) == '\\'))
		{
			i = haystack.indexOf( needle, i + 1 );
		}

		return i;
	}

	protected String getArgumentList( Function f )
	{
		StringBuilder sb = new StringBuilder();

		ArgumentResolver[] resolvers = f.getResolvers();

		for (int i = 0; i < resolvers.length; i++)
		{
			sb.append( 'a' ).append( i ).append( ',' );
		}

		return sb.toString();
	}

	protected String getArgumentData( Function f )
	{
		StringBuilder sb = new StringBuilder();

		ArgumentResolver[] resolvers = f.getResolvers();

		for (int i = 0; i < resolvers.length; i++)
		{
			ArgumentResolver ar = resolvers[i];

			if (ar instanceof ParameterResolver)
			{
				if (sb.length() > 0)
				{
					sb.append( ',' );
				}

				sb.append( ar.getName() ).append( ':' ).append( 'a' ).append( i );
			}
		}

		return sb.toString();
	}

}
