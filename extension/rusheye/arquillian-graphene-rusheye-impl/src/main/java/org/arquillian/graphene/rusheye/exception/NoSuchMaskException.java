package org.arquillian.graphene.rusheye.exception;

public class NoSuchMaskException extends RuntimeException{
	
	private static final long serialVersionUID = -6912622751610135186L;
	
	public NoSuchMaskException() {
    }

    public NoSuchMaskException(String message) {
        super(message);
    }

    public NoSuchMaskException(Throwable cause) {
        super(cause);
    }

    public NoSuchMaskException(String message, Throwable cause) {
        super(message, cause);
    }
}
