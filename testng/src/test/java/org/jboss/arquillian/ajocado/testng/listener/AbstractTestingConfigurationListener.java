package org.jboss.arquillian.ajocado.testng.listener;

import static org.jboss.arquillian.ajocado.testng.listener.TestingConfigurationListener.Phase.AFTER_CLASS;
import static org.jboss.arquillian.ajocado.testng.listener.TestingConfigurationListener.Phase.AFTER_METHOD;
import static org.jboss.arquillian.ajocado.testng.listener.TestingConfigurationListener.Phase.BEFORE_CLASS;
import static org.jboss.arquillian.ajocado.testng.listener.TestingConfigurationListener.Phase.BEFORE_METHOD;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(TestingConfigurationListener.class)
public class AbstractTestingConfigurationListener {

    @BeforeClass
    public void beforeClass() {
        TestingConfigurationListener.assertPhase(BEFORE_CLASS);
    }

    @BeforeMethod
    public void beforeMethod() {
        TestingConfigurationListener.assertPhase(BEFORE_METHOD);
    }

    @AfterMethod
    public void afterMethod() {
        TestingConfigurationListener.assertPhase(AFTER_METHOD);
    }

    @AfterClass
    public void afterClass() {
        TestingConfigurationListener.assertPhase(AFTER_CLASS);
    }
}
