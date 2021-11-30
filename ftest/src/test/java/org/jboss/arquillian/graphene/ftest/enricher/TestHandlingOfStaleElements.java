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

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.junit.Assert.fail;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class TestHandlingOfStaleElements {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private JavascriptExecutor executor;

    @FindBy(id = "root")
    private StaleElementPageFragment pageFragment;
    @FindBy(id = "stale")
    private StaleRootPageFragment rootPageFragment;

    @FindBy(id = "root")
    private List<StaleElementPageFragment> pageFragments;
    @FindBy(id = "root")
    private WebElement rootElement;
    @FindBy(id = "stale")
    private WebElement staleElement;

    @FindBy(id = "root")
    private List<WebElement> rootElements;

    @FindBy(tagName = "body")
    private WebElement body;

    @ArquillianResource
    private URL contextRoot;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inCurrentPackage().find("staleelements.html").loadPage(browser, contextRoot);
    }

    @Test
    public void testDeletion() {
        rootElement.isDisplayed();
        try {
            executor.executeScript("return arguments[0].parentNode.removeChild(arguments[0])", rootElement);
            rootElement.isDisplayed();
            fail("rootElement should not be found");
        } catch (NoSuchElementException e) {
        }
    }

    @Test
    public void testReplacement() {
        rootElement.isDisplayed();
        try {
            executor.executeScript("return arguments[1].parentNode.replaceChild(arguments[0],arguments[1])",
                                   staleElement, rootElement);
            rootElement.isDisplayed();
            fail("rootElement should not be found");
        } catch (NoSuchElementException e) {
        }
        staleElement.isDisplayed();
    }

    @Test
    public void testElement() throws Exception {
        rootElement.isDisplayed();
        pageFragment.makeStale();
        rootElement.isDisplayed();
    }

    @Test
    public void testListOfElements() throws Exception {
        WebElement e = rootElements.get(0);
        e.isDisplayed();
        pageFragment.makeStale();
        e.isDisplayed();
    }

    @Test
    public void testPageFragment() throws Exception {
        pageFragment.getRoot().isDisplayed();
        pageFragment.makeStale();
        pageFragment.getRoot().isDisplayed();
    }

    @Test
    public void testPageFragmentWithStaleRoot() {
        rootPageFragment.getInStale().isDisplayed();
        pageFragment.makeStale();
        rootPageFragment.getInStale().isDisplayed();
    }

    @Test
    public void testListOfPageFragments() throws Exception {
        StaleElementPageFragment pf = pageFragments.get(0);
        pf.getRoot().isDisplayed();
        pf.makeStale();
        pf.getRoot().isDisplayed();
    }

    public static class StaleElementPageFragment {

        @Root
        private WebElement root;

        @FindBy(className = "make-stale")
        private WebElement makeStale;

        public void makeStale() {
            makeStale.click();
        }

        public WebElement getRoot() {
            return root;
        }

    }

    public static class StaleRootPageFragment {

        @FindBy(className = "in-stale")
        private WebElement inStale;

        public WebElement getInStale() {
            return inStale;
        }
    }

}
