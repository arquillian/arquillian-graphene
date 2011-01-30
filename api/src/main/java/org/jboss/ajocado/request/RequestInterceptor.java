package org.jboss.ajocado.request;

import org.jboss.ajocado.request.RequestType;

public interface RequestInterceptor {
	public RequestType getRequestTypeDone();
	
	public RequestType clearRequestTypeDone();
	
	public void waitForRequestTypeChange();
}
