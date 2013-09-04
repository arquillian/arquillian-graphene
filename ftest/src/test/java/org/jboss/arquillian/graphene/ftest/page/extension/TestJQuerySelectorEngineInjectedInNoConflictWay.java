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
package org.jboss.arquillian.graphene.ftest.page.extension;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.FindByJQuery;
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

@RunWith(Arquillian.class)
@RunAsClient
public class TestJQuerySelectorEngineInjectedInNoConflictWay {

    private static final String EXPECTED_$_OBJECT_VALUE = "something different than JQuery object";
    private static final String EXPECTED_JQUERY_OBJECT_VALUE = "way different";

    @ArquillianResource
    private URL contextRoot;

    @FindByJQuery("#placeholder")
    private WebElement placeholder;

    @FindByJQuery(":button")
    private WebElement show$ObjectValue;

    @FindByJQuery(":button:eq(1)")
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
