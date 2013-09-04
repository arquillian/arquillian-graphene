/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.ftest.enricher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class TestInitializeFindBys {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextRoot;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inCurrentPackage().find("findbys.html").loadPage(browser, contextRoot);
    }

    private static final String EXPECTED = "correct";

    @FindBys({ @FindBy(css = "#root"), @FindBy(tagName = "div"), @FindBy(tagName = "span") })
    private WebElement element;

    @FindBys({ @FindBy(css = "#root"), @FindBy(css = ".list") })
    private List<WebElement> elements;

    @Test
    public void testWebDriverFindBysOverWebElementInitialized() {
        assertFindBysOverWebElement(element);
    }

    private void assertFindBysOverWebElement(WebElement element) {
        assertNotNull("Element cannot be null! @FindBys was not initialized correctly!", element);
        assertEquals("Element location determined by @FindBys was not correct!", EXPECTED, element.getText());
    }

    @Test
    public void testWebDriverFindBysOverListInitialized() {
        assertFindBysOverList(elements);
    }

    private void assertFindBysOverList(List<WebElement> elements) {
        assertNotNull(elements);
        for (int i = 1; i < 4; i++) {
            assertEquals(i, (int) Integer.valueOf(elements.get(i - 1).getText()));
        }
    }
}
