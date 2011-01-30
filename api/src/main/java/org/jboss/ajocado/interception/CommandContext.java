package org.jboss.ajocado.interception;

public interface CommandContext {
	public Object invoke();
	
	public String getCommand();
	
	public String[] getArguments();
}
