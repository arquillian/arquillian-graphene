package org.jboss.arquillian.graphene.ftest.page.extension;

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(Arquillian.class)
@RunAsClient
public class TestJQuerySelectorEngineInjectedInNoConflictWay {

    private static final String EXPECTED_$_OBJECT_VALUE = "something different than JQuery object";
    private static final String EXPECTED_JQUERY_OBJECT_VALUE = "way different";

    @ArquillianResource
    private URL contextRoot;

    @FindBy(jquery = "#placeholder")
    private WebElement placeholder;

    @FindBy(jquery = ":button")
    private WebElement show$ObjectValue;

    @FindBy(jquery = ":button:eq(1)")
    private WebElement showJQueryObjectValue;

    @Drone
    private WebDriver browser;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inCurrentPackage().find("sampleJQueryLocatorNoConflict.html").loadPage(browser, contextRoot);
    }

    @Test
    public void test$ObejctWasNotInvadedByIncludedJQuery() {
        show$ObjectValue.click();
        String placeholderText = placeholder.getText();
        assertEquals("The installed JQuery for sizzle locators invades predefined global variable $!", EXPECTED_$_OBJECT_VALUE,
            placeholderText);
    }

    @Test
    public void testJQueryObjectWasNotInvadedByIncludedJQuery() {
        showJQueryObjectValue.click();
        String placeholderText = placeholder.getText();
        assertEquals("The installed JQuery for sizzle locators invades predefined global variable jQuery!",
            EXPECTED_JQUERY_OBJECT_VALUE, placeholderText);
    }
}
