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
package org.jboss.arquillian.graphene.enricher.pageFragments;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.fragment.AbstractPageFragmentStub;
import org.jboss.arquillian.graphene.enricher.pageFragments.fragments.PageFragmentWithEmbeddedAnotherPageFragmentStub;
import org.jboss.arquillian.graphene.enricher.pageObjects.pages.EmbeddedPage;
import org.jboss.arquillian.graphene.enricher.pageObjects.pages.TestPage;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

/**
 * @author Juraj Huska
 */
@RunWith(Arquillian.class)
public class TestInitializingPageFragments {

    @FindBy(xpath = "//div[@id='rootElement']")
    private AbstractPageFragmentStub abstractPageFragmentStub;

    @FindBy(xpath = "//div[@id='rootElement']")
    private PageFragmentWithEmbeddedAnotherPageFragmentStub pageFragmentWithEmbeddedAnotherPageFragment;

    @FindBy(xpath = "//input")
    private WebElement input;

    @FindBy(className = "divs")
    private List<WebElement> divs;

    @Page
    private TestPage testPage;

    private final String EXPECTED_NESTED_ELEMENT_TEXT = "Some Value";

    @Drone
    private WebDriver selenium;

    public void loadPage() {
        URL page = this.getClass().getClassLoader()
            .getResource("org/jboss/arquillian/graphene/ftest/pageFragmentsEnricher/sample.html");

        selenium.get(page.toExternalForm());
    }

    @Test
    public void testpageFragmentIsInitialized() {
        loadPage();
        assertNotNull("AbstractPageFragment should be initialised at this point!", abstractPageFragmentStub);
    }

    @Test
    public void testPageFragmentHasSetRootCorrectly() {
        loadPage();
        assertEquals("The root was not set correctly!", abstractPageFragmentStub.invokeMethodOnElementRefByXpath(),
            EXPECTED_NESTED_ELEMENT_TEXT);
    }

    @Test
    public void testPageObjectInitialisedCorrectly() {
        loadPage();
        assertEquals("The page object was not set correctly!", testPage.getAbstractPageFragment()
            .invokeMethodOnElementRefByXpath(), EXPECTED_NESTED_ELEMENT_TEXT);
    }

    @Test
    public void testOtherWebElementsInitialisedCorrectly() {
        loadPage();
        String EXPECTED_VALUE = "Gooseka";
        input.sendKeys(EXPECTED_VALUE);

        assertEquals("The value of the input is wrong, the element which represents it was not initialised correctly!",
            input.getAttribute("value"), EXPECTED_VALUE);
    }

    @Test
    public void testEmbeddedPageObjectInitializedCorrectly() {
        loadPage();
        assertEquals("The embedded page was not initialized correctly!", EmbeddedPage.EXPECTED_TEXT_OF_EMBEDDED_ELEM, testPage
            .getEmbeddedPage().invokeMethodOnEmbeddedElement());
    }

    @Test
    public void testInitializeListOfWebElementsInjectedToTests() {
        loadPage();

        checkInitializationOfWebElements(divs, "Outside PageFragment");
    }

    @Test
    public void testInitializeListOfWebElementsInjectedToPageFragments() {
        loadPage();

        checkInitializationOfWebElements(abstractPageFragmentStub.getSpansInPageFragment(), "Inside PageFragment");
    }

    @Test
    public void testInitializeListOfWebElementsInjectedToPageObject() {
        loadPage();

        checkInitializationOfWebElements(testPage.getParagraphs(), "Inside PageObject");
    }

    @Test
    public void testSupportForAdvancedActions() {
        loadPage();

        WebDriver driver = GrapheneContext.getProxyForInterfaces(HasInputDevices.class);
        Actions builder = new Actions(driver);

        // following tests usage of Actions with injected plain WebElement
        builder.click(input);
        // following with List<WebElement>
        builder.click(divs.get(0));
        // following with WebElements from Page Fragments
        builder.click(abstractPageFragmentStub.getLocatorRefByXPath());
        // following with List of WebElements from Page Fragments
        builder.click(abstractPageFragmentStub.getSpansInPageFragment().get(0));

        builder.perform();
    }

    @Test
    public void testInitializationOfEmbeddedPageFragmentsInOtherPageFragments() {
        loadPage();

        WebElement element = pageFragmentWithEmbeddedAnotherPageFragment.getEmbeddedPageFragment().getLocatorRefByClassName();

        assertEquals("The Page Fragment ebmedded in another Page Fragment was not initialized correctly!", element.getText(),
            "Value of element in embedded page fragment");
    }

    private void checkInitializationOfWebElements(List<WebElement> webElements, String expectedValueOfWebElements) {
        assertNotNull("The list of WebElements was not initialized correctly!", webElements);

        for (int i = 1; i <= 3; i++) {
            WebElement webElement = null;
            try {
                webElement = webElements.get(i - 1);
            } catch (IndexOutOfBoundsException ex) {
                fail("The List<WebElement> was not initialized correclty! " + ex);
                return;
            }
            assertEquals("The WebElement number " + i + " from list was not initialized correctly!", expectedValueOfWebElements
                + " " + String.valueOf(i), webElement.getText());
        }
    }
}
