package org.jboss.arquillian.graphene.container;

import java.lang.reflect.Method;
import java.net.URL;

/**
 * The service which is able to lookup the contextRoot URL for context of current test and test method.
 *
 * @author Lukas Fryc
 */
public interface ServletURLLookupService {

    /**
     * Retrieves contextRoot in the context of given testMethod
     *
     * Returns null if contextRoot can't be retrieved (when no deployment is available)
     */
    URL getContextRoot(Method testMethod);

}
