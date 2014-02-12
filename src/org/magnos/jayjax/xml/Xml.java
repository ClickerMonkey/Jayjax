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
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;


public class Xml
{

	private static final DocumentBuilderFactory documentBuilderFactory;
	private static final DocumentBuilder documentBuilder;
	
	private static final Set<String> TRUES = new HashSet<String>( Arrays.asList( "1", "t", "true", "y", "ya", "yes", "yessums" ) );
	private static final Set<String> FALSES = new HashSet<String>( Arrays.asList( "0", "f", "false", "n", "nah", "no", "nope" ) );
	
	static
	{
		try
		{
			documentBuilderFactory = DocumentBuilderFactory.newInstance();
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		}
		catch (ParserConfigurationException e)
		{
			throw new RuntimeException( e );
		}
	}

	public static Element load( InputStream in ) throws SAXException, IOException
	{
		return documentBuilder.parse( in ).getDocumentElement();
	}

	public static Map<String, String> getAttributes( Element e )
	{
		Map<String, String> attributes = new HashMap<String, String>();

		NamedNodeMap attrs = e.getAttributes();

		for (int i = 0; i < attrs.getLength(); i++)
		{
			Node a = attrs.item( i );
			attributes.put( a.getNodeName(), a.getNodeValue() );
		}

		return attributes;
	}

	public static RuntimeException unexpectedTag( Element parent, Element child )
	{
		return new RuntimeException( "Unexpected tag '" + child.getTagName() + "' in element '" + parent.getTagName() + "'" );
	}

	public static String getAttribute( Element e, String name, String defaultValue, boolean required )
	{
		String value = defaultValue;

		if (e.hasAttribute( name ))
		{
			value = e.getAttribute( name );

			if (value.trim().length() == 0)
			{
				value = null;
			}
		}

		if (value == null && required)
		{
			throw new NullPointerException( "Attribute " + name + " is required and missing from " + e.getTagName() );
		}

		return value;
	}

	public static Boolean getBooleanAttribute( Element e, String name, Boolean defaultValue, boolean required )
	{
		Boolean value = defaultValue;
		
		if (e.hasAttribute( name ))
		{
			value = parse( e.getAttribute( name ), name );
		}
		
		if (value == null && required)
		{
			throw new NullPointerException( "Attribute " + name + " is required and missing from " + e.getTagName() );
		}
		
		return value;
	}
	
	public static <T extends Enum<T>> T getEnumAttribute( Element e, String name, Class<T> enumClass, T defaultValue, boolean required )
	{
		T value = defaultValue;
		
		if (e.hasAttribute( name ))
		{
			value = Enum.valueOf( enumClass, e.getAttribute( name ) );
		}
		
		if (value == null && required)
		{
			throw new NullPointerException( "Attribute " + name + " is required and missing from " + e.getTagName() );
		}
		
		return value;
	}
	
	public static boolean parse( String value, String valueName )
	{
		if (value != null)
		{
			value = value.toLowerCase();

			if (TRUES.contains( value ))
			{
				return true;
			}

			if (FALSES.contains( value ))
			{
				return false;
			}
		}

		throw new RuntimeException( valueName + " was not a valid boolean value (" + value + "), acceptable trues: " + TRUES + ", acceptable falses: " + FALSES );
	}

	public static String[] split( String x )
	{
		if (x == null)
		{
			return null;
		}

		String[] split = x.split( "[, ]" );

		int nonZero = 0;

		for (int i = 0; i < split.length; i++)
		{
			split[i] = split[i].trim();

			if (split[i].length() > 0)
			{
				split[nonZero++] = split[i];
			}
		}

		return Arrays.copyOf( split, nonZero );
	}

}
