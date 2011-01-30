package org.jboss.ajocado.framework;

import java.util.List;

public interface SeleniumExtensions {
	public void requireResources(List<String> resourceNames);
	
	public void registerCustomHandlers();
	
	public void requireResource(String resourceName);
}
