package org.arquillian.graphene.rusheye.exception;

public class RushEyeDescriptorParsingException extends RuntimeException {

	private static final long serialVersionUID = -1828106902777124637L;

	public RushEyeDescriptorParsingException() {
    }

    public RushEyeDescriptorParsingException(String message) {
        super(message);
    }

    public RushEyeDescriptorParsingException(Throwable cause) {
        super(cause);
    }

    public RushEyeDescriptorParsingException(String message, Throwable cause) {
        super(message, cause);
    }

}