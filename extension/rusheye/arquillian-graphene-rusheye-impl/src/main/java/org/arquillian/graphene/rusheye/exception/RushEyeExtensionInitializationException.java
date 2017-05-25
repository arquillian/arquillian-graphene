package org.arquillian.graphene.rusheye.exception;

public class RushEyeExtensionInitializationException extends RuntimeException {

	private static final long serialVersionUID = -1085706802993077668L;

	public RushEyeExtensionInitializationException() {
    }

    public RushEyeExtensionInitializationException(String message) {
        super(message);
    }

    public RushEyeExtensionInitializationException(Throwable cause) {
        super(cause);
    }

    public RushEyeExtensionInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

}