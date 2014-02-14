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

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;
import org.magnos.jayjax.io.CharacterReader;

public class TestJsonFormat
{

    @Test
    public void testString() throws IOException
    {
        String json = "\"Hello World!\"";
        
        JsonValue value = Json.valueOf( json );
        
        assertEquals( JsonString.class, value.getClass() );
        assertEquals( json, value.toJson() );
    }
    
    @Test
    public void testInteger() throws IOException
    {
        String json = "3";
        
        JsonValue value = Json.valueOf( json );
        
        assertEquals( JsonNumber.class, value.getClass() );
        assertEquals( json, value.toJson() );
        assertEquals( 3, value.getObject() );
    }
    
    @Test
    public void testLong() throws IOException
    {
        String json = "354343453435";
        
        JsonValue value = Json.valueOf( json );
        
        assertEquals( JsonNumber.class, value.getClass() );
        assertEquals( json, value.toJson() );
        assertEquals( 354343453435L, value.getObject() );
    }
    
    @Test
    public void testFloat() throws IOException
    {
        String json = "-0.34";
        
        JsonValue value = Json.valueOf( json );
        
        assertEquals( JsonNumber.class, value.getClass() );
        assertEquals( json, value.toJson() );
        assertEquals( -0.34f, value.getObject() );
    }
    
    @Test
    public void testBoolean() throws IOException
    {
        String json = "true";
        
        JsonValue value = Json.valueOf( json );
        
        assertEquals( JsonBoolean.class, value.getClass() );
        assertEquals( json, value.toJson() );
        assertTrue( (Boolean)value.getObject() );
    }
    
    @Test
    public void testNull() throws IOException
    {
        String json = "null";
        
        JsonValue value = Json.valueOf( json );
        
        assertEquals( JsonNull.class, value.getClass() );
        assertEquals( json, value.toJson() );
        assertNull( value.getObject() );
    }
    
    @Test
    public void testArray() throws IOException
    {
        String json = "[ 3, 4,5,  true  ,  \"Hello World!\"       ]";
        String expd = "[3,4,5,true,\"Hello World!\"]";
        
        JsonValue value = Json.valueOf( json );

        assertEquals( JsonArray.class, value.getClass() );
        assertEquals( expd, value.toJson() );
        
        JsonArray array = (JsonArray)value;
        
        assertEquals( 3, array.get( 0 ) );
        assertEquals( 4, array.get( 1 ) );
        assertEquals( 5, array.get( 2 ) );
        assertEquals( true, array.get( 3 ) );
        assertEquals( "Hello World!", array.get( 4 ) );
    }
    
    @Test
    public void testObject() throws IOException
    {
        String json = "{\"hello\":4}";
        
        JsonValue value = Json.valueOf( json );

        assertEquals( JsonObject.class, value.getClass() );
        assertEquals( json, value.toJson() );
        
        JsonObject object = (JsonObject)value;
        
        assertEquals( 4, object.get( "hello" ) );
    }
    
    @Test
    public void testMultiple() throws IOException
    {
    	String json = "[1,2,3]{\"name\":true}";
    	CharacterReader reader = CharacterReader.forString( json );
    	
    	JsonValue a = Json.valueOf( reader );
    	JsonValue b = Json.valueOf( reader );
    	
    	System.out.println( a.toJson() );
    	System.out.println( b.toJson() );
    }
    
}
