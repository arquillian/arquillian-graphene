package org.jboss.arquillian.graphene.guard;

import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.InstallableJavaScript;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.page.interception.XhrInterception;
import org.jboss.arquillian.graphene.request.RequestGuard;

@JavaScript(value = "Graphene.Page.RequestGuard")
@Dependency(sources = "Graphene.Page.RequestGuard.js", interfaces=XhrInterception.class)
public interface RequestGuardImpl extends RequestGuard, InstallableJavaScript {
}