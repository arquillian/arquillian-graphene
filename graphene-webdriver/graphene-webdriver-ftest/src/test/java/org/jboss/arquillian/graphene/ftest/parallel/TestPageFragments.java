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
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import qualifier.Browser1;
import qualifier.Browser2;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class TestPageFragments extends AbstractParallelTest {

    @Browser1
    @FindBy(tagName="body")
    private PageFragment pageFragment1;

    @Browser2
    @FindBy(tagName="body")
    private PageFragment pageFragment2;

    @FindBy(tagName="body")
    private PageFragment pageFragmentDefault;

    @Test
    public void testNotNull() {
        Assert.assertNotNull(pageFragment1);
        Assert.assertNotNull(pageFragment2);
        Assert.assertNotNull(pageFragmentDefault);
    }

    @Test
    public void testHeadersViaAttributes() {
        Assert.assertNotNull(pageFragment1.header);
        Assert.assertNotNull(pageFragment1.header);
        Assert.assertNotNull(pageFragmentDefault.header);

        Assert.assertEquals("Page 1", pageFragment1.header.getText().trim());
        Assert.assertEquals("Page 2", pageFragment2.header.getText().trim());
        Assert.assertEquals("Page Default", pageFragmentDefault.header.getText().trim());
    }

    @Test
    public void testHeadersViaMethod() {
        Assert.assertNotNull(pageFragment1.header());
        Assert.assertNotNull(pageFragment2.header());
        Assert.assertNotNull(pageFragmentDefault.header());

        Assert.assertEquals("Page 1", pageFragment1.header().getText().trim());
        Assert.assertEquals("Page 2", pageFragment2.header().getText().trim());
        Assert.assertEquals("Page Default", pageFragmentDefault.header().getText().trim());
    }

    @Test
    public void testDroneInPageFragments() {
        String url1 = browser1.getCurrentUrl();
        String url2 = browser2.getCurrentUrl();
        String urlDefault = browserDefault.getCurrentUrl();

        Assert.assertNotNull(pageFragment1.browser());
        Assert.assertNotNull(pageFragment2.browser());
        Assert.assertNotNull(pageFragmentDefault.browser());

        Assert.assertEquals(url1, pageFragment1.getCurrentURL());
        Assert.assertEquals(url2, pageFragment2.getCurrentURL());
        Assert.assertEquals(urlDefault, pageFragmentDefault.getCurrentURL());
    }

    @Test
    public void testJavaScriptInterfaceInPageFragments() {
        Assert.assertEquals("Page 1", pageFragment1.getHeaderTextViaJavaScriptInterface().trim());
        Assert.assertEquals("Page 2", pageFragment2.getHeaderTextViaJavaScriptInterface().trim());
        Assert.assertEquals("Page Default", pageFragmentDefault.getHeaderTextViaJavaScriptInterface().trim());
    }

    @Test
    public void testJavaScriptExecutorInPageFragments() {
        Assert.assertEquals("Page 1", pageFragment1.getTitleViaJavaScriptExecutor().trim());
        Assert.assertEquals("Page 2", pageFragment2.getTitleViaJavaScriptExecutor().trim());
        Assert.assertEquals("Page Default", pageFragmentDefault.getTitleViaJavaScriptExecutor().trim());
    }

    @Test
    public void testGrapheneContextQualifier() {
        Assert.assertEquals(Browser1.class, pageFragment1.getQualifier());
        Assert.assertEquals(Browser2.class, pageFragment2.getQualifier());
        Assert.assertEquals(Default.class, pageFragmentDefault.getQualifier());
    }

    public static class PageFragment {

        @JavaScript
        private Document document;

        @Drone
        private WebDriver browser;

        @FindBy(tagName="h1")
        private WebElement header;

        @ArquillianResource
        private JavascriptExecutor javascriptExecutor;

        @ArquillianResource
        private GrapheneContext context;

        public String getCurrentURL() {
            return browser.getCurrentUrl();
        }

        public String getHeaderTextViaJavaScriptInterface() {
            return document.getElementsByTagName("h1").get(0).getText().trim();
        }

        public Class<?> getQualifier() {
            return context.getQualifier();
        }

        public String getTitleViaJavaScriptExecutor() {
            Object title = javascriptExecutor.executeScript("return document.title");
            if (title == null) {
                return null;
            } else {
                return (String) title;
            }
        }

        public WebElement header() {
            return header;
        }

        public WebDriver browser() {
            return browser;
        }

    }

}
