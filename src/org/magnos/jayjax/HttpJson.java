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
import java.io.StringReader;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.magnos.jayjax.io.CharacterReader;
import org.magnos.jayjax.io.CharacterSet;
import org.magnos.jayjax.json.Json;
import org.magnos.jayjax.json.JsonArray;
import org.magnos.jayjax.json.JsonEmpty;
import org.magnos.jayjax.json.JsonNull;
import org.magnos.jayjax.json.JsonObject;
import org.magnos.jayjax.json.JsonValue;


public class HttpJson
{

    private static final CharacterSet SET_NAME_END = new CharacterSet( new char[][] { { '[' }, { '.' } } );
    private static final CharacterSet SET_ARRAY_END = new CharacterSet( new char[][] { { ']' } } );
    private static final Pattern PATTERN_INDEX = Pattern.compile( "\\d+" );

    public static JsonObject fromRequest( HttpServletRequest request ) throws IOException
    {
        JsonObject json = new JsonObject();

        for (Entry<String, String[]> entry : request.getParameterMap().entrySet())
        {
            populateJsonValue( json, entry.getKey(), entry.getValue() );
        }

        return json;
    }

    protected static void populateJsonValue( JsonValue destination, String name, String... values ) throws IOException
    {
        CharacterReader reader = new CharacterReader( new StringReader( name ) );

        populateJsonValue( destination, 0, reader.readUntil( SET_NAME_END, false, false, true ), reader, values );
    }

    private static JsonValue populateJsonValue( JsonValue destination, int arrayIndex, String property, CharacterReader reader, String... values ) throws IOException
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
            case ARRAY:
                arr = (JsonArray)destination;
                for (String v : values)
                {
                    arr.set( arrayIndex++, parseValue( v ) );
                }
                break;
            // the parent value is an object, set the property to the one 
            // value that was given, or set the property to the multiple
            // values that were given.
            case OBJECT:
                obj = (JsonObject)destination;
                if (values.length == 1)
                {
                    obj.set( property, parseValue( values[0] ) );
                }
                else if (values.length > 1)
                {
                    arr = new JsonArray();
                    for (String v : values)
                    {
                        arr.set( arrayIndex++, parseValue( v ) );
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
                case OBJECT:
                    obj = (JsonObject)destination;
                    arr = (JsonArray)obj.getValue( property );
                    if (arr == null)
                    {
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
                case OBJECT:
                    obj = (JsonObject)destination;
                    arr = (JsonArray)obj.getValue( property );
                    if (arr == null)
                    {
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
                case OBJECT:
                    obj = (JsonObject)destination;
                    subobj = (JsonObject)obj.getValue( property );
                    if (subobj == null)
                    {
                        subobj = new JsonObject();
                        obj.set( property, subobj );
                    }
                    populateJsonValue( subobj, 0, key, reader, values );
                    break;
                    
                case ARRAY:
                    arr = (JsonArray)destination;
                    subobj = arrayIndex >= arr.length() ? null : arr.getValue( arrayIndex, JsonObject.class );
                    if (subobj == null)
                    {
                        subobj = new JsonObject();
                        arr.set( arrayIndex, subobj );
                    }
                    populateJsonValue( subobj, 0, key, reader, values );
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
            case OBJECT:
                obj = (JsonObject)destination;
                subobj = obj.getValue( property, JsonObject.class );
                if (subobj == null)
                {
                    subobj = new JsonObject();
                    obj.set( property, subobj );
                }
                populateJsonValue( subobj, 0, subprops, reader, values );
                break;

            case ARRAY:
                arr = (JsonArray)destination;
                subobj = arrayIndex >= arr.length() ? null : arr.getValue( arrayIndex, JsonObject.class );
                if (subobj == null)
                {
                    subobj = new JsonObject();
                    arr.set( arrayIndex, subobj );
                }
                populateJsonValue( subobj, 0, subprops, reader, values );
                break;

            default:
                throw new NullPointerException();
            }
            break;
        }

        return destination;
    }

    private static JsonValue parseValue( String x ) throws IOException
    {
        if (x == null || x.equals( Json.NULL ))
        {
            return JsonNull.INSTANCE;
        }
        
        if (x.isEmpty())
        {
            return JsonEmpty.INSTANCE;
        }
        
        return Json.valueOf( x );
    }

}
