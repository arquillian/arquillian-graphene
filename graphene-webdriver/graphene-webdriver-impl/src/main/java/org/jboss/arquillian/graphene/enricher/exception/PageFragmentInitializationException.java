package org.jboss.arquillian.graphene.enricher.exception;

public class PageFragmentInitializationException extends RuntimeException {

    private static final long serialVersionUID = 444458L;
    
    public PageFragmentInitializationException(String message) {
        super(message);
    }

    public PageFragmentInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PageFragmentInitializationException(Throwable cause) {
        super(cause);
    }
}
