
package org.magnos.jayjax;

import java.lang.reflect.Method;


public class Function
{

    private String givenAction;
    private String givenMethod;
    private String givenInvoke;
    private String givenValidator;
    private String givenSecure;

    private Method method;
    private Class<?>[] parameters;
    private Validator validator;
    private boolean secure;

    public String getGivenAction()
    {
        return givenAction;
    }

    public void setGivenAction( String givenAction )
    {
        this.givenAction = givenAction;
    }

    public String getGivenMethod()
    {
        return givenMethod;
    }

    public void setGivenMethod( String givenMethod )
    {
        this.givenMethod = givenMethod;
    }

    public String getGivenInvoke()
    {
        return givenInvoke;
    }

    public void setGivenInvoke( String givenInvoke )
    {
        this.givenInvoke = givenInvoke;
    }

    public String getGivenValidator()
    {
        return givenValidator;
    }

    public void setGivenValidator( String givenValidator )
    {
        this.givenValidator = givenValidator;
    }

    public String getGivenSecure()
    {
        return givenSecure;
    }

    public void setGivenSecure( String givenSecure )
    {
        this.givenSecure = givenSecure;
    }

    public Method getMethod()
    {
        return method;
    }

    public void setMethod( Method method )
    {
        this.method = method;
    }

    public Class<?>[] getParameters()
    {
        return parameters;
    }

    public void setParameters( Class<?>[] parameters )
    {
        this.parameters = parameters;
    }

    public Validator getValidator()
    {
        return validator;
    }

    public void setValidator( Validator validator )
    {
        this.validator = validator;
    }

    public boolean isSecure()
    {
        return secure;
    }

    public void setSecure( boolean secure )
    {
        this.secure = secure;
    }

}
