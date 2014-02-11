
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
        switch (variable)
        {
        case "$request":
            ensureTypes( variable, type, ServletRequest.class, HttpServletRequest.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest();
                }
            };
        case "$response":
            ensureTypes( variable, type, ServletResponse.class, HttpServletResponse.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getResponse();
                }
            };
        case "$session":
            ensureTypes( variable, type, HttpSession.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getSession( true );
                }
            };
        case "$data":
            ensureTypes( variable, type, JsonValue.class, JsonObject.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getParameters();
                }
            };
        case "$async":
            ensureTypes( variable, type, AsyncContext.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getAsyncContext();
                }
            };
        case "$authType":
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getAuthType();
                }
            };
        case "$contentType":
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getContentType();
                }
            };
        case "$cookies":
            ensureTypes( variable, type, Cookie[].class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getCookies();
                }
            };
        case "$method":
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getMethod();
                }
            };
        case "$pathInfo":
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getPathInfo();
                }
            };
        case "$pathTranslated":
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getPathTranslated();
                }
            };
        case "$protocol":
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getProtocol();
                }
            };
        case "$query":
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getQueryString();
                }
            };
        case "$uri":
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getRequestURI();
                }
            };
        case "$scheme":
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getScheme();
                }
            };
        case "$serverName":
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getServerName();
                }
            };
        case "$serverPort":
            ensureTypes( variable, type, Integer.class, int.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getServerPort();
                }
            };
        case "$servletContext":
            ensureTypes( variable, type, ServletContext.class );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getServletContext();
                }
            };
        case "$servletPath":
            ensureTypes( variable, type, STRING_TYPES );
            return new ArgumentResolver( variable, type ) {

                public Object getArgument( Invocation invocation ) throws IOException, ServletException
                {
                    return invocation.getRequest().getServletPath();
                }
            };
        case "$principal":
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
