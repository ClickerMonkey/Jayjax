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
        HttpJson.populateJsonValue(json, "name1", "1");
        HttpJson.populateJsonValue(json, "name2[]", "2");
        HttpJson.populateJsonValue(json, "name3[meow]", "3");
        HttpJson.populateJsonValue(json, "name4[1]", "4");
        HttpJson.populateJsonValue(json, "name4[5]", "4.5");
        HttpJson.populateJsonValue(json, "name5.hello", "5");
        HttpJson.populateJsonValue(json, "name6.hello.world", "6");
        HttpJson.populateJsonValue(json, "contact[0].firstName","Philip");
        HttpJson.populateJsonValue(json, "contact[0].lastName", "Diffenderfer");
        HttpJson.populateJsonValue(json, "contact[1].firstName", "John");
        HttpJson.populateJsonValue(json, "contact[1].lastName", "Doe");
        HttpJson.populateJsonValue(json, "contact[0].movies[]", "Fellowship of The Ring");
        HttpJson.populateJsonValue(json, "contact[0].movies[]", "Two Towers");
        HttpJson.populateJsonValue(json, "contact[0].children[0].name", "Connor");
        HttpJson.populateJsonValue(json, "contact[0].children[0].age", "3");
        HttpJson.populateJsonValue(json, "contact[0].children[1].name", "Mackenzie");
        HttpJson.populateJsonValue(json, "contact[0].children[1].age", "4");
        
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
