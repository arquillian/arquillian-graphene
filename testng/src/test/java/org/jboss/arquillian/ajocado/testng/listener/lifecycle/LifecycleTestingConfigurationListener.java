package org.jboss.arquillian.ajocado.testng.listener.lifecycle;

import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.AFTER_CLASS;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.AFTER_METHOD;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.BEFORE_CLASS;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.BEFORE_METHOD;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.LISTENER_AFTER_CLASS;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.LISTENER_AFTER_METHOD;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.LISTENER_AFTER_SUITE;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.LISTENER_BEFORE_CLASS;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.LISTENER_BEFORE_METHOD;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.LISTENER_BEFORE_SUITE;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.TEST1;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.TEST2;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;
import org.jboss.arquillian.ajocado.testng.listener.AbstractConfigurationListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

@Test(enabled = false)
public class LifecycleTestingConfigurationListener extends AbstractConfigurationListener {

    private static final Phase[] PHASES = new Phase[] { LISTENER_BEFORE_SUITE, LISTENER_BEFORE_CLASS, BEFORE_CLASS,
            LISTENER_BEFORE_METHOD, BEFORE_METHOD, TEST1, LISTENER_AFTER_METHOD, AFTER_METHOD, LISTENER_BEFORE_METHOD,
            BEFORE_METHOD, TEST2, LISTENER_AFTER_METHOD, AFTER_METHOD, LISTENER_AFTER_CLASS, AFTER_CLASS,
            LISTENER_BEFORE_CLASS, BEFORE_CLASS, LISTENER_BEFORE_METHOD, BEFORE_METHOD, TEST1, LISTENER_AFTER_METHOD,
            AFTER_METHOD, LISTENER_BEFORE_METHOD, BEFORE_METHOD, TEST2, LISTENER_AFTER_METHOD, AFTER_METHOD,
            LISTENER_AFTER_CLASS, AFTER_CLASS, LISTENER_AFTER_SUITE };

    private static int phase = 0;

    @BeforeSuite
    public void beforeSuite() {
        assertPhase(LISTENER_BEFORE_SUITE);
    }

    @BeforeClass
    public void beforeClass() {
        assertPhase(LISTENER_BEFORE_CLASS);
    }

    @BeforeMethod
    public void beforeMethod() {
        assertPhase(LISTENER_BEFORE_METHOD);
    }

    @AfterMethod
    public void afterMethod() {
        assertPhase(LISTENER_AFTER_METHOD);
    }

    @AfterClass
    public void afterClass() {
        assertPhase(LISTENER_AFTER_CLASS);
    }

    @AfterSuite
    public void afterSuite() {
        assertPhase(LISTENER_AFTER_SUITE);
    }

    public static void assertPhase(Phase... actualPhases) {
        final Phase phaseName = PHASES[phase++];
        assertTrue(ArrayUtils.contains(actualPhases, phaseName),
            "Actual phase options (" + Arrays.deepToString(actualPhases) + ") doesn't match expected phase ("
                + phaseName + ")");
    }

    public static enum Phase {
        LISTENER_BEFORE_SUITE, LISTENER_BEFORE_CLASS, LISTENER_BEFORE_METHOD, TEST1, TEST2, LISTENER_AFTER_METHOD, LISTENER_AFTER_CLASS, LISTENER_AFTER_SUITE, BEFORE_CLASS, BEFORE_METHOD, AFTER_METHOD, AFTER_CLASS
    }

}
