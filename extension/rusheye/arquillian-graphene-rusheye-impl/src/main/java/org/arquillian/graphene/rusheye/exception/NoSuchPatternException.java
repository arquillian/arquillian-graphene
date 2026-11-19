package org.arquillian.graphene.rusheye.exception;

public class NoSuchPatternException extends RuntimeException {

	private static final long serialVersionUID = -4532972466724854705L;

	public NoSuchPatternException() {
    }

    public NoSuchPatternException(String message) {
        super(message);
    }

    public NoSuchPatternException(Throwable cause) {
        super(cause);
    }

    public NoSuchPatternException(String message, Throwable cause) {
        super(message, cause);
    }
}