package org.jboss.arquillian.graphene.ftest.intercept;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class TestInterceptorRegistration {
    @Drone
    private WebDriver browser;

    private boolean interceptor_registered_before_test_invoked = false;

    @Before
    public void resetFlags() {
        interceptor_registered_before_test_invoked = false;
    }

    @Before
    public void register_interceptor_before_test() {
        ((GrapheneProxyInstance) browser).registerInterceptor(new Interceptor() {
            public Object intercept(InvocationContext context) throws Throwable {
                interceptor_registered_before_test_invoked = true;
                return context.invoke();
            }
        });
    }

    @Test
    public void interceptor_can_be_registered_before_test() {
        assertFalse(interceptor_registered_before_test_invoked);
        browser.get("about:blank");
        assertTrue(interceptor_registered_before_test_invoked);
    }

    @Test
    public void interceptor_can_be_registered_by_extension_before_test() {
        assertFalse(InterceptorRegistrationExtension.invoked);
        browser.get("about:blank");
        assertTrue(InterceptorRegistrationExtension.invoked);
    }

}
