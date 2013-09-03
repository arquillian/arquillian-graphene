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
package org.jboss.arquillian.graphene.ftest.javascript;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class EnrichmentTestCase {

    @ArquillianResource
    private URL contextRoot;

    @Drone
    private WebDriver browser;

    @JavaScript
    private Document document;

    @JavaScript
    private Screen screen;

    @JavaScript
    private Unstable unstable;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inCurrentPackage().find("sample.html").loadPage(browser, contextRoot);
    }

    @Test
    public void testNotNull() {
        Assert.assertNotNull(document);
        Assert.assertNotNull(screen);
    }

    @Test
    public void testDocumentMethods() {
        Assert.assertEquals("Hello World!", document.getElementsByTagName("h1").get(0).getText());
    }

    @Test
    public void testScreenMethods() {
        Assert.assertNotNull(screen.getHeight());
        Assert.assertNotNull(screen.getWidth());
    }

    @Test
    public void testUnstable() {
        try {
            Assert.assertNotNull("The return value can't be null.", unstable.simple());
            Assert.assertNotNull("The return value can't be null.", unstable.simple());
        } catch (RuntimeException e) {
            e.printStackTrace();
            Assert.fail("Can't invoke unstable javascript extension: " + e.getMessage());
        }
    }

    @JavaScript("document")
    public static interface Document {

        String getTitle();

        List<WebElement> getElementsByTagName(String tagName);
    }

    @JavaScript("window.screen")
    public static interface Screen {
        Long getWidth();
        Long getHeight();
    }

    @JavaScript("document.unstable")
    @Dependency(sources = {"org/jboss/arquillian/graphene/ftest/javascript/unstable.js"})
    public static interface Unstable {
        Long simple();
    }

}
