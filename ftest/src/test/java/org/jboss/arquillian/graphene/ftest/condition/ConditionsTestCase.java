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
package org.jboss.arquillian.graphene.ftest.condition;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ConditionsTestCase {

    @Drone
    private WebDriver browser;

    private static final By BY_HEADER = By.id("header");
    private static final By BY_OPTION1 = By.id("option1");

    @FindBy(id="appear")
    private WebElement appearButton;
    @FindBy(id="header")
    private WebElement header;
    @FindBy(id="hide")
    private WebElement hideButton;
    @FindBy(id="idInput")
    private WebElement idInput;
    @FindBy(id="option1")
    private WebElement option1;
    @FindBy(id="select")
    private WebElement select;
    @FindBy(id="textInput")
    private WebElement textInput;
    @FindBy(id="submit")
    private WebElement updateButton;

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
    public void testAttributeIsPresent() {
        hideButton.click();
        Graphene.waitModel().until().element(header).attribute("style").is().present();
        appearButton.click();
        Graphene.waitModel().until().element(header).attribute("style").is().not().present();
    }

    @Test
    public void testAttributeValueContains() {
        textInput.clear();
        Graphene.waitModel().until().element(textInput).value().not().contains("Tested");
        textInput.sendKeys("Tested Header");
        Graphene.waitModel().until().element(textInput).value().contains("Tested");
    }

    @Test
    public void testAttributeValueEquals() {
        textInput.clear();
        textInput.sendKeys("Tested");
        Graphene.waitModel().until().element(textInput).value().not().equalTo("Tested Header");
        textInput.sendKeys(" Header");
        Graphene.waitModel().until().element(textInput).value().equalTo("Tested Header");
    }

    @Test
    public void testElementIsPresent() {
        idInput.clear();
        idInput.sendKeys("header2");
        updateButton.click();
        Graphene.waitModel(browser).until().element(header).is().not().present();
        idInput.clear();
        idInput.sendKeys("header");
        updateButton.click();
        Graphene.waitModel(browser).until().element(header).is().present();
    }

    @Test
    public void testElementIsPresentWithBy() {
        idInput.clear();
        idInput.sendKeys("header2");
        updateButton.click();
        Graphene.waitModel(browser).until().element(BY_HEADER).is().not().present();
        idInput.clear();
        idInput.sendKeys("header");
        updateButton.click();
        Graphene.waitModel(browser).until().element(BY_HEADER).is().present();
    }

    @Test
    public void testElementIsSelected() {
        new Select(select).selectByIndex(0);
        Graphene.waitModel().until().element(option1).is().selected();
        new Select(select).selectByIndex(1);
        Graphene.waitModel().until().element(option1).is().not().selected();
    }

    @Test
    public void testElementIsSelectedWithBy() {
        new Select(select).selectByIndex(0);
        Graphene.waitModel().until().element(BY_OPTION1).is().selected();
        new Select(select).selectByIndex(1);
        Graphene.waitModel().until().element(BY_OPTION1).is().not().selected();
    }

    @Test
    public void testElementIsVisible() {
        hideButton.click();
        Graphene.waitModel().until().element(header).is().not().visible();
        appearButton.click();
        Graphene.waitModel().until().element(header).is().visible();
    }

    @Test
    public void testElementIsVisibleWithBy() {
        hideButton.click();
        Graphene.waitModel().until().element(BY_HEADER).is().not().visible();
        appearButton.click();
        Graphene.waitModel().until().element(BY_HEADER).is().visible();
    }

    @Test
    public void testElementTextContains() {
        textInput.clear();
        textInput.sendKeys("florence and the machine");
        updateButton.click();
        Graphene.waitModel().until().element(header).text().contains("machine");
        textInput.clear();
        textInput.sendKeys("Tested Header");
        updateButton.click();
        Graphene.waitModel().until().element(header).text().not().contains("machine");
    }

    @Test
    public void testElementTextContainsWithBy() {
        textInput.clear();
        textInput.sendKeys("florence and the machine");
        updateButton.click();
        Graphene.waitModel().until().element(BY_HEADER).text().contains("machine");
        textInput.clear();
        textInput.sendKeys("Tested Header");
        updateButton.click();
        Graphene.waitModel().until().element(BY_HEADER).text().not().contains("machine");
    }

    @Test
    public void testElementTextEquals() {
        textInput.clear();
        textInput.sendKeys("florence and the machine");
        updateButton.click();
        Graphene.waitModel().until().element(header).text().equalTo("florence and the machine");
        textInput.sendKeys("Tested Header");
        updateButton.click();
        Graphene.waitModel().until().element(header).text().not().equalTo("florence and the machine");
    }

    @Test
    public void testElementTextEqualsWithBy() {
        textInput.clear();
        textInput.sendKeys("florence and the machine");
        updateButton.click();
        Graphene.waitModel().until().element(BY_HEADER).text().equalTo("florence and the machine");
        textInput.sendKeys("Tested Header");
        updateButton.click();
        Graphene.waitModel().until().element(BY_HEADER).text().not().equalTo("florence and the machine");
    }
}
