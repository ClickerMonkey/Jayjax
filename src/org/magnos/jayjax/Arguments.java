
package org.magnos.jayjax;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;


public class Arguments
{

    public static Object resolve( HttpServletRequest request, HttpServletResponse response, String name, Class<?> expectedType ) throws IOException
    {
        if (name.startsWith( "$" ))
        {
            Object resolved = handleSpecialArgument( request, response, name );

            if (resolved == null)
            {
                throw new NullPointerException();
            }

            if (resolved.getClass() != expectedType)
            {
                throw new ClassCastException();
            }

            return resolved;
        }

        try
        {
            Part part = request.getPart( name );

            if (part != null)
            {
                if (expectedType != Part.class)
                {
                    throw new ClassCastException();
                }

                return part;
            }
        }
        catch (ServletException e)
        {
            // ignore
        }

        return request.getParameter( name );
    }

    private static Object handleSpecialArgument( HttpServletRequest request, HttpServletResponse response, String name )
    {
        if (name.equals( "$request" ))
        {
            return request;
        }
        if (name.equals( "$response" ))
        {
            return response;
        }
        if (name.equals( "$session" ))
        {
            return request.getSession();
        }
        if (name.equals( "$authType" ))
        {
            return request.getAuthType();
        }

        return null;
    }

}
