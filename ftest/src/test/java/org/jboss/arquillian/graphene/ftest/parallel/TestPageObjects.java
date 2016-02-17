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
package org.jboss.arquillian.graphene.ftest.parallel;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.graphene.page.document.Document;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Assert;
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
        assertPagesNotNull(page1, page2, pageDefault);
    }

    @Test
    public void testMethodParamsNotNull(@Page @Browser1 SimplePage paramPage1, @Page @Browser2 SimplePage paramPage2,
        @Page SimplePage paramPageDefault) {
        assertPagesNotNull(paramPage1, paramPage2, paramPageDefault);
    }

    @Test
    public void testHeadersViaAttributes() {
        assertHeadersViaAttributes(page1, page2, pageDefault);
    }

    @Test
    public void testMethodParamsHeadersViaAttributes(@Page @Browser1 SimplePage paramPage1,
        @Page @Browser2 SimplePage paramPage2, @Page SimplePage paramPageDefault) {
        assertHeadersViaAttributes(paramPage1, paramPage2, paramPageDefault);
    }

    @Test
    public void testHeadersViaMethod() {
        assertHeadersViaMethod(page1, page2, pageDefault);
    }

    @Test
    public void testMethodParamsHeadersViaMethod(@Page @Browser1 SimplePage paramPage1,
        @Page @Browser2 SimplePage paramPage2, @Page SimplePage paramPageDefault) {
        assertHeadersViaMethod(paramPage1, paramPage2, paramPageDefault);
    }

    @Test
    public void testDroneInPageObjects() {
        assertDroneInPageObjects(page1, page2, pageDefault);
    }

    @Test
    public void testMethodParamDroneInPageObjects(@Page @Browser1 SimplePage paramPage1,
        @Page @Browser2 SimplePage paramPage2, @Page SimplePage paramPageDefault) {
        assertDroneInPageObjects(paramPage1, paramPage2, paramPageDefault);
    }

    @Test
    public void testJavaScriptInterfaceInPageObjects() {
        assertJavaScriptInterfaceInPageObjects(page1, page2, pageDefault);
    }

    @Test
    public void testMethodParamJavaScriptInterfaceInPageObjects(@Page @Browser1 SimplePage paramPage1,
        @Page @Browser2 SimplePage paramPage2, @Page SimplePage paramPageDefault) {
        assertJavaScriptInterfaceInPageObjects(paramPage1, paramPage2, paramPageDefault);
    }

    @Test
    public void testJavaScriptExecutorInPageObjects() {
        assertJavaScriptExecutorInPageObjects(page1, page2, pageDefault);
    }

    @Test
    public void testMethodParamJavaScriptExecutorInPageObjects(@Page @Browser1 SimplePage paramPage1,
        @Page @Browser2 SimplePage paramPage2, @Page SimplePage paramPageDefault) {
        assertJavaScriptExecutorInPageObjects(paramPage1, paramPage2, paramPageDefault);
    }

    @Test
    public void testGrapheneContextQualifier() {
        assertGrapheneContextQualifier(page1, page2, pageDefault);
    }

    @Test
    public void testMethodParamGrapheneContextQualifier(@Page @Browser1 SimplePage paramPage1,
        @Page @Browser2 SimplePage paramPage2, @Page SimplePage paramPageDefault) {
        assertGrapheneContextQualifier(paramPage1, paramPage2, paramPageDefault);
    }

    private void assertPagesNotNull(SimplePage assertPage1, SimplePage assertPage2, SimplePage assertPageDefault) {
        Assert.assertNotNull(assertPage1);
        Assert.assertNotNull(assertPage2);
        Assert.assertNotNull(assertPageDefault);
    }

    public void assertHeadersViaAttributes(SimplePage assertPage1, SimplePage assertPage2,
        SimplePage assertPageDefault) {

        Assert.assertNotNull(assertPage1.header);
        Assert.assertNotNull(assertPage2.header);
        Assert.assertNotNull(assertPageDefault.header);

        Assert.assertEquals("Page 1", assertPage1.header.getText().trim());
        Assert.assertEquals("Page 2", assertPage2.header.getText().trim());
        Assert.assertEquals("Page Default", assertPageDefault.header.getText().trim());
    }

    public void assertHeadersViaMethod(SimplePage assertPage1, SimplePage assertPage2, SimplePage assertPageDefault) {
        Assert.assertNotNull(assertPage1.header());
        Assert.assertNotNull(assertPage2.header());
        Assert.assertNotNull(assertPageDefault.header());

        Assert.assertEquals("Page 1", assertPage1.header().getText().trim());
        Assert.assertEquals("Page 2", assertPage2.header().getText().trim());
        Assert.assertEquals("Page Default", assertPageDefault.header().getText().trim());
    }

    public void assertDroneInPageObjects(SimplePage assertPage1, SimplePage assertPage2, SimplePage assertPageDefault) {
        String url1 = browser1.getCurrentUrl();
        String url2 = browser2.getCurrentUrl();
        String urlDefault = browserDefault.getCurrentUrl();

        Assert.assertNotNull(assertPage1.browser());
        Assert.assertNotNull(assertPage2.browser());
        Assert.assertNotNull(assertPageDefault.browser());

        Assert.assertEquals(url1, assertPage1.getCurrentURL());
        Assert.assertEquals(url2, assertPage2.getCurrentURL());
        Assert.assertEquals(urlDefault, assertPageDefault.getCurrentURL());
    }

    public void assertJavaScriptInterfaceInPageObjects(SimplePage assertPage1, SimplePage assertPage2,
        SimplePage assertPageDefault) {
        Assert.assertEquals("Page 1", assertPage1.getHeaderTextViaJavaScriptInterface().trim());
        Assert.assertEquals("Page 2", assertPage2.getHeaderTextViaJavaScriptInterface().trim());
        Assert.assertEquals("Page Default", assertPageDefault.getHeaderTextViaJavaScriptInterface().trim());
    }

    public void assertJavaScriptExecutorInPageObjects(SimplePage assertPage1, SimplePage assertPage2,
        SimplePage assertPageDefault) {
        Assert.assertEquals("Page 1", assertPage1.getTitleViaJavaScriptExecutor().trim());
        Assert.assertEquals("Page 2", assertPage2.getTitleViaJavaScriptExecutor().trim());
        Assert.assertEquals("Page Default", assertPageDefault.getTitleViaJavaScriptExecutor().trim());
    }

    public void assertGrapheneContextQualifier(SimplePage assertPage1, SimplePage assertPage2,
        SimplePage assertPageDefault) {
        Assert.assertEquals(Browser1.class, assertPage1.getQualifier());
        Assert.assertEquals(Browser2.class, assertPage2.getQualifier());
        Assert.assertEquals(Default.class, assertPageDefault.getQualifier());
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
