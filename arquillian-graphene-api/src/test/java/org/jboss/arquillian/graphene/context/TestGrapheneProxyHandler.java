package org.jboss.arquillian.graphene.context;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import javax.swing.text.html.Option;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;

public class TestGrapheneProxyHandler {

    private GrapheneProxyHandler handler;

    private Answer isProxyable = new Answer() {
        public Object answer(InvocationOnMock invocation) throws Throwable {
            Method method = invocation.getMethod();
            assertTrue(handler.returnsWebDriverApi(method, invocation.getArguments()));
            return null;
        }
    };

    private Answer isNotProxyable = new Answer() {
        public Object answer(InvocationOnMock invocation) throws Throwable {
            Method method = invocation.getMethod();
            assertFalse(handler.returnsWebDriverApi(method, invocation.getArguments()));
            return null;
        }
    };

    @Before
    public void prepare() {
        handler = new GrapheneProxyHandler();
    }

    @Test
    public void test_webDriver_methods_which_should_not_return_proxy() {
        // when
        WebDriver driver = Mockito.mock(WebDriver.class, isNotProxyable);
        Options options;
        Navigation navigation;

        // then
        try {
            driver.toString();
            driver.close();
            driver.equals(new Object());
            driver.findElement(By.className(""));
            driver.findElements(By.className(""));
            driver.get("");
            driver.getClass();
            driver.getCurrentUrl();
            driver.getPageSource();
            driver.getTitle();
            driver.getWindowHandle();
            driver.getWindowHandles();
            driver.hashCode();
            driver.quit();
            driver.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void test_webDriver_methods_which_should_return_proxy() {
        // when
        WebDriver driver = Mockito.mock(WebDriver.class, isProxyable);
        Options options = mock(Options.class, isProxyable);
        TargetLocator targetLocator = mock(TargetLocator.class, isProxyable);
        Navigation navigation = mock(Navigation.class, isProxyable);

        // then
        try {
            driver.manage();
            driver.navigate();
            driver.switchTo();

            options.ime();
            options.logs();
            options.timeouts();
            options.window();
            
            targetLocator.activeElement();
            targetLocator.alert();
            targetLocator.defaultContent();
            targetLocator.frame(0);
            targetLocator.frame("name");
            targetLocator.frame(mock(WebElement.class));
            targetLocator.window("name");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
