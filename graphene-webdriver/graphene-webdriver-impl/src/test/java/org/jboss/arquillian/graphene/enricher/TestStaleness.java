package org.jboss.arquillian.graphene.enricher;

import java.util.List;
import junit.framework.Assert;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.context.TestingDriverStub;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TestStaleness extends AbstractGrapheneEnricherTest {

    @FindBy(id="blah")
    private WebElement element;

    @Before
    public void prepareDriver() {
        GrapheneContext.set(new DriverSometimesProducingStaleElements());
    }

    @Test
    public void testWebElement() {
        getGrapheneEnricher().enrich(this);
        Assert.assertNotNull(element);
        Assert.assertEquals("text", element.getText());
    }

    public static class DriverSometimesProducingStaleElements extends TestingDriverStub {

        private int counter = 0;

        @Override
        public WebElement findElement(By by) {
            counter++;
            if (counter % 2 == 1) {
                return new StaleWebElement();
            } else {
                return new StaleWebElement() {

                    @Override
                    public String getText() {
                        return "text";
                    }

                };
            }
        }

    }

    public static class StaleWebElement implements WebElement {

        @Override
        public void click() {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public void submit() {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public void sendKeys(CharSequence... css) {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public void clear() {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public String getTagName() {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public String getAttribute(String string) {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public boolean isSelected() {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public boolean isEnabled() {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public String getText() {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public List<WebElement> findElements(By by) {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public WebElement findElement(By by) {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public boolean isDisplayed() {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public Point getLocation() {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public Dimension getSize() {
            throw new StaleElementReferenceException("Stale element.");
        }

        @Override
        public String getCssValue(String string) {
            throw new StaleElementReferenceException("Stale element.");
        }

    }

}
