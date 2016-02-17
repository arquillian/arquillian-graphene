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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.graphene.ftest.enricher.page.fragment.PageFragmentExtendingPageFragment;
import org.jboss.arquillian.graphene.ftest.enricher.page.fragment.PageFragmentImplementingGrapheneElement;
import org.jboss.arquillian.graphene.ftest.enricher.page.fragment.PageFragmentImplementingWebElement;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Location;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@RunWith(Arquillian.class)
@RunAsClient
public class TestDelegatingPageFragment {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextRoot;

    @Page
    private TestPage page;

    private static final String pageLocation = "org/jboss/arquillian/graphene/ftest/enricher/delegating-pagefragment.html";

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().find("delegating-pagefragment.html").buildWar("deployment.war");
    }

    @Test
    public void testPageFragmentMethodIsDelegatingCorrectly() {
        browser.get(contextRoot + pageLocation);
        testPageFragment(page.getInputFragment());
    }

    @Test
    public void testPageFragmentMethodIsDelegatingCorrectlyMethodParam(@Page TestPage pageParam) {
        browser.get(contextRoot + pageLocation);
        testPageFragment(pageParam.getInputFragment());
    }

    @Test
    public void testExtendingPageFragmentMethodIsDelegatingCorrectly() {
        browser.get(contextRoot + pageLocation);
        testPageFragment(page.getInputExtendedFragment());
    }

    @Test
    public void testExtendingPageFragmentMethodIsDelegatingCorrectlyMethodParam(@Page TestPage pageParam) {
        browser.get(contextRoot + pageLocation);
        testPageFragment(pageParam.getInputExtendedFragment());
    }

    @Test
    public void testPageFragmentImplementingGrapheneElement() {
        checkPageFragmentImplementingGrapheneElement(page);
    }

    @Test
    public void testPageFragmentImplementingGrapheneElementMethodParam(@Page TestPage pageParam) {
        checkPageFragmentImplementingGrapheneElement(pageParam);
    }

    @Test
    public void testPageFragmentFromInitialPageIsDelegatingCorrectly(@InitialPage TestPage testedPage) {
        testPageFragment(testedPage.getInputFragment());
    }

    @Test(expected = NoSuchElementException.class)
    public void testPageFragmentUnwrapsInvocationTargetException() {
        page.notExisting.isDisplayed();
    }

    @Test(expected = NoSuchElementException.class)
    public void testPageFragmentUnwrapsInvocationTargetExceptionMethodParam(@Page TestPage pageParam) {
        pageParam.notExisting.isDisplayed();
    }

    private void checkPageFragmentImplementingGrapheneElement(TestPage pageToCheck) {
        browser.get(contextRoot + pageLocation);
        assertTrue(pageToCheck.outputFragment.isPresent());
        assertEquals("foo-bar", pageToCheck.outputFragment.getStyleClass());
        assertEquals(pageToCheck.outputFragment.getOutputText(), pageToCheck.outputFragment.getText());
    }

    private void testPageFragment(PageFragmentImplementingWebElement fragment) {
        String expectedText = "test";
        fragment.sendKeys(expectedText);
        assertEquals(expectedText, fragment.getInputText());
        assertEquals("foo-bar", fragment.getStyleClass());
    }

    @Location(pageLocation)
    public class TestPage {
        @FindBy(tagName = "input")
        private PageFragmentImplementingWebElement inputFragment;

        @FindBy(tagName = "p")
        private PageFragmentImplementingGrapheneElement outputFragment;

        @FindBy(id = "notExisting")
        private PageFragmentImplementingWebElement notExisting;

        @FindBy(className = "foo-bar")
        private PageFragmentExtendingPageFragment inputExtendedFragment;

        public PageFragmentImplementingWebElement getInputFragment() {
            return inputFragment;
        }

        public PageFragmentExtendingPageFragment getInputExtendedFragment() {
            return inputExtendedFragment;
        }
    }
}
