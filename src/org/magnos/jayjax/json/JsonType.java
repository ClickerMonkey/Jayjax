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

/**
 * The different types of values in JSON.
 * 
 * @author Philip Diffenderfer
 * 
 */
public enum JsonType
{
	/**
	 * A map of values by name.
	 */
	OBJECT,
	/**
	 * An indexed based array of values.
	 */
	ARRAY,
	/**
	 * A String value.
	 */
	STRING,
	/**
	 * A NULL value with an unknown type.
	 */
	NULL,
	/**
	 * A signed whole or decimal number.
	 */
	NUMBER,
	/**
	 * A true or false value.
	 */
	BOOLEAN,
	/**
	 * No value was specified. This is not a valid JSON value, but is required.
	 */
	EMPTY
}