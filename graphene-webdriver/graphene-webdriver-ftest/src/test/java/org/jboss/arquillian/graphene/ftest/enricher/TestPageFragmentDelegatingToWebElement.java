package org.jboss.arquillian.graphene.ftest.enricher;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.graphene.ftest.enricher.page.fragment.PageFragmentImplementingWebElement;
import org.jboss.arquillian.graphene.spi.annotations.InitialPage;
import org.jboss.arquillian.graphene.spi.annotations.Location;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@RunWith(Arquillian.class)
@RunAsClient
public class TestPageFragmentDelegatingToWebElement {

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
        testPage(page);
    }
    
    @Test
    public void testPageFragmentFromInitialPageIsDelegatingCorrectly(@InitialPage TestPage testedPage) {
        testPage(testedPage);
    }
    
    private void testPage(TestPage testedPage) {
        String expectedText = "test";
        testedPage.getInputFragment().sendKeys(expectedText);
        assertEquals(expectedText, testedPage.getInputFragment().getInputText());
        assertEquals("foo-bar", testedPage.getInputFragment().getStyleClass());
    }

    @Location("resource://" + pageLocation)
    public class TestPage {
        @FindBy(tagName = "input")
        private PageFragmentImplementingWebElement inputFragment;

        public PageFragmentImplementingWebElement getInputFragment() {
            return inputFragment;
        }
    }
}
