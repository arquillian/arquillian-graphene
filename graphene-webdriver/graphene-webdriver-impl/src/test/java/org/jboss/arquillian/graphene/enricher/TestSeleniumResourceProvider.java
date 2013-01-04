package org.jboss.arquillian.graphene.enricher;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.SeleniumResourceProvider.ActionsProvider;
import org.jboss.arquillian.graphene.enricher.SeleniumResourceProvider.KeyboardProvider;
import org.jboss.arquillian.graphene.enricher.SeleniumResourceProvider.MouseProvider;
import org.jboss.arquillian.graphene.enricher.SeleniumResourceProvider.WebDriverProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;

/**
 * @author Lukas Fryc
 */
@RunWith(MockitoJUnitRunner.class)
public class TestSeleniumResourceProvider {

    @Mock(extraInterfaces = HasInputDevices.class)
    WebDriver driver;

    @Before
    public void setUp() {
        GrapheneContext.set(driver);
    }

    @After
    public void tearDown() {
        GrapheneContext.reset();
    }

    @Test
    public void testDirectProviderCanProvideMethod() {
        // having
        WebDriverProvider provider = new WebDriverProvider();
        // then
        assertTrue(provider.canProvide(WebDriver.class));
        assertFalse(provider.canProvide(JavascriptExecutor.class));
    }

    @Test
    public void testDirectProviderLookup() {
        // having
        WebDriverProvider provider = new WebDriverProvider();
        // when
        Object object = provider.lookup(null, null);
        // then
        assertTrue(object instanceof WebDriver);
    }

    @Test
    public void testIndirectProviderCanProvideMethod() {
        // having
        KeyboardProvider provider = new KeyboardProvider();
        // then
        assertTrue(provider.canProvide(Keyboard.class));
        assertFalse(provider.canProvide(Mouse.class));
    }

    @Test
    public void testIndirectProviderLookup() {
        // having
        KeyboardProvider provider = new KeyboardProvider();
        // when
        Object object = provider.lookup(null, null);
        // then
        assertTrue(object instanceof Keyboard);
    }

    @Test
    public void testMouseProviderLookup() {
        // having
        MouseProvider provider = new MouseProvider();
        // when
        Object object = provider.lookup(null, null);
        // then
        assertTrue(object instanceof Mouse);
    }

    @Test
    public void testActionsProviderLookup() {
        // having
        ActionsProvider provider = new ActionsProvider();
        Mouse mouse = mock(Mouse.class);
        Keyboard keyboard = mock(Keyboard.class);
        when(((HasInputDevices) driver).getMouse()).thenReturn(mouse);
        when(((HasInputDevices) driver).getKeyboard()).thenReturn(keyboard);
        // when
        Actions actions = (Actions) provider.lookup(null, null);
        actions.click().perform();
        // then
        verify(mouse).click(null);
        verifyNoMoreInteractions(mouse, keyboard);
    }
}
