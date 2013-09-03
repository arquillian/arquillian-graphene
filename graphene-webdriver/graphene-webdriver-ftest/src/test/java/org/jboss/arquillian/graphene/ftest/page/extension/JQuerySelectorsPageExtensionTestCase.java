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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class JQuerySelectorsPageExtensionTestCase {

    @ArquillianResource
    private URL contextRoot;

    @FindBy(jquery = ":header")
    private WebElement webElementByJQuery;

    @FindBy(jquery = ":header")
    private List<WebElement> listOfWebElementsByJQuery;

    @FindBy(jquery = "div:eq(1)")
    private JQuerySelectorTestPageFragment jquerySelectorTestPageFragment;

    @FindBy(jquery = "div:eq(1)")
    private List<JQuerySelectorTestPageFragment> listOfJQueryPageFragments;

    @FindBy(jquery = "#nonExistingId")
    private WebElement notExistingElement;

    @FindBy(jquery = "#nonExistingId")
    private List<WebElement> notExistingElements;

    private static final String contentOfSpecialCharacters = "special chars '\"$";
    @FindBy(jquery = "p:contains(\"" + contentOfSpecialCharacters + "\")")
    private WebElement escapedDoubleQuotes;

    @FindBy(jquery = "div[id=\"foo:bar\"]")
    private WebElement escapedDoubleQuotes2;

    @Drone
    private WebDriver browser;

    private static final String EXPECTED_JQUERY_TEXT_1 = "Hello jquery selectors!";
    private static final String EXPECTED_JQUERY_TEXT_2 = "Nested div with foo class.";
    private static final String EXPECTED_NO_SUCH_EL_EX_MSG = "Cannot locate element using";
    private static final String EXPECTED_WRONG_SELECTOR_MSG = "Check out whether it is correct!";

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inCurrentPackage().find("sampleJQueryLocator.html").loadPage(browser, contextRoot);
    }

    @Test
    public void testFindByWrongSelector() {
        try {
            browser.findElement(ByJQuery.selector(":notExistingSelector"));
        } catch (WebDriverException ex) {
            // desired state
            assertTrue("The exception thrown after locating element by non existing selector is wrong!", ex.getMessage()
                .contains(EXPECTED_WRONG_SELECTOR_MSG));
            return;
        }

        fail("There should be webdriver exception thrown when locating element by wrong selector!");
    }

    @Test
    public void testFindNonExistingElement() {
        try {
            browser.findElement(ByJQuery.selector(":contains('non existing string')"));
        } catch (NoSuchElementException ex) {
            // this is desired state
            assertTrue("Error message of NoSuchElementException is wrong! ", ex.getMessage()
                .contains(EXPECTED_NO_SUCH_EL_EX_MSG));
            return;
        }

        fail("There was not thrown NoSuchElementException when trying to locate non existed element!");
    }

    @Test
    public void testFindingWebElementFromAnotherWebElement() {
        WebElement root = browser.findElement(ByJQuery.selector("#root:visible"));

        WebElement div = root.findElement(ByJQuery.selector(".foo:visible"));

        assertNotNull("The div element should be found!", div);
        assertEquals("The element was not referenced from parent WebElement correctly!", EXPECTED_JQUERY_TEXT_2, div.getText());
    }

    @Test
    public void testJQuerySelectorCallingFindByDirectly() {
        ByJQuery headerBy = new ByJQuery(":header");
        WebElement headerElement = browser.findElement(headerBy);

        assertNotNull(headerElement);
        assertEquals("h1", headerElement.getTagName());
    }

    @Test
    public void testFindByOnWebElement() {
        assertNotNull(webElementByJQuery);
        assertEquals("h1", webElementByJQuery.getTagName());
    }

    @Test
    public void testFindByOnListOfWebElement() {
        assertNotNull(listOfWebElementsByJQuery);
        assertEquals("h1", listOfWebElementsByJQuery.get(0).getTagName());
    }

    @Test
    public void testFindByOnPageFragment() {
        assertNotNull(jquerySelectorTestPageFragment);
        assertEquals(EXPECTED_JQUERY_TEXT_1, jquerySelectorTestPageFragment.getJQueryLocator().getText());
    }

    @Test
    public void testFindByOnListOfPageFragments() {
        assertNotNull(listOfJQueryPageFragments);
        assertEquals(EXPECTED_JQUERY_TEXT_1, listOfJQueryPageFragments.get(0).getJQueryLocator().getText());
    }

    @Test(expected = NoSuchElementException.class)
    public void testFindNotExistingWebElement() {
        @SuppressWarnings("unused")
        String text = notExistingElement.getText();
    }

    @Test
    public void testFindNonExistingWebElements() {
        assertEquals("When locating not existing elements an empty list should be returned!", 0, notExistingElements.size());
    }

    @Test
    public void testEscapedDoubleQuotesSelector() {
        String actual = escapedDoubleQuotes.getText().trim();
        assertEquals("WebElement referenced by ecaped locator was not found correctly!", contentOfSpecialCharacters, actual);
    }

    @Test
    public void testEscapedColonSelector() {
        String actual = escapedDoubleQuotes2.getText().trim();
        assertEquals("WebElement with locator containing escaped colon not located correctly!", "Some content", actual);
    }

    /* *************
     * Page Fragment
     */
    public class JQuerySelectorTestPageFragment {

        @FindBy(jquery = "div:contains('jquery selectors')")
        private WebElement jqueryLocator;

        public WebElement getJQueryLocator() {
            return jqueryLocator;
        }
    }
}
