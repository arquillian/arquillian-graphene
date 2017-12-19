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
package org.jboss.arquillian.graphene.ftest.webdriver;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.findby.JQuery;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.ArquillianClassRule;
import org.jboss.arquillian.junit.ArquillianRule;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunAsClient
public class FindElementJUnitRulesTestCase {

    @ClassRule
    public static ArquillianClassRule classRule = new ArquillianClassRule();

    @Rule
    public ArquillianRule rule = new ArquillianRule();

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextRoot;

    @FindBy(className = "make-stale")
    private WebElement makeStale;

    @JavaScript
    JQueryInstallator installator;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
         Resource.inCurrentPackage().find("staleelements.html").loadPage(browser, contextRoot);
    }

    @Test
    public void testFindStaleElementOnWebDriver() {
        WebElement stale = browser.findElement(By.className("stale"));
        Assert.assertTrue(stale.isDisplayed());
        makeStale.click();
        Assert.assertTrue(stale.isDisplayed());
    }

    @Test
    public void testFindStaleElementsOnWebDriver() {
        WebElement stale = browser.findElements(By.className("stale")).get(0);
        makeStale.click();
        Assert.assertTrue(stale.isDisplayed());
    }

    @Test
    public void testFindStaleElementOnWebElement() {
        WebElement stale = browser.findElement(By.tagName("body")).findElement(By.className("stale"));
        makeStale.click();
        Assert.assertTrue(stale.isDisplayed());
    }

    @Test
    public void testFindStaleElementsOnWebElement() {
        WebElement stale = browser.findElement(By.tagName("body")).findElements(By.className("stale")).get(0);
        makeStale.click();
        Assert.assertTrue(stale.isDisplayed());
    }

    @Test
    public void testFindElementOnStaleWebElement() {
        WebElement inStale = browser.findElement(By.className("stale")).findElement(By.className("in-stale"));
        makeStale.click();
        Assert.assertTrue(inStale.isDisplayed());
    }

    @Test
    public void testFindElementsOnStaleWebElement() {
        WebElement inStale = browser.findElement(By.className("stale")).findElements(By.className("in-stale")).get(0);
        makeStale.click();
        Assert.assertTrue(inStale.isDisplayed());
    }

    @Test
    public void testStelenessAndJavascriptOnWebDriver() {
        WebElement stale = browser.findElement(By.className("stale"));
        makeStale.click();
        JavascriptExecutor executor = (JavascriptExecutor) browser;
        installator.install();
        executor.executeScript("Graphene.jQuery(arguments[0]).trigger('blur')", stale);
    }

    @Test
    public void testStelenessAndJavascriptOnWebElement() {
        WebElement inStale = browser.findElement(By.className("stale")).findElement(By.className("in-stale"));
        makeStale.click();
        JavascriptExecutor executor = (JavascriptExecutor) browser;
        installator.install();
        executor.executeScript("Graphene.jQuery(arguments[0]).trigger('blur')", inStale);
    }

    @Test
    public void testStalenessAndActionsOnWebDriver1() {
        WebElement stale = browser.findElement(By.className("stale"));
        Action action = new Actions(browser).clickAndHold(stale).release(stale).build();
        makeStale.click();
        action.perform();
    }

    @Test
    public void testStalenessAndActionsOnWebDriver2() {
        WebElement stale = browser.findElement(By.className("stale"));
        Action action = new Actions(browser).moveToElement(stale).build();
        makeStale.click();
        action.perform();
    }

    @Test
    public void testStalenessAndActionsOnWebElement1() {
        WebElement inStale = browser.findElement(By.className("stale")).findElement(By.className("in-stale"));
        Action action = new Actions(browser).clickAndHold(inStale).release(inStale).build();
        makeStale.click();
        action.perform();
    }

    @Test
    public void testStalenessAndActionsOnWebElement2() {
        WebElement inStale = browser.findElement(By.className("stale")).findElement(By.className("in-stale"));
        Action action = new Actions(browser).moveToElement(inStale).build();
        makeStale.click();
        action.perform();
    }

    @JavaScript("Graphene.jqueryInstallator")
    @Dependency(sources = "org/jboss/arquillian/graphene/ftest/webdriver/jqueryInstallator.js", interfaces = JQuery.class)
    public interface JQueryInstallator {

        Boolean install();

    }

}
