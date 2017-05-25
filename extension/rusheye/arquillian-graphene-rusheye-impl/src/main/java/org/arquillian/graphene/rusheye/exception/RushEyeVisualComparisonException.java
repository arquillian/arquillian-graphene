package org.arquillian.graphene.rusheye.exception;

public class RushEyeVisualComparisonException extends RuntimeException {

	private static final long serialVersionUID = -1085706802993077668L;

	public RushEyeVisualComparisonException() {
    }

    public RushEyeVisualComparisonException(String message) {
        super(message);
    }

    public RushEyeVisualComparisonException(Throwable cause) {
        super(cause);
    }

    public RushEyeVisualComparisonException(String message, Throwable cause) {
        super(message, cause);
    }
}
