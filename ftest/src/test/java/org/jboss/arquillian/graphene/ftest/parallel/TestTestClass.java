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
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.graphene.page.document.Document;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import qualifier.Browser1;
import qualifier.Browser2;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class TestTestClass extends AbstractParallelTest {

    @Browser1
    @JavaScript
    private Document document1;

    @Browser2
    @JavaScript
    private Document document2;

    @JavaScript
    private Document documentDefault;

    @Browser1
    @ArquillianResource
    private JavascriptExecutor javaScriptExecutor1;

    @Browser2
    @ArquillianResource
    private JavascriptExecutor javaScriptExecutor2;

    @ArquillianResource
    private JavascriptExecutor javaScriptExecutorDefault;

    @Browser1
    @ArquillianResource
    private GrapheneContext context1;

    @Browser2
    @ArquillianResource
    private GrapheneContext context2;

    @ArquillianResource
    private GrapheneContext contextDefault;

    @Test
    public void testJavaScriptNotNull() {
        Assert.assertNotNull(document1);
        Assert.assertNotNull(document2);
        Assert.assertNotNull(documentDefault);
    }

    @Test
    public void testDroneNotNull() {
        Assert.assertNotNull(browser1);
        Assert.assertNotNull(browser2);
        Assert.assertNotNull(browserDefault);
    }

    @Test
    public void testArquillianResourcesNotNull() {
        Assert.assertNotNull(javaScriptExecutor1);
        Assert.assertNotNull(javaScriptExecutor2);
        Assert.assertNotNull(javaScriptExecutorDefault);
    }

    @Test
    public void testJavaScriptInterfaceInvocation() {
        Assert.assertEquals(1, document1.getElementsByTagName("h1").size());
        Assert.assertEquals(1, document2.getElementsByTagName("h1").size());
        Assert.assertEquals(1, documentDefault.getElementsByTagName("h1").size());
        Assert.assertEquals("Page 1", document1.getElementsByTagName("h1").get(0).getText());
        Assert.assertEquals("Page 2", document2.getElementsByTagName("h1").get(0).getText());
        Assert.assertEquals("Page Default", documentDefault.getElementsByTagName("h1").get(0).getText());
    }

    @Test
    public void testJavaScriptExecutorInvocation() {
        String title1 = (String) javaScriptExecutor1.executeScript("return document.title");
        String title2 = (String) javaScriptExecutor2.executeScript("return document.title");
        String titleDefault = (String) javaScriptExecutorDefault.executeScript("return document.title");

        Assert.assertEquals("Page 1", title1);
        Assert.assertEquals("Page 2", title2);
        Assert.assertEquals("Page Default", titleDefault);
    }

    @Test
    public void testOpenPage() {
        WebElement header1 = browser1.findElement(By.tagName("h1"));
        WebElement header2 = browser2.findElement(By.tagName("h1"));
        WebElement headerDefault = browserDefault.findElement(By.tagName("h1"));
        Assert.assertNotNull(header1);
        Assert.assertNotNull(header2);
        Assert.assertNotNull(headerDefault);
        Assert.assertEquals("Page 1", header1.getText().trim());
        Assert.assertEquals("Page 2", header2.getText().trim());
        Assert.assertEquals("Page Default", headerDefault.getText().trim());
    }

    @Test
    public void testGrapheneContextQualifier() {
        Assert.assertEquals(Browser1.class, context1.getQualifier());
        Assert.assertEquals(Browser2.class, context2.getQualifier());
        Assert.assertEquals(Default.class, contextDefault.getQualifier());
    }
}
