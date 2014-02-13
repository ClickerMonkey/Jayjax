package org.magnos.jayjax;

public interface JayjaxListener
{
	public void onMessage(JayjaxMessage message, Invocation invocation, Throwable e);
}
