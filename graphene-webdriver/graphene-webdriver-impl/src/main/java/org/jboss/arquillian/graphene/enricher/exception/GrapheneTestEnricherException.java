package org.jboss.arquillian.graphene.enricher.exception;

public class GrapheneTestEnricherException extends RuntimeException {

    private static final long serialVersionUID = 8757125L;

    public GrapheneTestEnricherException(String message, Throwable cause) {
        super(message, cause);
    }

    public GrapheneTestEnricherException(String message) {
        super(message);
    }

    public GrapheneTestEnricherException(Throwable cause) {
        super(cause);
    }

}
