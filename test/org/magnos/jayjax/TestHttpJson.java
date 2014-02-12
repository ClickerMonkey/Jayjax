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

import java.io.IOException;

import org.junit.Test;
import org.magnos.jayjax.json.JsonObject;
import org.magnos.jayjax.json.JsonValue;
import org.magnos.jayjax.json.JsonWriter;


public class TestHttpJson
{
	@Test
	public void test() throws IOException
	{
		JsonValue json = new JsonObject();
		HttpJson.populateJsonValue( json, "name1", "1" );
		HttpJson.populateJsonValue( json, "name2[]", "2" );
		HttpJson.populateJsonValue( json, "name3[meow]", "3" );
		HttpJson.populateJsonValue( json, "name4[1]", "4" );
		HttpJson.populateJsonValue( json, "name4[5]", "4.5" );
		HttpJson.populateJsonValue( json, "name5.hello", "5" );
		HttpJson.populateJsonValue( json, "name6.hello.world", "6" );
		HttpJson.populateJsonValue( json, "contact[0].firstName", "Philip" );
		HttpJson.populateJsonValue( json, "contact[0].lastName", "Diffenderfer" );
		HttpJson.populateJsonValue( json, "contact[1].firstName", "John" );
		HttpJson.populateJsonValue( json, "contact[1].lastName", "Doe" );
		HttpJson.populateJsonValue( json, "contact[0].movies[]", "Fellowship of The Ring" );
		HttpJson.populateJsonValue( json, "contact[0].movies[]", "Two Towers" );
		HttpJson.populateJsonValue( json, "contact[0].children[0].name", "Connor" );
		HttpJson.populateJsonValue( json, "contact[0].children[0].age", "3" );
		HttpJson.populateJsonValue( json, "contact[0].children[1].name", "Mackenzie" );
		HttpJson.populateJsonValue( json, "contact[0].children[1].age", "4" );

		StringBuilder out = new StringBuilder();
		JsonWriter writer = JsonWriter.forAppender( out );
		writer.setIndent( true );
		writer.setIndentation( "  " );
		writer.setNewlineArrayStart( false );
		writer.setNewlineMemberSeparator( true );
		writer.setNewlineObjectStart( true );
		writer.setNewlineObjectEnd( true );
		writer.setStringsQuoted( false );
		json.write( writer );

		System.out.println( out );
	}
}
