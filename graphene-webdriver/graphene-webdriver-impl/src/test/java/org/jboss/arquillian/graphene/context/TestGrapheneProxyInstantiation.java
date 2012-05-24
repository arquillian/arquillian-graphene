package org.jboss.arquillian.graphene.context;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import org.jboss.arquillian.graphene.context.GrapheneProxy.FutureTarget;
import org.junit.Test;

public class TestGrapheneProxyInstantiation {

    @Test
    public void when_proxy_is_created_then_no_constructor_is_called() {
        // having
        FutureTarget target = new FutureTarget() {

            @Override
            public Object getTarget() {
                throw new ExpectedException();
            }
        };

        // when
        TestingDriver driver = GrapheneProxy.<TestingDriver>getProxyForFutureTarget(target, TestingDriver.class);
        try {
            driver.quit();
            fail("exception should be thrown because of FutureTarget definition");
        } catch (ExpectedException e) {
            // expected
        }

        // verify
        assertFalse(TestingDriver.contructorInvoked);
    }

    public static class TestingDriver extends TestingDriverStub {

        public static boolean contructorInvoked = false;

        public TestingDriver() {
            contructorInvoked = true;
        }
    }

    @SuppressWarnings("serial")
    public static class ExpectedException extends RuntimeException {

    }

}
