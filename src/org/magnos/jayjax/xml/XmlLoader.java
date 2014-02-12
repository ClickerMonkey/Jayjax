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

package org.magnos.jayjax.xml;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Part;

import org.magnos.jayjax.ArgumentResolver;
import org.magnos.jayjax.Controller;
import org.magnos.jayjax.ControllerScope;
import org.magnos.jayjax.Function;
import org.magnos.jayjax.Jayjax;
import org.magnos.jayjax.Validator;
import org.magnos.jayjax.resolve.ActionResolver;
import org.magnos.jayjax.resolve.ParameterResolver;
import org.magnos.jayjax.resolve.PartArrayResolver;
import org.magnos.jayjax.resolve.PartResolver;
import org.magnos.jayjax.resolve.VariableResolver;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class XmlLoader
{

	private static final String TAG_JAYJAX = "jayjax";
	private static final String TAG_VALIDATOR = "validator";
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
		Map<String, Validator> validatorMap = new HashMap<String, Validator>();
		
		for (Element e : children)
		{
			String tag = e.getTagName().toLowerCase();

			if (tag.equals( TAG_CONTROLLER ))
			{
				loadController( e, validatorMap );
			}
			else if (tag.equals( TAG_VALIDATOR ))
			{
			    loadValidator( e, validatorMap );
			}
			else
			{
				throw Xml.unexpectedTag( root, e );
			}
		}
	}

	private static void loadController( Element e, Map<String, Validator> validatorMap ) throws NoSuchMethodException, SecurityException, ClassNotFoundException
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
				controller.getFunctions().add( loadFunction( child, controller, validatorMap ) );
			}
			else
			{
				throw Xml.unexpectedTag( e, child );
			}
		}

		Jayjax.addController( controller );
	}

	private static Function loadFunction( Element e, Controller controller, Map<String, Validator> validatorMap )
	{
		Function f = new Function();

		f.setController( controller );
		
		// Action
		f.setGivenAction( Xml.getAttribute( e, "action", null, true ) );
		f.setAction( Pattern.compile( f.getGivenAction() ) );

		// Method
		f.setGivenMethod( Xml.getAttribute( e, "method", null, true ) );
		f.setRequestMethods(
			(f.getGivenMethod().contains( "GET" ) ? Function.REQUEST_METHOD_GET : 0) |
			(f.getGivenMethod().contains( "POST" ) ? Function.REQUEST_METHOD_POST : 0)
		);

		// Secure
		f.setSecure( Xml.getBooleanAttribute( e, "secure", false, true ) );
		
		// Javascript
		f.setJavascript( Xml.getBooleanAttribute( e, "javascript", true, true ) );
		
		// Validator
		String validatorName = Xml.getAttribute( e, "validator", null, false );
		
		if (validatorName != null) 
		{
		    Validator validator = validatorMap.get( validatorName );
		    
		    if (validator == null) 
		    {
		        throw new RuntimeException( "Validator with name " + validatorName + " was not found!" );
		    }
		    
		    f.setGivenValidator( validatorName );
		    f.setValidator( validator );
		}

		// Invoke, Method, Parameters, & Resolvers
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
			else if (name.startsWith( "$" ))
			{
			    resolvers[i] = VariableResolver.getResolver( name, type );
			}
			else
			{
				if (type == Part.class)
				{
					resolvers[i] = new PartResolver( name, type );
				}
				else if (type == Part[].class)
				{
				    resolvers[i] = new PartArrayResolver( name, type );
				}
				else
				{
					resolvers[i] = new ParameterResolver( name, type );
				}
			}
		}

		f.setGivenInvoke( invokeString );
		f.setMethod( method );
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
	
	private static void loadValidator( Element e, Map<String, Validator> validatorMap )
	{
	    String name = Xml.getAttribute( e, "name", null, true );
	    String className = Xml.getAttribute( e, "class", null, true );
	    
	    try
	    {
	        Validator validator = (Validator)Class.forName( className ).newInstance();
	        
	        validatorMap.put( name, validator );
	    }
	    catch (Exception ex)
	    {
	        throw new RuntimeException( ex );
	    }
	}

}
