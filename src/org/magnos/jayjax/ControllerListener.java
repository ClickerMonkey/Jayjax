package org.magnos.jayjax;


public abstract class ControllerListener
{
    
    public void onInit() 
    {
        
    }
    
    public void onDestroy() 
    {
        
    }
    
    public void onBeforeInvocation(Invocation invocation) 
    {
        
    }
    
    public Object onAfterInvocation(Invocation invocation) 
    { 
        return invocation.getResult(); 
    }
    
}
