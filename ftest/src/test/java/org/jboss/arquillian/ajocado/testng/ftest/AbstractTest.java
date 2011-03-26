package org.jboss.arquillian.ajocado.testng.ftest;

import java.net.URL;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration;
import org.jboss.arquillian.ajocado.testng.AjocadoRunner;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;

@Listeners({AjocadoRunner.class})
public class AbstractTest {

    AjaxSelenium selenium;

    AjocadoConfiguration configuration;

    URL contextPath;

    @BeforeClass(alwaysRun = true)
    public void fetchConfiguration() {
        this.contextPath = configuration.getContextPath();
    }
}
