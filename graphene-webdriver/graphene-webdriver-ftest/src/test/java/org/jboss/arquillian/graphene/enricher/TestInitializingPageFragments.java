/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.enricher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.page.EmbeddedPage;
import org.jboss.arquillian.graphene.enricher.page.TestPage;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Juraj Huska
 */
@RunWith(Arquillian.class)
public class TestInitializingPageFragments {

    @FindBy(xpath = "//div[@id='rootElement']")
    private AbstractPageFragmentStub abstractPageFragmentStub;

    @FindBy(xpath = "//input")
    private WebElement input;

    @Page
    private TestPage testPage;

    private final String EXPECTED_NESTED_ELEMENT_TEXT = "Some Value";

    @Drone
    WebDriver selenium;

    public void loadPage() {
        URL page = this.getClass().getClassLoader()
            .getResource("org/jboss/arquillian/graphene/ftest/pageFragmentsEnricher/sample.html");

        selenium.get(page.toExternalForm());
    }

    //@Test
    public void testpageFragmentIsInitialized() {
        loadPage();
        assertNotNull("AbstractPageFragment should be initialised at this point!", abstractPageFragmentStub);
    }

    //@Test
    public void testPageFragmentHasSetRootCorrectly() {
        loadPage();
        assertEquals("The root was not set correctly!", abstractPageFragmentStub.invokeMethodOnElementRefByXpath(),
            EXPECTED_NESTED_ELEMENT_TEXT);
    }

    //@Test
    public void testPageObjectInitialisedCorrectly() {
        loadPage();
        assertEquals("The page object was not set correctly!", testPage.getAbstractPageFragment()
            .invokeMethodOnElementRefByXpath(), EXPECTED_NESTED_ELEMENT_TEXT);
    }

    //@Test
    public void testOtherWebElementsInitialisedCorrectly() {
        loadPage();
        String EXPECTED_VALUE = "Gooseka";
        input.sendKeys(EXPECTED_VALUE);

        assertEquals("The value of the input is wrong, the element which represents it was not initialised correctly!",
            input.getAttribute("value"), EXPECTED_VALUE);
    }

    // this should be moved to TestInitializingPageObjects test class, which is already in another pull request
    @Test
    public void testEmbeddedPageObjectInitializedCorrectly() {
        loadPage();
        assertEquals("The embedded page was not initialized correctly!", EmbeddedPage.EXPECTED_TEXT_OF_EMBEDDED_ELEM, testPage
            .getEmbeddedPage().invokeMethodOnEmbeddedElement());
    }
}
