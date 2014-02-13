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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.Part;

import org.magnos.jayjax.json.JsonObject;
import org.magnos.jayjax.json.JsonType;
import org.magnos.jayjax.json.JsonValue;



public class TestController
{
    
    public TestController()
    {
        Jayjax.setSession( "message", "WHAT'S GOING ON?" );    
    }

    // Test.testString('Hello World!', null, '', ['a', 'b', 'c'], [], null);
    public boolean testString(String a, String b, String c, String[] d, String[] e, String[] f) 
    {
        return a.equals( "Hello World!" ) &&
               b == null && 
               c == null && 
               Arrays.equals( d, new String[] {"a", "b", "c"} ) &&
               e != null && e.length == 0 && 
               f == null;
    }
    
    // Test.testEnum('APPLICATION', null, ['REQUEST','SESSION'], [], null);
    public boolean testEnum(ControllerScope a, ControllerScope b, ControllerScope[] c, ControllerScope[] d, ControllerScope[] e) 
    {
        return a == ControllerScope.APPLICATION &&
               b == null && 
               Arrays.equals( c, new ControllerScope[] {ControllerScope.REQUEST, ControllerScope.SESSION} ) &&
               d != null && d.length == 0 &&
               e == null;
    }
    
    // Test.testByte(-128, 127, 0, 45, null, [1,2,3], [], null);
    public boolean testByte(byte a, byte b, byte c, byte d, Byte e, byte[] f, byte[] g, byte[] h) 
    { 
        return a == Byte.MIN_VALUE && 
               b == Byte.MAX_VALUE && 
               c == 0 && 
               d == 45 && 
               e == null && 
               Arrays.equals( f, new byte[] {1, 2, 3} ) &&
               g != null && g.length == 0 &&
               h == null;
    }
    
    // Test.testShort(-32768, 32767, 0, 45, null, [1,2,3], [], null);
    public boolean testShort(short a, short b, short c, short d, Short e, short[] f, short[] g, short[] h) 
    { 
        return a == Short.MIN_VALUE && 
               b == Short.MAX_VALUE && 
               c == 0 && 
               d == 45 && 
               e == null && 
               Arrays.equals( f, new short[] {1, 2, 3} ) &&
               g != null && g.length == 0 &&
               h == null;
    }
    
    // Test.testInt(-2147483648, 2147483647, 0, 45, null, [1,2,3], [], null);
    public boolean testInt(int a, int b, int c, int d, Integer e, int[] f, int[] g, int[] h) 
    { 
        return a == Integer.MIN_VALUE && 
               b == Integer.MAX_VALUE && 
               c == 0 && 
               d == 45 && 
               e == null && 
               Arrays.equals( f, new int[] {1, 2, 3} ) &&
               g != null && g.length == 0 &&
               h == null;
    }
    
    // Test.testLong(-2147483649, 2147483648, 0, 45, null, [1,2,3], [], null);
    public boolean testLong(long a, long b, long c, long d, Long e, long[] f, long[] g, long[] h) 
    { 
        return a == Integer.MIN_VALUE - 1L && 
               b == Integer.MAX_VALUE + 1L && 
               c == 0 && 
               d == 45 && 
               e == null && 
               Arrays.equals( f, new long[] {1, 2, 3} ) &&
               g != null && g.length == 0 &&
               h == null;
    }
    
    // Test.testBoolean(true, false, null, [true, false, true], [], null);
    public boolean testBoolean(boolean a, boolean b, Boolean e, boolean[] f, boolean[] g, boolean[] h) 
    { 
        return a == true && 
               b == false && 
               e == null && 
               Arrays.equals( f, new boolean[] {true, false, true} ) &&
               g != null && g.length == 0 &&
               h == null;
    }
    
    // Test.testChar(0, 65535, 'a', 'b', null, [1,2,3], [], null);
    public boolean testChar(char a, char b, char c, char d, Character e, char[] f, char[] g, char[] h) 
    { 
        return a == Character.MIN_VALUE && 
               b == Character.MAX_VALUE && 
               c == 'a' && 
               d == 'b' && 
               e == null && 
               Arrays.equals( f, new char[] {1, 2, 3} ) &&
               g != null && g.length == 0 &&
               h == null;
    }
    
