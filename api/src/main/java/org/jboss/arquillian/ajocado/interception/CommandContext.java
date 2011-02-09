package org.jboss.arquillian.ajocado.interception;

public interface CommandContext {
	public Object invoke() throws CommandInterceptionException;
	
	public String getCommand();
	
	public String[] getArguments();
}
