/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.arquillian.graphene.proxy;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.GrapheneContext;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.ImeHandler;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.logging.Logs;

/**
 * @author Lukas Fryc
 */
public class TestGrapheneProxyHandler {

    private GrapheneProxyHandler handler;

    private class IsProxyable implements Answer<Object> {

        List<Method> violations = new LinkedList<Method>();

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            Method method = invocation.getMethod();
            if (!handler.isProxyable(method, invocation.getArguments())) {
                violations.add(method);
            }
            return null;
        }

        public List<Method> getViolations() {
            return Collections.unmodifiableList(violations);
        }
    }

    private class IsNotProxyable implements Answer<Object> {

        List<Method> violations = new LinkedList<Method>();

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            Method method = invocation.getMethod();
            if (handler.isProxyable(method, invocation.getArguments())) {
                violations.add(method);
            }
            return null;
        }

        public List<Method> getViolations() {
            return Collections.unmodifiableList(violations);
        }
    }

    @Before
    public void prepare() {
        handler = GrapheneProxyHandler.forTarget(GrapheneContext.getContextFor(Default.class), null);
    }

    @Test
    public void test_webDriver_methods_which_should_not_return_proxy() {
        IsNotProxyable isNotProxyable = new IsNotProxyable();

        // when
        WebDriver driver = Mockito.mock(WebDriver.class, isNotProxyable);
        Options options = mock(Options.class, isNotProxyable);
        Navigation navigation = mock(Navigation.class, isNotProxyable);
        ImeHandler ime = mock(ImeHandler.class, isNotProxyable);
        Logs logs = mock(Logs.class, isNotProxyable);

        // then
        try {
            driver.toString();
            driver.close();
            driver.equals(new Object());
            driver.get("");
            driver.getClass();
            driver.getCurrentUrl();
            driver.getPageSource();
            driver.getTitle();
            driver.getWindowHandle();
            driver.hashCode();
            driver.quit();
            driver.toString();

            options.addCookie(mock(Cookie.class));
            options.deleteAllCookies();
            options.deleteCookie(mock(Cookie.class));
            options.deleteCookieNamed("");
            options.getCookieNamed("");

            navigation.back();
            navigation.forward();
            navigation.to("");
            navigation.to(new URL("http://localhost/"));

            ime.activateEngine("");
            ime.deactivate();
            ime.getActiveEngine();
            ime.isActivated();

            logs.get("");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertEquals(Arrays.asList(), isNotProxyable.getViolations());
    }

    @Test
    public void test_webDriver_methods_which_should_return_proxy() {
        IsProxyable isProxyable = new IsProxyable();

        // when
        WebDriver driver = Mockito.mock(WebDriver.class, isProxyable);
        Options options = mock(Options.class, isProxyable);
        TargetLocator targetLocator = mock(TargetLocator.class, isProxyable);
        ImeHandler ime = mock(ImeHandler.class, isProxyable);
        Timeouts timeouts = mock(Timeouts.class, isProxyable);

        // then
        try {
            driver.manage();
            driver.navigate();
            driver.switchTo();
            driver.findElement(By.className(""));
            driver.findElements(By.className(""));
            driver.getWindowHandles();

            options.ime();
            options.logs();
            options.timeouts();
            options.window();
            options.getCookies();

            targetLocator.activeElement();
            targetLocator.alert();
            targetLocator.defaultContent();
            targetLocator.frame(0);
            targetLocator.frame("name");
            targetLocator.frame(mock(WebElement.class));
            targetLocator.window("name");

            ime.getAvailableEngines();

            timeouts.implicitlyWait(1L, TimeUnit.MICROSECONDS);
            timeouts.setScriptTimeout(1L, TimeUnit.MICROSECONDS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertEquals(Arrays.asList(), isProxyable.getViolations());
    }
}
