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

import java.util.regex.Matcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.magnos.jayjax.json.JsonObject;


public class Invocation
{

    private Function function;
    private Matcher matcher;
    private Object[] arguments;
    private JsonObject parameters;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private Object result;

    public Function getFunction()
    {
        return function;
    }

    public void setFunction( Function function )
    {
        this.function = function;
    }

    public Matcher getMatcher()
    {
        return matcher;
    }

    public void setMatcher( Matcher matcher )
    {
        this.matcher = matcher;
    }

    public Object[] getArguments()
    {
        return arguments;
    }

    public void setArguments( Object[] arguments )
    {
        this.arguments = arguments;
    }

    public JsonObject getParameters()
    {
        return parameters;
    }

    public void setParameters( JsonObject parameters )
    {
        this.parameters = parameters;
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }

    public void setRequest( HttpServletRequest request )
    {
        this.request = request;
    }

    public HttpServletResponse getResponse()
    {
        return response;
    }

    public void setResponse( HttpServletResponse response )
    {
        this.response = response;
    }

    public Object getResult()
    {
        return result;
    }

    public void setResult( Object result )
    {
        this.result = result;
    }

}
