/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.ftest.enricher;

import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.graphene.proxy.GrapheneContextualHandler;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyUtil;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.support.FindBy;

@RunWith(Arquillian.class)
@RunAsClient
public class TestEnrichedElementsEqualsMethod {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextRoot;

    @ArquillianResource
    private JavascriptExecutor executor;

    private static final String TEST_ELEMENT_ID = "select";

    @FindBy(id = TEST_ELEMENT_ID)
    private WebElement enriched;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inCurrentPackage().find("sample.html").loadPage(browser, contextRoot);
    }

    // We're testing multiples types of WebElements here:
    // - unwrapped;
    // - proxified but not wrapped in a GrapheneElement (called proxified further on for brevity);
    // - proxified and wrapped (called enriched further on).

    private WebElement getUnwrapped() {
        // we could get this type currently with executor.executeScript("return document.getElementById('...')");
        // but this could easily change (ARQGRA-494), so we'll just unwrap the enriched element ourselves
        WebElement unwrapped = enriched;
        while (unwrapped instanceof WrapsElement) {
            unwrapped = ((WrapsElement) unwrapped).getWrappedElement();
        }
        if (unwrapped instanceof GrapheneElement || unwrapped instanceof GrapheneProxyInstance) {
            // it's not right to use asserts here - not the point of the test
            // also a caller might use @Test(expected = AssertionError.class)
            throw new RuntimeException("Not unwrapped enough: " + unwrapped.getClass());
        }
        return unwrapped;
    }

    private WebElement getProxified() {
        // right now browser.switchTo().activeElement() is an example of a proxified but not wrapped element
        // we won't rely on that though, because it could easily change, so instead we'll use the proxifier directly (and in
        // such a way so as to benefit from the fix relevant to the test)

        GrapheneContextualHandler handler = GrapheneContextualHandler.forTarget(GrapheneContext.lastContext(), browser);
        WebElement proxy;
        try {
            Method findElement = WebDriver.class.getMethod("findElement", By.class);
            proxy = (WebElement) handler.invoke(browser, findElement, new Object[] { By.id(TEST_ELEMENT_ID) });
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        // in case you need to reproduce the problem, create the proxy manually:
        // Class<?>[] interfaces = GrapheneProxyUtil.getInterfaces(getUnwrapped().getClass());
        // WebElement proxy = GrapheneProxy.getProxyForTargetWithInterfaces(GrapheneContext.lastContext(), getUnwrapped(),
        // interfaces);

        if (!(proxy instanceof GrapheneProxyInstance) || proxy instanceof GrapheneElement) {
            // it's not right to use asserts here - not the point of the test
            // also a caller might use @Test(expected = AssertionError.class)
            throw new RuntimeException(
                "Not proxified correctly: " + Arrays.toString(GrapheneProxyUtil.getInterfaces(proxy.getClass())));
        }
        return proxy;
    }

    // Since we're testing the equals method itself, we should do it in the assertTrue(a.equals(b)) manner. When using
    // assertEquals for reflexivity checks JUnit is within its rights to see that expected == actual and be satisfied. Or it can
    // switch operand places and call actual.equals(expected). assertEquals(el1, el2) also wouldn't be correct semantically, as
    // we're not really "expecting el1". But assertTrue(el1.equals(el2)) would be correct, since we're expecting the outcome of
    // equals to be true.

    // -------------------------------------------------------------------------
    // Reflexivity
    // -------------------------------------------------------------------------

    @Test
    public void testEnrichedEqualsToSelf() {
        assertTrue(enriched.equals(enriched));
    }

    @Test
    public void testUnwrappedEqualsToSelf() {
        // testing Selenium here, so it's more of a sanity check
        assertTrue(getUnwrapped().equals(getUnwrapped()));
    }

    @Test
    public void testProxifiedEqualsToSelf() {
        WebElement proxy = getProxified();
        assertTrue(proxy.equals(proxy));
    }

    @Test
    public void testProxifiedEqualsToProxified() {
        // not strictly reflexivity, but reflexivity at the underlying element level
        assertNotSame(getProxified(), getProxified()); // test doesn't work otherwise
        assertTrue(getProxified().equals(getProxified()));
    }

    @Test
    public void testEnrichedEqualsToEnriched() {
        // not strictly reflexivity, but reflexivity at the underlying element level
        WebElement altEnriched = browser.findElement(By.id(TEST_ELEMENT_ID));
        assertNotSame(enriched, altEnriched);
        assertNotSame(((GrapheneProxyInstance) enriched).unwrap(), ((GrapheneProxyInstance) altEnriched).unwrap());
        assertTrue(enriched.equals(altEnriched));
    }

    // -------------------------------------------------------------------------
    // Symmetry & hashCode (& implied transitivity)
    // -------------------------------------------------------------------------

    @Test
    public void testEnrichedEqualsToUnwrapped() {
        assertTrue(enriched.equals(getUnwrapped()));
    }

    @Test
    public void testEnrichedHashCodeEqualsToUnwrapped() {
        assertTrue(enriched.hashCode() == getUnwrapped().hashCode());
    }

    @Test
    public void testUnwrappedEqualsToEnriched() {
        assertTrue(getUnwrapped().equals(enriched));
    }

    @Test
    public void testUnwrappedHashCodeEqualsToEnriched() {
        assertTrue(getUnwrapped().hashCode() == enriched.hashCode());
    }

    @Test
    public void testEnrichedEqualsToProxified() {
        assertTrue(enriched.equals(getProxified()));
    }

    @Test
    public void testEnrichedHashCodeEqualsToProxified() {
        assertTrue(enriched.hashCode() == getProxified().hashCode());
    }

    @Test
    public void testProxifiedEqualsToEnriched() {
        assertTrue(getProxified().equals(enriched));
    }

    @Test
    public void testProxifiedHashCodeEqualsToEnriched() {
        assertTrue(getProxified().hashCode() == enriched.hashCode());
    }

    @Test
    public void testUnwrappedEqualsToProxified() {
        assertTrue(getUnwrapped().equals(getProxified()));
    }

    @Test
    public void testUnwrappedHashCodeEqualsToProxified() {
        assertTrue(getUnwrapped().hashCode() == getProxified().hashCode());
    }

    @Test
    public void testProxifiedEqualsToUnwrapped() {
        assertTrue(getProxified().equals(getUnwrapped()));
    }

    @Test
    public void testProxifiedHashCodeEqualsToUnwrapped() {
        assertTrue(getProxified().hashCode() == getUnwrapped().hashCode());
    }

    // -------------------------------------------------------------------------
    // Specific edge cases
    // -------------------------------------------------------------------------

    private WebElement getReturnedFromJSExecutor() {
        return (WebElement) executor.executeScript("return document.getElementById('" + TEST_ELEMENT_ID + "')");
    }

    @Test
    public void testEnrichedEqualsToReturnedFromJSExecutor() {
        assertTrue(enriched.equals(getReturnedFromJSExecutor()));
    }

    @Test
    public void testReturnedFromJSExecutorEqualsToEnriched() {
        assertTrue(getReturnedFromJSExecutor().equals(enriched));
    }

    private WebElement getTargetActiveElement() {
        enriched.click();
        return browser.switchTo().activeElement();
    }

    @Test
    public void testEnrichedEqualsToTargetActiveElement() {
        assertTrue(enriched.equals(getTargetActiveElement()));
    }

    @Test
    public void testTargetActiveElementEqualsToEnriched() {
        assertTrue(getTargetActiveElement().equals(enriched));
    }

}
