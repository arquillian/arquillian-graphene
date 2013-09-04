package org.jboss.arquillian.graphene.intercept;

import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.junit.Test;

public class TestIntercepting {

    @Test
    public void testInterceptorCalling() {
        // having
        MyObject target = new MyObject();
        MyObject proxy = GrapheneProxy.getProxyForTarget(null, target);

        // when
        ((GrapheneProxyInstance) proxy).registerInterceptor(new Interceptor() {
            @Override
            public Object intercept(InvocationContext context) throws Throwable {
                return context.invoke();
            }
        });

        proxy.someMethod();
    }

    public class MyObject {
        public void someMethod() {
        }
    }
}
