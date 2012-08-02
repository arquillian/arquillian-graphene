package org.jboss.arquillian.graphene.page;

/**
 * Types of request, which browser executes.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public enum RequestType {

    /** The XMLHttpRequest type */
    XHR,
    /** The regular HTTP request type. */
    HTTP,
    /** The type for no request. */
    NONE;
}