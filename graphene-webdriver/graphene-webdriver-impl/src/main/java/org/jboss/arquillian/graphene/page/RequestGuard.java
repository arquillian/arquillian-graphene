package org.jboss.arquillian.graphene.page;

import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.page.interception.XhrInterception;

@JavaScript("Graphene.requestGuard")
@Dependency(sources = "requestGuard.js", interfaces = XhrInterception.class)
public interface RequestGuard {

    RequestType getRequestDone();

    RequestType clearRequestDone();

    boolean isRequestDone();
}