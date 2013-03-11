package org.jboss.arquillian.graphene.enricher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@RunWith(Arquillian.class)
public class TestInitializeEmptyFindBy {

    @Drone
    private WebDriver browser;

    @FindBy
    private WebElement divWebElement;

    @FindBy
    private SpanFragment spanFragment;

    @FindBy
    private Select selectElement;

    @FindBy
    private WebElement nameOfInputElement;

    @Page
    private PageObject pageObjectWithSeleniumFindBys;

    @Before
    public void loadPage() {
        URL page = this.getClass().getClassLoader()
                .getResource("org/jboss/arquillian/graphene/ftest/enricher/empty-findby.html");
        browser.get(page.toString());
    }

    @Test
    public void testWebElementById() {
        checkWebElementById(divWebElement);
    }

    @Test
    public void testWebElementByName() {
        checkWebElementByName(nameOfInputElement);
    }

    @Test
    public void testPageFragmentById() {
        checkPageFragmentById(spanFragment);
    }

    @Test
    public void testSelectById() {
        checkSelectById(selectElement);
    }

    @Test
    public void testSeleniumFindBy() {
        checkPageFragmentById(pageObjectWithSeleniumFindBys.spanFragment);
        checkSelectById(pageObjectWithSeleniumFindBys.selectElement);
        checkWebElementById(pageObjectWithSeleniumFindBys.divWebElement);
        checkWebElementByName(pageObjectWithSeleniumFindBys.nameOfInputElement);
    }

    public class PageObject {
        @org.openqa.selenium.support.FindBy
        public WebElement divWebElement;

        @org.openqa.selenium.support.FindBy
        public SpanFragment spanFragment;

        @org.openqa.selenium.support.FindBy
        public Select selectElement;

        @org.openqa.selenium.support.FindBy
        public WebElement nameOfInputElement;
    }

    public class SpanFragment {

        @FindBy(tagName = "span")
        private List<WebElement> span;

        public List<WebElement> getSpan() {
            return span;
        }
    }

    private void checkWebElementById(WebElement element) {
        assertNotNull(element);
        assertEquals("WebElement content", element.getText());
    }

    private void checkWebElementByName(WebElement element) {
        assertNotNull(element);
        String expected = "Test";
        element.sendKeys(expected);
        assertEquals(expected, element.getAttribute("value"));
    }

    private void checkPageFragmentById(SpanFragment fragment) {
        assertNotNull(fragment);
        assertEquals("1", fragment.getSpan().get(0).getText());
    }

    private void checkSelectById(Select select) {
        assertNotNull(select);
        select.selectByIndex(1);
        assertEquals("two", select.getFirstSelectedOption().getText());
    }
}
