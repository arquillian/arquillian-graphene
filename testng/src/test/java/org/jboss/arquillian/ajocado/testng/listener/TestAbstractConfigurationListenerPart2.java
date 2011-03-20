package org.jboss.arquillian.ajocado.testng.listener;

import static org.jboss.arquillian.ajocado.testng.listener.TestingConfigurationListener.Phase.TEST1;
import static org.jboss.arquillian.ajocado.testng.listener.TestingConfigurationListener.Phase.TEST2;

import org.testng.annotations.Test;

public class TestAbstractConfigurationListenerPart2 extends AbstractTestingConfigurationListener {
    @Test
    public void test1() {
        TestingConfigurationListener.assertPhase(TEST1);
    }

    @Test
    public void test2() {
        TestingConfigurationListener.assertPhase(TEST2);
    }
}
