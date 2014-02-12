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

import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlIterator<T extends Node> implements Iterator<T>, Iterable<T>
{

	private final NodeList nodes;
	private int index = -1;
	private int next = 0;
	
	public XmlIterator( Element e )
	{
		this( e.getChildNodes() );
	}
	
	public XmlIterator( NodeList nodes) 
	{
		this.nodes = nodes;
		this.next = getNextNode( index );
	}

	@Override
	public Iterator<T> iterator()
	{
		return this;
	}
	
	@Override
	public boolean hasNext()
	{
		return next != -1;
	}

	@Override
	public T next()
	{
		index = next;
		next = getNextNode( index );
		
		return (T)nodes.item( index );
	}

	@Override
	public void remove()
	{
		throw new UnsupportedOperationException();
	}
	
	private int getNextNode(int i)
	{
		while (++i < nodes.getLength()) 
		{
			if (nodes.item(i).getNodeType() == Node.ELEMENT_NODE) 
			{
				return i;
			}
		}
		
		return -1;
	}

}