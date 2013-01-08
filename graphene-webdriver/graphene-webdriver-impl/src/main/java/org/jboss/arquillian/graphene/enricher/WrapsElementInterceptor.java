package org.jboss.arquillian.graphene.enricher;

import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;

public class WrapsElementInterceptor implements Interceptor {

    private GrapheneProxyInstance elementProxy;

    public WrapsElementInterceptor(GrapheneProxyInstance elementProxy) {
        this.elementProxy = elementProxy;
    }

    public Object intercept(InvocationContext context) throws Throwable {
        return elementProxy.unwrap();
    }
}