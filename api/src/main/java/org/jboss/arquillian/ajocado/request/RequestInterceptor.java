package org.jboss.arquillian.ajocado.request;

import org.jboss.arquillian.ajocado.request.RequestType;

public interface RequestInterceptor {
	public RequestType getRequestTypeDone();
	
	public RequestType clearRequestTypeDone();
	
	public void waitForRequestTypeChange();
}
