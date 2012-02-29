package org.jboss.arquillian.ajocado;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.framework.AjocadoConfigurationContext;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumImpl;
import org.jboss.arquillian.ajocado.framework.SystemPropertiesConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TestAjocadoMigration {

    @Mock
    GrapheneSeleniumImpl selenium;

    @Mock
    SystemPropertiesConfiguration configuration;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void impl_could_be_set_to_context() {
        AjaxSeleniumContext.set(selenium);
    }

    @Test
    public void impl_could_be_proxied() {
        AjaxSeleniumContext.set(selenium);
        AjaxSeleniumContext.getProxy();
    }

    @Test
    public void impl_is_not_initialized() {
        AjaxSeleniumContext.set(null);
        assertFalse(AjaxSeleniumContext.isInitialized());
    }

    @Test
    public void impl_is_initialized() {
        AjaxSeleniumContext.set(selenium);
        assertTrue(AjaxSeleniumContext.isInitialized());
    }

    @Test
    public void configuration_could_be_set_to_context() {
        AjocadoConfigurationContext.set(configuration);
    }

    @Test
    public void configuration_could_be_proxied() {
        AjocadoConfigurationContext.set(configuration);
        AjocadoConfigurationContext.getProxy();
    }

    @Test
    public void configuration_is_not_initialized() {
        AjocadoConfigurationContext.set(null);
        assertFalse(AjocadoConfigurationContext.isInitialized());
    }

    @Test
    public void configuration_is_initialized() {
        AjocadoConfigurationContext.set(configuration);
        assertTrue(AjocadoConfigurationContext.isInitialized());
    }
}