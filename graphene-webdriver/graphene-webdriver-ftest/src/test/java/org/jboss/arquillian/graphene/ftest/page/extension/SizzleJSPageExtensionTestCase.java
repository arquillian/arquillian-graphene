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
package org.jboss.arquillian.graphene.ftest.page.extension;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.annotation.ByJQuery;
import org.jboss.arquillian.graphene.spi.annotations.FindBy;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@RunWith(Arquillian.class)
public class SizzleJSPageExtensionTestCase {

    @FindBy(jquery = ":header")
    private WebElement webElementBySizzle;

    @FindBy(jquery = ":header")
    private List<WebElement> listOfWebElementsBySizzle;

    @FindBy(jquery = "div:first")
    private SizzleTestPageFragment sizzleTestPageFragment;

    @FindBy(jquery = "div:first")
    private List<SizzleTestPageFragment> listOfSizzlePageFragments;

    @Drone
    private WebDriver browser;

    private static String EXPECTED_SIZZLE_TEXT = "Hello sizzle locators!";

    public void loadPage() {
        URL page = this.getClass().getClassLoader()
            .getResource("org/jboss/arquillian/graphene/ftest/page/extension/sample.html");
        browser.get(page.toString());
    }

    @Test
    public void testSizzleJSPageExtensionInstalled() {
        loadPage();

        ByJQuery htmlBy = new ByJQuery("html");
        WebElement htmlElement = browser.findElement(htmlBy);

        assertNotNull(htmlElement);
        assertEquals("html", htmlElement.getTagName());
    }

    @Test
    public void testFindByOnWebElementSizzleLocator() {
        loadPage();

        assertNotNull(webElementBySizzle);
        assertEquals("h1", webElementBySizzle.getTagName());
    }

    @Test
    public void testFindByOnListOfWebElementSizzleLocator() {
        loadPage();

        assertNotNull(listOfWebElementsBySizzle);
        assertEquals("h1", listOfWebElementsBySizzle.get(0).getTagName());
    }

    @Test
    public void testFindByOnPageFragmentBySizzleLocator() {
        loadPage();

        assertNotNull(sizzleTestPageFragment);
        assertEquals(EXPECTED_SIZZLE_TEXT, sizzleTestPageFragment.getSizzleLocator().getText());
    }

    @Test
    public void testFindByOnListOfPageFragments() {
        loadPage();

        assertNotNull(listOfSizzlePageFragments);
        assertEquals(EXPECTED_SIZZLE_TEXT, listOfSizzlePageFragments.get(0).getSizzleLocator().getText());
    }

    /* *************
     * Page Fragment
     */
    public class SizzleTestPageFragment {

        @FindBy(jquery = "div:contains(sizzle locators)")
        private WebElement sizzleLocator;

        public WebElement getSizzleLocator() {
            return sizzleLocator;
        }
    }
}
