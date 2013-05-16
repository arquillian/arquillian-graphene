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
package org.jboss.arquillian.graphene.ftest.condition;

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.condition.AttributeConditionFactory;
import org.jboss.arquillian.graphene.condition.ElementConditionFactory;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
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
        checkAttributeIsPresent(Graphene.attribute(header, "style"));
    }

    @Test
    public void testAttributeValueContains() {
        checkAttributeValueContains(Graphene.attribute(textInput, "value"));
    }

    @Test
    public void testAttributeValueEquals() {
        checkAttributeValueEquals(Graphene.attribute(textInput, "value"));
    }

    @Test
    public void testElementIsPresent() {
        checkElementIsPresent(Graphene.element(header));
    }

    @Test
    public void testElementIsPresentWithBy() {
        checkElementIsPresent(Graphene.element(BY_HEADER));
    }

    @Test
    public void testElementIsSelected() {
        checkElementIsSelected(Graphene.element(option1));
    }

    @Test
    public void testElementIsSelectedWithBy() {
        checkElementIsSelected(Graphene.element(BY_OPTION1));
    }

    @Test
    public void testElementIsVisible() {
        checkElementIsVisible(Graphene.element(header));
    }

    @Test
    public void testElementIsVisibleWithBy() {
        checkElementIsVisible(Graphene.element(BY_HEADER));
    }

    @Test
    public void testElementTextContains() {
        checkElementTextContains(Graphene.element(header));
    }

    @Test
    public void testElementTextContainsWithBy() {
        checkElementTextContains(Graphene.element(BY_HEADER));
    }

    @Test
    public void testElementTextEquals() {
        checkElementTextEquals(Graphene.element(header));
    }

    @Test
    public void testElementTextEqualsWithBy() {
        checkElementTextEquals(Graphene.element(BY_HEADER));
    }

    protected void checkAttributeIsPresent(AttributeConditionFactory headerStyleAttributeFactory) {
        hideButton.click();
        Graphene.waitModel().until(headerStyleAttributeFactory.isPresent());
        appearButton.click();
        Graphene.waitModel().until(headerStyleAttributeFactory.not().isPresent());
    }

    protected void checkAttributeValueContains(AttributeConditionFactory textInputValueAttributeFactory) {
        // deprecated
        textInput.clear();
        Graphene.waitModel().until(textInputValueAttributeFactory.not().valueContains("Tested"));
        textInput.sendKeys("Tested Header");
        Graphene.waitModel().until(textInputValueAttributeFactory.valueContains("Tested"));
        // current
        textInput.clear();
        Graphene.waitModel().until(textInputValueAttributeFactory.not().contains("Tested"));
        Assert.assertTrue(textInputValueAttributeFactory.not().contains("Tested").apply(browser));
        textInput.sendKeys("Tested Header");
        Graphene.waitModel().until(textInputValueAttributeFactory.contains("Tested"));
    }

    protected void checkAttributeValueEquals(AttributeConditionFactory textInputValueAttributeFactory) {
        // deprecated
        textInput.clear();
        textInput.sendKeys("Tested");
        Graphene.waitModel().until(textInputValueAttributeFactory.not().valueEquals("Tested Header"));
        textInput.sendKeys(" Header");
        Graphene.waitModel().until(textInputValueAttributeFactory.valueEquals("Tested Header"));
        // current
        textInput.clear();
        textInput.sendKeys("Tested");
        Graphene.waitModel().until(textInputValueAttributeFactory.not().equalTo("Tested Header"));
        Assert.assertTrue(textInputValueAttributeFactory.not().equalTo("Tested Header").apply(browser));
        textInput.sendKeys(" Header");
        Graphene.waitModel().until(textInputValueAttributeFactory.equalTo("Tested Header"));
    }

    protected void checkElementIsPresent(ElementConditionFactory headerElementFactory) {
        idInput.clear();
        idInput.sendKeys("header2");
        updateButton.click();
        Graphene.waitModel(browser).until(headerElementFactory.not().isPresent());
        idInput.clear();
        idInput.sendKeys("header");
        updateButton.click();
        Graphene.waitModel(browser).until(headerElementFactory.isPresent());
    }

    protected void checkElementIsSelected(ElementConditionFactory option1ElementFactory) {
        new Select(select).selectByIndex(0);
        Graphene.waitModel().until(option1ElementFactory.isSelected());
        new Select(select).selectByIndex(1);
        Graphene.waitModel().until(option1ElementFactory.not().isSelected());
    }

    protected void checkElementTextContains(ElementConditionFactory headerElementFactory) {
        // deprecated
        textInput.clear();
        textInput.sendKeys("florence and the machine");
        updateButton.click();
        Graphene.waitModel().until(headerElementFactory.textContains("machine"));
        textInput.clear();
        textInput.sendKeys("Tested Header");
        updateButton.click();
        Graphene.waitModel().until(headerElementFactory.not().textContains("machine"));
        // current
        textInput.clear();
        textInput.sendKeys("florence and the machine");
        updateButton.click();
        Graphene.waitModel().until(headerElementFactory.text().contains("machine"));
        Assert.assertTrue(headerElementFactory.text().contains("machine").apply(browser));
        textInput.clear();
        textInput.sendKeys("Tested Header");
        updateButton.click();
        Graphene.waitModel().until(headerElementFactory.text().not().contains("machine"));
    }

    protected void checkElementTextEquals(ElementConditionFactory headerElementFactory) {
        // deprecated
        textInput.clear();
        textInput.sendKeys("florence and the machine");
        updateButton.click();
        Graphene.waitModel().until(headerElementFactory.textEquals("florence and the machine"));
        textInput.sendKeys("Tested Header");
        updateButton.click();
        Graphene.waitModel().until(headerElementFactory.not().textEquals("florence and the machine"));
        // current
        textInput.clear();
        textInput.sendKeys("florence and the machine");
        updateButton.click();
        Graphene.waitModel().until(headerElementFactory.text().equalTo("florence and the machine"));
        Assert.assertTrue(headerElementFactory.text().equalTo("florence and the machine").apply(browser));
        textInput.sendKeys("Tested Header");
        updateButton.click();
        Graphene.waitModel().until(headerElementFactory.text().not().equalTo("florence and the machine"));
    }

    protected void checkElementIsVisible(ElementConditionFactory headerElementFactory) {
        hideButton.click();
        Graphene.waitModel().until(headerElementFactory.not().isVisible());
        appearButton.click();
        Graphene.waitModel().until(headerElementFactory.isVisible());
    }

}
