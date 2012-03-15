package org.jboss.arquillian.graphene.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Proxy;

import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebElement;

public class TestGrapheneContextProxying {

    private static final String SAMPLE_STRING = "sample";

    @Test
    public void context_provides_proxy_which_delegates_to_current_context() {
        // having
        WebDriver driver = mock(WebDriver.class);

        // when
        GrapheneContext.set(driver);
        when(driver.toString()).thenReturn(SAMPLE_STRING);

        // then
        WebDriver proxy = GrapheneContext.getProxy();
        assertEquals(SAMPLE_STRING, proxy.toString());
    }

    @Test
    public void when_proxy_returns_webdriver_api_then_another_proxy_is_returned_instead_of_invocation() {
        // having
        WebDriver driver = mock(WebDriver.class);

        // when
        GrapheneContext.set(driver);
        when(driver.navigate()).thenThrow(IllegalStateException.class);

        // then
        WebDriver driverProxy = GrapheneContext.getProxy();
        // no exception is thrown for navigate
        Navigation navigationProxy = driverProxy.navigate();
        // and returned instance is proxy
        assertTrue(Proxy.isProxyClass(navigationProxy.getClass()));
    }

    @Test
    public void when_proxy_returns_anotherProxy_then_anotherProxy_delegates_to_current_context() {
        // having
        WebDriver driver = mock(WebDriver.class);
        WebDriver anotherDriver = mock(WebDriver.class);
        Navigation navigation = mock(Navigation.class);

        // when
        GrapheneContext.set(driver);
        when(driver.navigate()).thenThrow(IllegalStateException.class);
        when(anotherDriver.navigate()).thenReturn(navigation);
        when(navigation.toString()).thenReturn(SAMPLE_STRING);

        // then
        WebDriver driverProxy = GrapheneContext.getProxy();
        Navigation navigationProxy = driverProxy.navigate();
        GrapheneContext.set(anotherDriver);
        assertEquals(SAMPLE_STRING, navigationProxy.toString());
    }

    @Test
    public void when_method_with_arguments_is_invoked_on_proxy_then_it_is_invoked_immediatelly() {
        // having
        WebDriver driver = mock(WebDriver.class);
        WebElement element = mock(WebElement.class);
        By by = mock(By.class);

        // when
        GrapheneContext.set(driver);
        when(driver.findElement(Mockito.any(By.class))).thenReturn(element);

        // then
        WebDriver driverProxy = GrapheneContext.getProxy();
        assertEquals(element, driver.findElement(by));
    }
}
