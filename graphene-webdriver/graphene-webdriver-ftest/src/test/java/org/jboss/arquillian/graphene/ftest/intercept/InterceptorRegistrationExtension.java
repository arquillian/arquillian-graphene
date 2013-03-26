package org.jboss.arquillian.graphene.ftest.intercept;

import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.EventContext;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.jboss.arquillian.test.spi.event.suite.Test;
import org.openqa.selenium.WebDriver;

public class InterceptorRegistrationExtension implements LoadableExtension {

    public static volatile boolean invoked = false;

    @Override
    public void register(ExtensionBuilder builder) {
        builder.observer(this.getClass());
    }

    public void register_interceptor(@Observes EventContext<Test> ctx) {
        invoked = false;
        try {
            Test event = ctx.getEvent();
            if (event.getTestClass().getJavaClass() == TestInterceptorRegistration.class) {
                WebDriver browser = GrapheneContext.getProxy();
                ((GrapheneProxyInstance) browser).registerInterceptor(new Interceptor() {

                    @Override
                    public Object intercept(InvocationContext context) throws Throwable {
                        invoked = true;
                        return context.invoke();
                    }
                });
            }
        } finally {
            ctx.proceed();
        }
    }
}
