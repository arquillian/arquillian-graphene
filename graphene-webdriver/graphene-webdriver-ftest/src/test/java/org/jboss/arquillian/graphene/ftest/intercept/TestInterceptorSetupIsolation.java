package org.jboss.arquillian.graphene.ftest.intercept;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(Arquillian.class)
public class TestInterceptorSetupIsolation {
    @Drone
    private WebDriver browser;

    private boolean invoked = false;

    @Before
    public void resetFlags() {
        invoked = false;
    }

    @Test
    @InSequence(1)
    public void interceptor_can_be_registered_in_test_itself() {
        ((GrapheneProxyInstance) browser).registerInterceptor(new Interceptor() {

            @Override
            public Object intercept(InvocationContext context) throws Throwable {
                invoked = true;
                return context.invoke();
            }
        });

        assertFalse(invoked);
        browser.get("about:blank");
        assertTrue(invoked);
    }

    /**
     * Tests that interceptor from previous test isn't propagated to this test
     */
    @Test
    @InSequence(2)
    public void interceptors_arent_shared_across_tests() {
        assertFalse(invoked);
        browser.get("about:blank");
        assertFalse(invoked);
    }
}
