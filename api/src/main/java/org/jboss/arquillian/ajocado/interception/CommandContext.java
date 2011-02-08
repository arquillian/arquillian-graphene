package org.jboss.arquillian.ajocado.interception;

public interface CommandContext {
	public Object invoke();
	
	public String getCommand();
	
	public String[] getArguments();
}
