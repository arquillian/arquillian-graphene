package org.jboss.arquillian.graphene.ftest.enricher.selenium;

import static org.junit.Assert.assertEquals;

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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;

/**
 * @author Lukas Fryc
 */
@RunWith(Arquillian.class)
@RunAsClient
public class TestSeleniumResourceProvider {

    @ArquillianResource
    private URL contextRoot;

    @Drone
    private WebDriver browser;

    @FindBy(css = "input[type=button]")
    WebElement button;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inCurrentPackage().find("selenium-provider.html").loadPage(browser, contextRoot);
    }

    @Test
    public void testWebDriver(@ArquillianResource WebDriver driver) {
        assertEquals("Sample Page", driver.getTitle());
    }

    @Test
    public void testJavaScriptExecutor(@ArquillianResource JavascriptExecutor executor) {
        executor.executeScript("document.title = arguments[0]", "New Title");
        assertEquals("New Title", browser.getTitle());
    }

    @Test
    public void testMouse(@ArquillianResource Mouse mouse) {
        // when
        mouse.click(((Locatable) button).getCoordinates());
        // then
        assertEquals("Clicked", button.getAttribute("value"));
    }

    @Test
    public void testActions(@ArquillianResource Actions actions) {
        // when
        actions.click(button).perform();
        // then
        assertEquals("Clicked", button.getAttribute("value"));
    }
}
