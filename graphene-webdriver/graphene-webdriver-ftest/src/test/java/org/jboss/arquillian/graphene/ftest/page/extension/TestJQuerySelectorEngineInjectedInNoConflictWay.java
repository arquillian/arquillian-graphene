package org.jboss.arquillian.graphene.ftest.page.extension;

import java.net.URL;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.junit.Arquillian;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(Arquillian.class)
public class TestJQuerySelectorEngineInjectedInNoConflictWay {

    private static final String EXPECTED_$_OBJECT_VALUE = "something different than JQuery object";
    private static final String EXPECTED_JQUERY_OBJECT_VALUE = "way different";

    @FindBy(jquery = "#placeholder")
    private WebElement placeholder;

    @FindBy(jquery = ":button")
    private WebElement show$ObjectValue;

    @FindBy(jquery = ":button:eq(1)")
    private WebElement showJQueryObjectValue;

    @Drone
    private WebDriver browser;

    public void loadPage() {
        URL page = this.getClass().getClassLoader()
            .getResource("org/jboss/arquillian/graphene/ftest/page/extension/sampleJQueryLocatorNoConflict.html");
        browser.get(page.toString());
    }

    @Test
    public void test$ObejctWasNotInvadedByIncludedJQuery() {
        loadPage();

        show$ObjectValue.click();
        String placeholderText = placeholder.getText();
        assertEquals("The installed JQuery for sizzle locators invades predefined global variable $!", EXPECTED_$_OBJECT_VALUE,
            placeholderText);
    }

    @Test
    public void testJQueryObjectWasNotInvadedByIncludedJQuery() {
        loadPage();

        showJQueryObjectValue.click();
        String placeholderText = placeholder.getText();
        assertEquals("The installed JQuery for sizzle locators invades predefined global variable jQuery!",
            EXPECTED_JQUERY_OBJECT_VALUE, placeholderText);
    }
}
