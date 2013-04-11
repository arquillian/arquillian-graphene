package org.jboss.arquillian.graphene.page;

/**
 * Identifies the state of the current request
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 */
public enum RequestState {

    /**
     * No request is currently in progress or done
     */
    NONE,
    /**
     * The request is in progress
     */
    IN_PROGRESS,
    /**
     * The request is done
     */
    DONE
}
