
package org.magnos.jayjax;

import java.util.Arrays;

import javax.servlet.http.Part;


public class UserController
{
	
	public static class User {
		public long id;
		public String name;
	}
	
	public static class UploadResult {
		public boolean success;
		public String error;
		public String filename;
		public String description;
		public String tags;
		public String[] names;
	}

	public User getUser( long id )
	{
		User u = new User();
		u.id = id;
		u.name = "ClickerMonkey";
		return u;
	}

	public UploadResult setProfilePicture( Part uploadedFile, String description, String tags, String[] names )
	{
		UploadResult result = new UploadResult();
		result.error = null;
		result.success = true;
		result.filename = uploadedFile.getSubmittedFileName();
		result.description = description;
		result.tags = tags;
		result.names = names;
		return result;
	}
	
	public int[] complexMethod(String[] words, User user, boolean flag)
	{
		System.out.println( "ENTER complexMethod" );
		System.out.println( Arrays.toString( words ) );
		System.out.println( user.id );
		System.out.println( user.name );
		System.out.println( flag );
		
		return new int[] {1, 2, 3};
	}

}