    // Test.testFloat(0.125, -0.125, 345.04296875, -345.04296875, null, [0.5,0.25,0.125], [], null);
    public boolean testFloat(float a, float b, float c, float d, Float e, float[] f, float[] g, float[] h) 
    { 
        return a == 0.125f && 
               b == -0.125f && 
               almostEquals( c, 345.04296875f ) && 
               almostEquals( d, -345.04296875f ) && 
               e == null && 
               Arrays.equals( f, new float[] {0.5f, 0.25f, 0.125f} ) &&
               g != null && g.length == 0 &&
               h == null;
    }
    
    // Test.testDouble(0.125, -0.125, 345.04296875, -345.04296875, null, [0.5,0.25,0.125], [], null);
    public boolean testDouble(double a, double b, double c, double d, Double e, double[] f, double[] g, double[] h) 
    { 
        return a == 0.125 && 
               b == -0.125 && 
               almostEquals( c, 345.04296875 ) && 
               almostEquals( d, -345.04296875 ) && 
               e == null && 
               Arrays.equals( f, new double[] {0.5, 0.25, 0.125} ) &&
               g != null && g.length == 0 &&
               h == null;
    }
    
    // Test.testAddContacts([{
    //       fullName:'Philip Diffenderfer',
    //       address:{houseNumber:12}
    //  },{
    //       fullName:'John Smith',
    //       address:{houseNumber:34}
    //  }]);
    public int testAddContacts(Contact[] contact) 
    {
        int total = 0;
        for (Contact c : contact) {
            total += c.address.houseNumber;
        }
        return total;
    }
    
    // Test.testGetContact('John Smith', '(123) 456-7890', 123, '579 Simple Street', 'Main City', 'AR', 17555);
    public List<Contact> testGetContact(String fullName, String phoneNumber, int houseNumber, String street, String city, String state, int zip) 
    {
        List<Contact> contacts = new ArrayList<Contact>();
        
        Contact c = new Contact();
        c.fullName = fullName;
        c.phoneNumber = phoneNumber;
        c.address = new Address();
        c.address.houseNumber = houseNumber;
        c.address.street = street;
        c.address.city = city;
        c.address.state = state;
        c.address.zip = zip;
        contacts.add( c );
        
        contacts.add( new Contact() );
        
        return contacts;
    }
    
    // /test/testSingleUpload
    public String testSingleUpload(Part uploadedFile) throws IOException
    {
        InputStream in = new BufferedInputStream( uploadedFile.getInputStream() );
        
        StringBuilder content = new StringBuilder();
        
        for (int data = in.read(); data != -1; data = in.read()) 
        {
            content.append( (char)data );
        }
        
        return content.toString();
    }
    
    // /test/testMultipleUpload
    public String testMultipleUpload(Part[] uploadedFile) throws IOException
    {
        StringBuilder content = new StringBuilder();
        
        for (int i = 0; i < uploadedFile.length; i++)
        {
            if (i > 0)
            {
                content.append( "\n" );
            }
            
            content.append( testSingleUpload( uploadedFile[i] ) );
        }
        
        return content.toString();
    }
    
    // Test.testSessionVariable();
    public boolean testSessionVariable(String message)
    {
        return message != null && message.equals( "WHAT'S GOING ON?" );
    }
    
    // GET /test/testURL/3452/Hello+World/true
    // Test.testURL(3452, 'Hello World', true);
    public boolean testURL(long id, String message, boolean flag)
    {
        return id == 3452L && message.equals( "Hello World" ) && flag == true;
    }
    
    // Test.testDate(new Date(), null, [new Date(),newDate()], [], null);
    public boolean testDate(Date a, Date b, Date[] c, Date[] d, Date[] e)
    {
    	Date now = new Date();
    	
    	return a != null && sameSecond(a, now) &&
    	       b == null && 
    	       c != null && c.length == 2 && c[0] != null && c[1] != null &&  sameSecond(c[0], now) && sameSecond(c[1], now) &&
    	       d != null && d.length == 0 &&
    	       e == null;
    }
    
    // Test.testTime(new Date(), null, [new Date(),newDate()], [], null);
    public boolean testTime(Time a, Time b, Time[] c, Time[] d, Time[] e)
    {
    	Date now = new Date();
    	
    	return a != null && sameSecond(a, now) &&
    	       b == null && 
    	       c != null && c.length == 2 && c[0] != null && c[1] != null &&  sameSecond(c[0], now) && sameSecond(c[1], now) &&
    	       d != null && d.length == 0 &&
    	       e == null;
    }
    
