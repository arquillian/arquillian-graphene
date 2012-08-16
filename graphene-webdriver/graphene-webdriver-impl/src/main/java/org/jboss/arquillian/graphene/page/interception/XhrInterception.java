package org.jboss.arquillian.graphene.page.interception;

import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.InstallableJavaScript;
import org.jboss.arquillian.graphene.javascript.JavaScript;

@JavaScript(value="Graphene.xhrInterception")
@Dependency(sources = "Graphene.xhrInterception.js")
public interface XhrInterception extends InstallableJavaScript {

    void inject();

    void reset();

    boolean isReplaced();
}
