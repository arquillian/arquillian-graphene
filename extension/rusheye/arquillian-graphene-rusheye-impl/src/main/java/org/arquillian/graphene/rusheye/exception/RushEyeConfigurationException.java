package org.arquillian.graphene.rusheye.exception;

public class RushEyeConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 2544120188796893890L;

	public RushEyeConfigurationException() {
    }

    public RushEyeConfigurationException(String message) {
        super(message);
    }

    public RushEyeConfigurationException(Throwable cause) {
        super(cause);
    }

    public RushEyeConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
}