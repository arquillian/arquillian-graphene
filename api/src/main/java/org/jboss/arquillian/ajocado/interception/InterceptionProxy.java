package org.jboss.arquillian.ajocado.interception;

import java.util.Set;

public interface InterceptionProxy {
	public <T extends Object> T getCommandProcessorProxy();
	
	public void registerInterceptor(CommandInterceptor interceptor);
	
	public CommandInterceptor unregisterInterceptor(CommandInterceptor interceptor);
	
	public Set<CommandInterceptor> unregisterInterceptorType(Class<? extends CommandInterceptor> type);
}
