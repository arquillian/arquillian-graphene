package org.jboss.arquillian.graphene.request;

/**
 * Types of request, which browser executes.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public enum RequestType {

    /** The XMLHttpRequest type */
    XHR,
    /** The regular HTTP request type. */
    HTTP,
    /** The type for no request. */
    NONE;
}