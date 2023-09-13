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

import static org.junit.Assert.assertThat;

import java.net.URL;
import java.util.List;

import org.hamcrest.Matchers;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.findby.ByJQuery;
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
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class TestGrapheneElement {

    @Drone
    private WebDriver browser;

    @FindBy
    private GrapheneElement root;
    @FindBy(id="root")
    private WebElement pureRoot;

    @FindBy
    private GrapheneElement doesntExist;
    @FindBy
    private List<GrapheneElement> doesntExistList;

    @FindBy(tagName="option")
    private List<GrapheneElement> options;
    @FindBy(tagName="option")
    private List<WebElement> pureOptions;

    @JavaScript
    private TestJavascript testJavascript;

    @ArquillianResource
    private Actions actions;

    @ArquillianResource
    private URL contextRoot;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inCurrentPackage().find("sample.html").loadPage(browser, contextRoot);
    }

    @Test
    public void testOneIsPresent() {
        Assert.assertTrue(root.isPresent());
        Assert.assertFalse(doesntExist.isPresent());
    }

    @Test
    public void testOneGetText() {
        Assert.assertEquals(pureRoot.getText().trim(), root.getText().trim());
    }

    @Test
    public void testOneWithJavascript() {
        String inner = testJavascript.getInnerHtml(root);

        // unify
        inner = inner.replaceAll("\"", "").toLowerCase();

        assertThat(inner, Matchers.containsString("<div id=pseudoroot>pseudo root</div>"));
    }

    @Test
    public void testOneWithActions() {
        actions.moveToElement(root).doubleClick().build().perform();
    }

    @Test
    public void testOneFindByTagName() {
        GrapheneElement element = root.findElement(By.tagName("div"));
        Assert.assertEquals("pseudo root", element.getText().trim());
    }

    @Test
    public void testOneFindByCss() {
        GrapheneElement element = root.findElement(By.cssSelector("div"));
        Assert.assertEquals("pseudo root", element.getText().trim());
    }

    @Test
    public void testOneFindByJQuery() {
        GrapheneElement element = root.findElement(ByJQuery.selector("div"));
        Assert.assertEquals("pseudo root", element.getText().trim());
    }

    @Test
    public void testListIsPresent() {
        Assert.assertEquals(3, options.size());
        for (GrapheneElement element: options) {
            Assert.assertTrue(element.isPresent());
        }
        Assert.assertEquals(0, doesntExistList.size());
    }

    @Test
    public void testListGetText() {
        for (int i=0; i<3; i++) {
            Assert.assertEquals(pureOptions.get(i).getText().trim(), options.get(i).getText());
        }
    }

    @Test
    public void testListWithJavascript() {
        for (GrapheneElement element: options) {
            String inner = testJavascript.getInnerHtml(element);
            Assert.assertTrue(inner.contains("option"));
        }
    }

    @JavaScript("document.test")
    @Dependency(sources = {"org/jboss/arquillian/graphene/ftest/enricher/test.js"})
    public interface TestJavascript {

        String getInnerHtml(WebElement element);

    }
}
