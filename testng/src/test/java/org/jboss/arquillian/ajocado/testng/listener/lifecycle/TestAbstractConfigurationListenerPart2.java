package org.jboss.arquillian.ajocado.testng.listener.lifecycle;

import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.TEST1;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.TEST2;

import org.testng.annotations.Test;

public class TestAbstractConfigurationListenerPart2 extends AbstractTestingConfigurationListener {
    @Test
    public void testLifecycle1() {
        LifecycleTestingConfigurationListener.assertPhase(TEST1);
    }

    @Test
    public void testLifecycle2() {
        LifecycleTestingConfigurationListener.assertPhase(TEST2);
    }
}
