package org.jboss.arquillian.graphene.guard;

import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.InstallableJavaScript;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.page.RequestType;
import org.jboss.arquillian.graphene.page.interception.XhrInterception;

@JavaScript(value = "Graphene.Page.RequestGuard")
@Dependency(sources = "Graphene.Page.RequestGuard.js", interfaces=XhrInterception.class)
public interface RequestGuard extends InstallableJavaScript {

    /**
     * @return the last request type
     */
    RequestType getRequestDone();

    /**
     * Clears the request type cache and returns the last request type
     * @return the last request type
     */
    RequestType clearRequestDone();
}