    // Test.testTimestamp(new Date(), null, [new Date(),newDate()], [], null);
    public boolean testTimestamp(Timestamp a, Timestamp b, Timestamp[] c, Timestamp[] d, Timestamp[] e)
    {
    	Date now = new Date();
    	
    	return a != null && sameSecond(a, now) &&
    	       b == null && 
    	       c != null && c.length == 2 && c[0] != null && c[1] != null &&  sameSecond(c[0], now) && sameSecond(c[1], now) &&
    	       d != null && d.length == 0 &&
    	       e == null;
    }
    
    // Test.testSqlDate(new Date(), null, [new Date(),newDate()], [], null);
    public boolean testSqlDate(java.sql.Date a, java.sql.Date b, java.sql.Date[] c, java.sql.Date[] d, java.sql.Date[] e)
    {
    	Date now = new Date();
    	
    	return a != null && sameSecond(a, now) &&
    	       b == null && 
    	       c != null && c.length == 2 && c[0] != null && c[1] != null &&  sameSecond(c[0], now) && sameSecond(c[1], now) &&
    	       d != null && d.length == 0 &&
    	       e == null;
    }
    
    // Test.testCalendar(new Date(), null, [new Date(),newDate()], [], null);
    public boolean testCalendar(Calendar a, Calendar b, Calendar[] c, Calendar[] d, Calendar[] e)
    {
    	Date now = new Date();
    	
    	return a != null && sameSecond(a.getTime(), now) &&
    	       b == null && 
    	       c != null && c.length == 2 && c[0] != null && c[1] != null &&  sameSecond(c[0].getTime(), now) && sameSecond(c[1].getTime(), now) &&
    	       d != null && d.length == 0 &&
    	       e == null;
    }
    
    // Test.testUUID('91b694a0-3f83-4c1c-b998-2351705c002c', null);
    public boolean testUUID(UUID a, UUID b)
    {
    	return a != null && a.equals( UUID.fromString( "91b694a0-3f83-4c1c-b998-2351705c002c" ) ) &&
    		   b == null;
    }
    
    // Test.testClass('java.lang.Integer', null);
    public Class<?> testClass(Class<?> a, Class<?> b)
    {
    	return (a == Integer.class && b == null) ? Long.class : null;
    }
    
    // Test.testJson({message:'Hello World',success:true});
    public boolean testJson(JsonValue json)
    {
    	return json != null && 
    	       json.getType() == JsonType.OBJECT &&
    	       ((JsonObject)json).has( "message" ) &&
    	       ((JsonObject)json).has( "success" ) &&
    	       ((JsonObject)json).get( "message" ).equals( "Hello World" ) &&
    	       ((JsonObject)json).get( "success" ).equals( Boolean.TRUE );
    }
    
    // Test.testMap({a:true,b:'Hi',c:4.5}, {}, null);
    public boolean testMap(Map<String, String> a, Map<String, String> b, Map<String, String> c)
    {
    	return a != null && a.size() == 3 && 
    	       a.get( "a" ).equals( "true" ) &&
    	       a.get( "b" ).equals( "Hi" ) &&
    	       a.get( "c" ).equals( "4.5" ) &&
    	       b != null && b.size() == 0 &&
    	       c == null;
    }

    // *NOT A JAVASCRIPT TEST*
    public boolean testCheckbox(boolean a, boolean b, boolean c)
    {
    	return a == true &&
    	       b == false &&
    	       c == false;
    }
    
    
    /*
     * HELPER METHODS & CLASSES 
     */
    
    private boolean almostEquals(double a, double b) 
    {
        return Math.abs(a - b) < 0.00001;
    }
    
    private boolean sameSecond(Date a, Date b)
    {
    	return Math.abs(a.getTime() - b.getTime()) < 60000;
    }

    public static class Contact 
    {
        public String fullName;
        public String phoneNumber;
        public Address address;
    }
    
    public static class Address 
    {
        public int houseNumber;
        public String street;
        public String city;
        public String state;
        public int zip;
    }
    
}
