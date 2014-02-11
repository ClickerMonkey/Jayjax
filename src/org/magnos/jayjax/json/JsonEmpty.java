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

package org.magnos.jayjax.json;

import java.io.IOException;

/**
 * An empty value of unknown type.
 * 
 * @author Philip Diffenderfer
 *
 */
public class JsonEmpty implements JsonValue
{

	/**
	 * We only need one instance of this object.
	 */
	public static final JsonEmpty INSTANCE = new JsonEmpty();

	@Override
	public Object getObject()
	{
		return Json.EMPTY;
	}

	@Override
	public JsonType getType()
	{
		return JsonType.EMPTY;
	}

	@Override
	public String toJson()
	{
		return Json.EMPTY;
	}

	@Override
	public void write( JsonWriter out ) throws IOException
	{
	}

}