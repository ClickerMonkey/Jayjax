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
package org.magnos.jayjax.resolve;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.Part;

import org.magnos.jayjax.ArgumentResolver;
import org.magnos.jayjax.Invocation;

public class PartArrayResolver extends ArgumentResolver
{

	public PartArrayResolver( String name, Class<?> type )
	{
		super( name, type );
	}

	@Override
	public Object getArgument( Invocation invocation ) throws IOException, ServletException
	{
	    Collection<Part> parts = invocation.getRequest().getParts(); 
	    
	    return parts.toArray( new Part[ parts.size() ] );
	}

}
