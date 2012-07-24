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
package org.jboss.arquillian.graphene.junit.ftest;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import org.jboss.arquillian.graphene.context.GrapheneConfigurationContext;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assume;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import static org.jboss.arquillian.graphene.Graphene.*;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class ConditionsTest {

    @Drone
    private WebDriver browser;

    @FindBy(name="q")
    private WebElement input;
    @FindBy(id="search")
    private WebElement search;
    @FindBy(id="topstuff")
    private WebElement topStuff;
    @FindBy(name="btnI")
    private WebElement tryLuck;

    @Test
    public void testElementIsDisplayed() {
        init(browser);
        browser.get("http://google.com");
        waitModel(browser).until(element(tryLuck).isDisplayed());
    }

    @Test
    public void testElementIsPresent() {
        Assume.assumeTrue(!(browser instanceof HtmlUnitDriver));
        init(browser);
        input.sendKeys("florence and the machine");
        input.submit();
        waitModel(browser).until(element(topStuff).isPresent());
    }

    @Test
    public void testElementTextContains() throws InterruptedException {
        Assume.assumeTrue(!(browser instanceof HtmlUnitDriver));
        init(browser);
        input.sendKeys("florence");
        waitModel(browser).until(element(search).isPresent());
        input.sendKeys("and the machine");
        waitModel(browser).until(element(search).textContains("florence and the machine"));
    }

    @Test
    public void testAttributeIsPresent() {
        init(browser);
        browser.get("http://google.com");
        waitModel(browser).until(attribute(input, "value").isPresent());
    }

    @Test
    public void testAttributeValueContains() {
        init(browser);
        input.sendKeys("florence and the machine");
        waitModel(browser).until(attribute(input, "value").valueContains("florence"));
    }

    @Test
    public void testAttributeValueEquals() {
        init(browser);
        input.sendKeys("florence and the machine");
        waitModel(browser).until(attribute(input, "value").valueEquals("florence and the machine"));
    }

    @Test
    public void testNotElementIsDisplayed() {
        Assume.assumeTrue(!(browser instanceof HtmlUnitDriver));
        init(browser);
        input.sendKeys("florence and the machine");
        input.submit();
        waitModel(browser).until(element(tryLuck).not().isDisplayed());
    }

    @Test
    public void testNotElementIsPresent() {
        init(browser);
        browser.get("http://google.com");
        waitModel(browser).until(element(search).not().isPresent());
    }

    @Test
    public void testNotElementTextContains() {
        Assume.assumeTrue(!(browser instanceof HtmlUnitDriver));
        init(browser);
        input.sendKeys("florence and the machine");
        input.clear();
        input.sendKeys("rolling stones");
        waitModel(browser).until(element(search).not().textContains("florence and the machine"));
    }

    @Test
    public void testNotElementTextEquals() {
        Assume.assumeTrue(!(browser instanceof HtmlUnitDriver));
        init(browser);
        input.sendKeys("florence and the machine");
        input.clear();
        input.sendKeys("rolling stones");
        waitModel(browser).until(element(search).not().textEquals("lary fary"));
    }

    @Test
    public void testNotAttributeIsPresent() {
        init(browser);
        browser.get("http://google.com");
        waitModel(browser).until(attribute(input, "laryfary").not().isPresent());
    }

    @Test
    public void testNotAttributeValueContains() {
        init(browser);
        input.sendKeys("florence and the machine");
        waitModel(browser).until(attribute(input, "value").not().valueContains("rolling stones"));
    }

    @Test
    public void testNotAttributeValueEquals() {
        init(browser);
        input.sendKeys("florence and the machine");
        waitModel(browser).until(attribute(input, "value").not().valueEquals("florence"));
    }

    protected void init(WebDriver driver) {
        // TODO replace with automatic instantiation using ARQGRA-40
        GrapheneConfigurationContext.set(new GrapheneConfiguration());
        driver.get("http://google.com");
        PageFactory.initElements(new DefaultElementLocatorFactory(driver), this);
    }

}
