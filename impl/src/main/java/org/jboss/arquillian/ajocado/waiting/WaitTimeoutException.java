/*
 * 
 */
package org.jboss.arquillian.ajocado.waiting;

import org.jboss.arquillian.ajocado.format.SimplifiedFormat;

/**
 * Indicates timeout of waiting.
 */
public class WaitTimeoutException extends RuntimeException {
    private static final long serialVersionUID = 6056785264760499779L;

    // failure subject - cannot be null after construction
    private Object failure;

    public WaitTimeoutException(Exception exception) {
        if (exception != null) {
            failure = exception;
        }
    }

    public WaitTimeoutException(CharSequence message, Object... args) {
        if (args != null) {
            failure = SimplifiedFormat.format(message.toString(), args);
        } else {
            failure = message.toString();
        }
    }

    @Override
    public Throwable getCause() {
        if (failure instanceof Exception) {
            return (Exception) failure;
        }

        return super.getCause();
    }

    @Override
    public String getMessage() {
        if (failure instanceof Exception) {
            return ((Exception) failure).getMessage();
        }
        return failure.toString();
    }
}