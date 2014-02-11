
package org.magnos.jayjax.xml;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Part;

import org.magnos.jayjax.Controller;
import org.magnos.jayjax.ControllerScope;
import org.magnos.jayjax.Function;
import org.magnos.jayjax.JayJax;
import org.magnos.jayjax.io.ArgumentResolver;
import org.magnos.jayjax.io.resolve.ActionResolver;
import org.magnos.jayjax.io.resolve.FileResolver;
import org.magnos.jayjax.io.resolve.ParameterResolver;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XmlLoader
{

	private static final String TAG_JAYJAX = "jayjax";
	private static final String TAG_CONTROLLER = "controller";
	private static final String TAG_FUNCTION = "function";

	private static final Pattern PATTERN_INVOKE = Pattern.compile( "([^\\(]+)\\(([^\\)]*)\\)" );

	public static void load( InputStream in ) throws SAXException, IOException, NoSuchMethodException, SecurityException, ClassNotFoundException
	{
		Element root = Xml.load( in );

		String rootTag = root.getTagName().toLowerCase();

		if (!rootTag.equals( TAG_JAYJAX ))
		{
			throw new RuntimeException( "Document root of a jayjax XML file must be jayjax" );
		}

		XmlIterator<Element> children = new XmlIterator<Element>( root );

		for (Element e : children)
		{
			String tag = e.getTagName().toLowerCase();

			if (tag.equals( TAG_CONTROLLER ))
			{
				loadController( e );
			}
			else
			{
				throw Xml.unexpectedTag( root, e );
			}
		}
	}

	private static void loadController( Element e ) throws NoSuchMethodException, SecurityException, ClassNotFoundException
	{
		Controller controller = new Controller();
		controller.setName( Xml.getAttribute( e, "name", null, true ) );
		controller.setSessionName( Xml.getAttribute( e, "session-name", controller.getName(), true ) );
		controller.setScope( Xml.getEnumAttribute( e, "scope", ControllerScope.class, ControllerScope.REQUEST, true ) );
		controller.setJavascript( Xml.getBooleanAttribute( e, "javascript", false, true ) );
		controller.setControllerClass( Class.forName( Xml.getAttribute( e, "class", null, true ) ) );
		controller.setFunctions( new ArrayList<Function>() );

		XmlIterator<Element> children = new XmlIterator<Element>( e );

		for (Element child : children)
		{
			String tag = child.getTagName().toLowerCase();

			if (tag.equals( TAG_FUNCTION ))
			{
				controller.getFunctions().add( loadFunction( child, controller ) );
			}
			else
			{
				throw Xml.unexpectedTag( e, child );
			}
		}

		JayJax.addController( controller );
	}

	private static Function loadFunction( Element e, Controller controller )
	{
		Function f = new Function();

		f.setController( controller );
		f.setGivenAction( Xml.getAttribute( e, "action", null, true ) );
		f.setAction( Pattern.compile( f.getGivenAction() ) );

		f.setGivenMethod( Xml.getAttribute( e, "method", null, true ) );
		f.setRequestMethods(
			(f.getGivenMethod().contains( "GET" ) ? Function.REQUEST_METHOD_GET : 0) |
				(f.getGivenMethod().contains( "POST" ) ? Function.REQUEST_METHOD_POST : 0)
			);

		f.setSecure( Xml.getBooleanAttribute( e, "secure", false, true ) );

		String invokeString = Xml.getAttribute( e, "invoke", null, true );
		Matcher invokeMatcher = PATTERN_INVOKE.matcher( invokeString );

		if (!invokeMatcher.matches())
		{
			throw new RuntimeException( "invoke attribute does not have valid syntax: " + invokeString );
		}

		String methodName = invokeMatcher.group( 1 );
		String[] methodArgumentNames = Xml.split( invokeMatcher.group( 2 ) );
		Method method = findMethod( methodName, controller );
		Class<?>[] parameters = method.getParameterTypes();
		ArgumentResolver[] resolvers = new ArgumentResolver[methodArgumentNames.length];

		for (int i = 0; i < methodArgumentNames.length; i++)
		{
			String name = methodArgumentNames[i];
			Class<?> type = parameters[i];

			if (name.startsWith( "#" ))
			{
				resolvers[i] = new ActionResolver( name, type, Integer.valueOf( name.substring( 1 ) ) );
			}
			else
			{
				if (type == Part.class)
				{
					resolvers[i] = new FileResolver( name, type );
				}
				else
				{
					resolvers[i] = new ParameterResolver( name, type );
				}
			}
		}

		f.setGivenInvoke( invokeString );
		f.setMethod( method );
		f.setParameters( parameters );
		f.setResolvers( resolvers );

		return f;
	}

	private static Method findMethod( String methodName, Controller controller )
	{
		for (Method m : controller.getControllerClass().getMethods())
		{
			if (m.getName().equalsIgnoreCase( methodName ))
			{
				return m;
			}
		}

		return null;
	}

}
