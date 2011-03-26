package org.jboss.arquillian.ajocado.testng.listener.lifecycle;

import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.AFTER_CLASS;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.AFTER_METHOD;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.BEFORE_CLASS;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.BEFORE_METHOD;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(LifecycleTestingConfigurationListener.class)
public class AbstractTestingConfigurationListener {

    @BeforeClass
    public void beforeClass() {
        LifecycleTestingConfigurationListener.assertPhase(BEFORE_CLASS);
    }

    @BeforeMethod
    public void beforeMethod() {
        LifecycleTestingConfigurationListener.assertPhase(BEFORE_METHOD);
    }

    @AfterMethod
    public void afterMethod() {
        LifecycleTestingConfigurationListener.assertPhase(AFTER_METHOD);
    }

    @AfterClass
    public void afterClass() {
        LifecycleTestingConfigurationListener.assertPhase(AFTER_CLASS);
    }
}
