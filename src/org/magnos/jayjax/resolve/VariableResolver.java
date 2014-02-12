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

package org.magnos.jayjax.resolve;

import java.io.IOException;
import java.nio.file.attribute.UserPrincipal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.AsyncContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.magnos.jayjax.ArgumentResolver;
import org.magnos.jayjax.Invocation;
import org.magnos.jayjax.json.JsonObject;
import org.magnos.jayjax.json.JsonValue;


public class VariableResolver
{

    private static final Class<?>[] STRING_TYPES = { String.class, StringBuilder.class, StringBuffer.class, CharSequence.class, Appendable.class };
    private static final Pattern PATTERN_SESSION = Pattern.compile( "\\$session\\[([^\\]]+)\\]" );
    private static final Pattern PATTERN_COOKIE = Pattern.compile( "\\$cookie\\[([^\\d]+)\\]" );

    public static ArgumentResolver getResolver( String variable, Class<?> type )
    {
    	String lower = variable.toLowerCase();
    	
        if (lower.equals("$request")) {
            ensureTypes( variable, type, ServletRequest.class, HttpServletRequest.class );
            return new ArgumentResolver( variable, type ) {
                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest();
                }
            };
        } else if (lower.equals("$response")) {
            ensureTypes( variable, type, ServletResponse.class, HttpServletResponse.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getResponse();
                }
            };
    	} else if (lower.equals("$session")) {
            ensureTypes( variable, type, HttpSession.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getSession( true );
                }
            };
    	} else if (lower.equals("$data")) {
            ensureTypes( variable, type, JsonValue.class, JsonObject.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getParameters();
                }
            };
    	} else if (lower.equals("$async")) {
            ensureTypes( variable, type, AsyncContext.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getAsyncContext();
                }
            };
    	} else if (lower.equals("$authType")) {
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getAuthType();
                }
            };
    	} else if (lower.equals("$contentType")) {
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getContentType();
                }
            };
    	} else if (lower.equals("$cookies")) {
            ensureTypes( variable, type, Cookie[].class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getCookies();
                }
            };
    	} else if (lower.equals("$method")) {
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getMethod();
                }
            };
    	} else if (lower.equals("$pathInfo")) {
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getPathInfo();
                }
            };
    	} else if (lower.equals("$pathTranslated")) {
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getPathTranslated();
                }
            };
    	} else if (lower.equals("$protocol")) {
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getProtocol();
                }
            };
    	} else if (lower.equals("$query")) {
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getQueryString();
                }
            };
    	} else if (lower.equals("$uri")) {
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getRequestURI();
                }
            };
    	} else if (lower.equals("$scheme")) {
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getScheme();
                }
            };
    	} else if (lower.equals("$serverName")) {
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getServerName();
                }
            };
    	} else if (lower.equals("$serverPort")) {
            ensureTypes( variable, type, Integer.class, int.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getServerPort();
                }
            };
    	} else if (lower.equals("$servletContext")) {
            ensureTypes( variable, type, ServletContext.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getServletContext();
                }
            };
    	} else if (lower.equals("$servletPath")) {
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getServletPath();
                }
            };
    	} else if (lower.equals("$principal")) {
            ensureTypes( variable, type, UserPrincipal.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getUserPrincipal();
                }
            };
        }

        Matcher sessionMatcher = PATTERN_SESSION.matcher( variable );
        if (sessionMatcher.matches())
        {
            final String sessionName = sessionMatcher.group( 1 );

            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getSession( true ).getAttribute( sessionName );
                }
            };
        }

        Matcher cookieMatcher = PATTERN_COOKIE.matcher( variable );
        if (cookieMatcher.matches())
        {
            final int cookieIndex = Integer.valueOf( cookieMatcher.group( 1 ) );

            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getCookies()[cookieIndex];
                }
            };
        }

        throw new RuntimeException( "The variable " + variable + " with expected type " + type + " is not an acceptable variable" );
    }

    private static void ensureTypes( String variable, Class<?> type, Class<?>... expectedTypes )
    {
        for (int i = 0; i < expectedTypes.length; i++)
        {
            if (type == expectedTypes[i])
            {
                return;
            }
        }

        throw new RuntimeException( "The variable " + variable + " has an unexpected type " + type.getSimpleName() );
    }

}
