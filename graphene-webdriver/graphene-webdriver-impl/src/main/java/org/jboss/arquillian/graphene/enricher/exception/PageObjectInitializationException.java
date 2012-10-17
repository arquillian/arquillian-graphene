package org.jboss.arquillian.graphene.enricher.exception;

public class PageObjectInitializationException extends RuntimeException {

    private static final long serialVersionUID = 7897241L;

    public PageObjectInitializationException(String message) {
        super(message);
    }

    public PageObjectInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageObjectInitializationException(Throwable cause) {
        super(cause);
    }
}
