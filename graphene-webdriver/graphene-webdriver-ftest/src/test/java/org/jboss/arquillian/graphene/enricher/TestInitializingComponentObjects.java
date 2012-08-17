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
import org.jboss.arquillian.graphene.enricher.page.TestPageObjectsInitialization;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.graphene.spi.components.common.AbstractComponentStub;
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
public class TestInitializingComponentObjects {

    @FindBy(xpath = "//div[@id='rootElement']")
    private AbstractComponentStub abstractComponent;

    @FindBy(xpath = "//input")
    private WebElement input;

    @Page
    private TestPageObjectsInitialization testPage;

    private final String EXPECTED_NESTED_ELEMENT_TEXT = "Some Value";

    @Drone
    WebDriver selenium;

    // @ArquillianResource
    // protected URL contextRoot;

    // private static final String WEB_APP_SRC = "src/test/webapp";

    // @Deployment(testable = false)
    // public static WebArchive deploy() {
    // return ShrinkWrap.create(WebArchive.class, "drone-test.war").addAsWebResource(new File(WEB_APP_SRC + "/index.html"),
    // ArchivePaths.create("index.html"));
    // }

    // @Before
    public void loadPage() {
        URL page = this.getClass().getClassLoader()
            .getResource("org/jboss/arquillian/graphene/ftest/componentObjectsEnricher/sample.html");

        selenium.get(page.toExternalForm());
    }

    @Test
    public void testComponentIsInitialized() {
        loadPage();
        assertNotNull("AbstractComponent should be initialised at this point!", abstractComponent);
    }

    @Test
    public void testComponentHasSetRootCorrectly() {
        loadPage();
        assertEquals("The root was not set correctly!", abstractComponent.invokeMethodOnElementRefByXpath(),
            EXPECTED_NESTED_ELEMENT_TEXT);
    }

    @Test
    public void testPageObjectInitialisedCorrectly() {
        loadPage();
        assertEquals("The page component was not set correctly!", testPage.getAbstractComponent()
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
}
