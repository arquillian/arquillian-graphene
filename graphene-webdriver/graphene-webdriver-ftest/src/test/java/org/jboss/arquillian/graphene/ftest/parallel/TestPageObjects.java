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
package org.jboss.arquillian.graphene.ftest.parallel;

import junit.framework.Assert;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.FindBy;
import qualifier.Browser1;
import qualifier.Browser2;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class TestPageObjects extends AbstractParallelTest {

    @Page
    @Browser1
    private SimplePage page1;

    @Page
    @Browser2
    private SimplePage page2;

    @Page
    private SimplePage pageDefault;

    @Test
    public void testNotNull() {
        Assert.assertNotNull(page1);
        Assert.assertNotNull(page2);
        Assert.assertNotNull(pageDefault);
    }

    @Test
    public void testHeadersViaAttributes() {
        Assert.assertNotNull(page1.header);
        Assert.assertNotNull(page2.header);
        Assert.assertNotNull(pageDefault.header);

        Assert.assertEquals("Page 1", page1.header.getText().trim());
        Assert.assertEquals("Page 2", page2.header.getText().trim());
        Assert.assertEquals("Page Default", pageDefault.header.getText().trim());
    }

    @Test
    public void testHeadersViaMethod() {
        Assert.assertNotNull(page1.header());
        Assert.assertNotNull(page2.header());
        Assert.assertNotNull(pageDefault.header());

        Assert.assertEquals("Page 1", page1.header().getText().trim());
        Assert.assertEquals("Page 2", page2.header().getText().trim());
        Assert.assertEquals("Page Default", pageDefault.header().getText().trim());
    }

    @Test
    public void testDroneInPageObjects() {
        String url1 = browser1.getCurrentUrl();
        String url2 = browser2.getCurrentUrl();
        String urlDefault = browserDefault.getCurrentUrl();

        Assert.assertNotNull(page1.browser());
        Assert.assertNotNull(page2.browser());
        Assert.assertNotNull(pageDefault.browser());

        Assert.assertEquals(url1, page1.getCurrentURL());
        Assert.assertEquals(url2, page2.getCurrentURL());
        Assert.assertEquals(urlDefault, pageDefault.getCurrentURL());
    }

    @Test
    public void testJavaScriptInterfaceInPageObjects() {
        Assert.assertEquals("Page 1", page1.getHeaderTextViaJavaScriptInterface().trim());
        Assert.assertEquals("Page 2", page2.getHeaderTextViaJavaScriptInterface().trim());
        Assert.assertEquals("Page Default", pageDefault.getHeaderTextViaJavaScriptInterface().trim());
    }

    @Test
    public void testJavaScriptExecutorInPageObjects() {
        Assert.assertEquals("Page 1", page1.getTitleViaJavaScriptExecutor().trim());
        Assert.assertEquals("Page 2", page2.getTitleViaJavaScriptExecutor().trim());
        Assert.assertEquals("Page Default", pageDefault.getTitleViaJavaScriptExecutor().trim());
    }

    @Test
    public void testGrapheneContextQualifier() {
        Assert.assertEquals(Browser1.class, page1.getQualifier());
        Assert.assertEquals(Browser2.class, page2.getQualifier());
        Assert.assertEquals(Default.class, pageDefault.getQualifier());
    }

    public static class SimplePage {

        @FindBy(tagName="h1")
        private WebElement header;

        @JavaScript
        private Document document;

        @ArquillianResource
        private JavascriptExecutor javascriptExecutor;

        @ArquillianResource
        private GrapheneContext context;

        @Drone
        private WebDriver browser;

        public String getCurrentURL() {
            return browser.getCurrentUrl();
        }

        public String getTitleViaJavaScriptExecutor() {
            Object title = javascriptExecutor.executeScript("return document.title");
            if (title == null) {
                return null;
            } else {
                return (String) title;
            }
        }

        public String getHeaderTextViaJavaScriptInterface() {
            return document.getElementsByTagName("h1").get(0).getText().trim();
        }

        public Class<?> getQualifier() {
            return context.getQualifier();
        }

        public WebElement header() {
            return header;
        }

        public WebDriver browser() {
            return browser;
        }
    }

}
