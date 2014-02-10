
package org.magnos.jayjax;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@MultipartConfig
@WebServlet(urlPatterns="/custom", displayName="Jayjax Servlet", name="jayjax")
public class JayjaxServlet extends HttpServlet
{

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        String description = request.getParameter( "description" );
        Part filePart = request.getPart( "file" );
        String fileName = filePart.getSubmittedFileName();
        String fileContent = toString( filePart.getInputStream(), filePart.getSize() );

        response.getOutputStream().print( String.format( "Description: %s, File (%s): %s\n", description, fileName, fileContent ) );
    }

    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        
    }
    
    public String toString( InputStream in, long size ) throws IOException
    {
        StringBuilder sb = new StringBuilder( (int)size );
        in = new BufferedInputStream( in );
        for (int b = in.read(); b != -1; b = in.read())
            sb.append( (char)b );
        return sb.toString();
    }

}
