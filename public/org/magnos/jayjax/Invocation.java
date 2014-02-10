
package org.magnos.jayjax;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class Invocation
{

    private Function function;
    private Object[] arguments;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public Function getFunction()
    {
        return function;
    }

    public Object[] getArguments()
    {
        return arguments;
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }

    public HttpServletResponse getResponse()
    {
        return response;
    }

}
