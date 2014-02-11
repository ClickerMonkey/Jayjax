
package org.magnos.jayjax;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Part;



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
    
    
    
    
    /*
     * HELPER METHODS & CLASSES 
     */
    
    private boolean almostEquals(double a, double b) 
    {
        return Math.abs(a - b) < 0.00001;
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
