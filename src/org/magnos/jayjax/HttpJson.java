
package org.magnos.jayjax;

import java.io.IOException;
import java.io.StringReader;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.magnos.jayjax.io.CharacterReader;
import org.magnos.jayjax.io.CharacterSet;
import org.magnos.jayjax.json.JsonArray;
import org.magnos.jayjax.json.JsonBoolean;
import org.magnos.jayjax.json.JsonNumber;
import org.magnos.jayjax.json.JsonObject;
import org.magnos.jayjax.json.JsonString;
import org.magnos.jayjax.json.JsonValue;
import org.magnos.jayjax.json.JsonWriter;


public class HttpJson
{

    private static final CharacterSet SET_NAME_END = new CharacterSet( new char[][] { { '[' }, { '.' } } );
    private static final CharacterSet SET_ARRAY_END = new CharacterSet( new char[][] { {']'} } );
    private static final Pattern PATTERN_INDEX = Pattern.compile("\\d+");
    
    public static JsonObject fromRequest( HttpServletRequest request ) throws IOException
    {
        JsonObject json = new JsonObject();

        for (Entry<String, String[]> entry : request.getParameterMap().entrySet())
        {
            populateJsonValue( json, entry.getKey(), entry.getValue() );
        }

        return json;
    }
    
    private static void populateJsonValue(JsonValue destination, String name, String ... values) throws IOException
    {
        CharacterReader reader = new CharacterReader( new StringReader( name ) );
        
        populateJsonValue( destination, 0, reader.readUntil( SET_NAME_END, false, false, true ), reader, values );
    }
    
    private static JsonValue populateJsonValue(JsonValue destination, int arrayIndex, String property, CharacterReader reader, String ... values) throws IOException
    {
        JsonObject obj = null, subobj = null;
        JsonArray arr = null;
        
        int next = reader.readData();
        switch (next) 
        {
        // we hit the end of the reader, which signals a property
        case CharacterReader.NO_DATA:
            
            switch (destination.getType()) 
            {
            // the parent value is an array, add each value to it
            case Array:
                arr = (JsonArray)destination;
                for (String v : values) {
                    arr.set( arrayIndex++, fromString( v ) );
                }
                break;
            // the parent value is an object, set the property to the one 
            // value that was given, or set the property to the multiple
            // values that were given.
            case Object:
                obj = (JsonObject)destination;
                if (values.length == 1) {
                    obj.set( property, fromString( values[0] ) );
                } else if (values.length > 1) {
                    arr = new JsonArray();
                    for (String v : values) {
                        arr.set( arrayIndex++, fromString( v ) );
                    }
                    obj.set( property, arr );
                }
                break;
                
            // other types are not acceptable, data type promotion is not a feature that makes sense yet.
            default:
                throw new NullPointerException();
            }
            
            break;
        case '[':
            
            // get the key specified in the brackets
            String key = reader.readUntil( SET_ARRAY_END, false, false, false );
            // if the key is empty, this adds to an existing array on the parent object
            if (key.length() == 0) 
            {
                switch (destination.getType()) 
                {
                // the parent must be an object which has an array with the given name
                case Object:
                    obj = (JsonObject)destination;
                    arr = (JsonArray)obj.getValue( property );
                    if (arr == null) {
                        arr = new JsonArray();
                        obj.set( property, arr );
                    }
                    populateJsonValue( arr, arr.length(), null, reader, values );
                    break;
                    
                default:
                    throw new NullPointerException();
                }
            } 
            // if the key was an index...
            else if (PATTERN_INDEX.matcher( key ).matches()) 
            {
                int index = Integer.valueOf( key );
                switch (destination.getType()) 
                {
                case Object:
                    obj = (JsonObject)destination;
                    arr = (JsonArray)obj.getValue( property );
                    if (arr == null) {
                        arr = new JsonArray();
                        obj.set( property, arr );
                    }
                    populateJsonValue( arr, index, null, reader, values );
                    break;
                    
                default:
                    throw new NullPointerException();
                }
            }
            // if the key was a property name
            else 
            {
                switch (destination.getType()) 
                {
                case Object:
                    obj = (JsonObject)destination;
                    subobj = (JsonObject)obj.getValue( key );
                    if (subobj == null) {
                        subobj = new JsonObject();
                        obj.set( property, subobj );
                    }
                    populateJsonValue( subobj, 0, key, reader, values);
                    break;
                    
                default:
                    throw new NullPointerException();
                }
            }
            break;
            
        case '.':
            String subprops = reader.readUntil( SET_NAME_END, false, false, true );
            
            switch (destination.getType()) 
            {
            case Object:
                obj = (JsonObject)destination;
                subobj = obj.getValue( property, JsonObject.class );
                if (subobj == null) {
                    subobj = new JsonObject();
                    obj.set( property, subobj );
                }
                populateJsonValue( subobj, 0, subprops, reader, values);
                break;
                
            case Array:
                arr = (JsonArray)destination;
                subobj = null;
                if (arrayIndex >= arr.length()) {
                    subobj = new JsonObject();
                    arr.set( arrayIndex, subobj );
                } else {
                    subobj = arr.getValue( arrayIndex, JsonObject.class );
                    if (subobj == null) {
                        subobj = new JsonObject();
                        arr.set( arrayIndex, subobj );
                    }
                }
                populateJsonValue( subobj, 0, subprops, reader, values);
                break;
                
            default:
                throw new NullPointerException();
            }
            break;
        }
        
        return destination;
    }
    
    private static JsonValue fromString(String x)
    {
        JsonValue value = JsonBoolean.fromString( x );
        
        if (value == null)
        {
            value = JsonNumber.fromString( x );
            
            if (value == null)
            {
                value = new JsonString( x );
            }
        }
        
        return value;
    }
    
    public static void main(String[] args) throws IOException
    {
        JsonValue json = new JsonObject();
        populateJsonValue(json, "name1", "1");
        populateJsonValue(json, "name2[]", "2");
        populateJsonValue(json, "name3[meow]", "3");
        populateJsonValue(json, "name4[1]", "4");
        populateJsonValue(json, "name4[5]", "4.5");
        populateJsonValue(json, "name5.hello", "5");
        populateJsonValue(json, "name6.hello.world", "6");
        populateJsonValue(json, "contact[0].firstName","Philip");
        populateJsonValue(json, "contact[0].lastName", "Diffenderfer");
        populateJsonValue(json, "contact[1].firstName", "John");
        populateJsonValue(json, "contact[1].lastName", "Doe");
        populateJsonValue(json, "contact[0].movies[]", "Fellowship of The Ring");
        populateJsonValue(json, "contact[0].movies[]", "Two Towers");
        populateJsonValue(json, "contact[0].children[0].name", "Connor");
        populateJsonValue(json, "contact[0].children[0].age", "3");
        populateJsonValue(json, "contact[0].children[1].name", "Mackenzie");
        populateJsonValue(json, "contact[0].children[1].age", "4");
        
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
