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
package org.jboss.arquillian.graphene.ftest.wait;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.elements.GrapheneSelectImpl;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.graphene.wait.AttributeBuilder;
import org.jboss.arquillian.graphene.wait.ElementBuilder;
import org.jboss.arquillian.graphene.wait.IsNotElementBuilder;
import org.jboss.arquillian.junit.ArquillianTestClass;
import org.jboss.arquillian.junit.ArquillianTest;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunAsClient
public abstract class AbstractWaitJUnitRulesTest {

    @ClassRule
    public static ArquillianTestClass arquillianTestClass = new ArquillianTestClass();

    @Rule
    public ArquillianTest arquillianTest = new ArquillianTest();

    @Drone
    protected WebDriver browser;
    @ArquillianResource
    private URL contextRoot;

    protected static final By BY_HEADER = By.id("header");
    protected static final By BY_OPTION1 = By.id("option1");
    protected static final By BY_SELECT = By.id("select");
    protected static final By BY_TEXT_INPUT = By.id("textInput");
    protected static final By BY_INPUT_WITH_EMPTY_STYLE = By.id("inputWithEmptyStyle");
    protected static final By BY_INPUT_WITH_EMPTY_STYLE_WHITE_SPACES = By.id("inputWithEmptyStyleWhiteSpaces");
    protected static final By BY_INPUT_WITH_NO_STYLE_DEFINED = By.id("inputWithNoStyleDefined");
    protected static final By BY_INPUT_WITH_EMPTY_READONLY = By.id("inputWithEmptyReadonly");

    @FindBy(id="appear")
    protected WebElement appearButton;
    @FindBy(id="header")
    protected WebElement header;
    @FindBy(id="hide")
    protected WebElement hideButton;
    @FindBy(id="idInput")
    protected WebElement idInput;
    @FindBy(id="option1")
    protected WebElement option1;
    @FindBy(id="select")
    protected WebElement select;
    @FindBy(id="textInput")
    protected WebElement textInput;
    @FindBy(id="submit")
    protected WebElement updateButton;
    @FindBy
    protected WebElement enableSelect;
    @FindBy
    protected WebElement disableSelect;
    @FindBy
    protected WebElement inputWithEmptyStyle;
    @FindBy
    protected WebElement inputWithEmptyStyleWhiteSpaces;
    @FindBy
    protected WebElement inputWithNoStyleDefined;
    @FindBy
    protected WebElement inputWithEmptyReadonly;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
         Resource.inCurrentPackage().find("sample.html").loadPage(browser, contextRoot);
    }

    protected void checkAttributeIsPresent(AttributeBuilder<?> headerStyle) {
        hideButton.click();
        headerStyle.is().present();
        appearButton.click();
        headerStyle.is().not().present();
    }

    protected void checkAttributeValueContains(AttributeBuilder<?> textInputValue) {
        textInput.clear();
        textInputValue.not().contains("Tested");
        textInput.sendKeys("Tested Header");
        textInputValue.contains("Tested");
    }

    protected void checkAttributeValueEquals(AttributeBuilder<?> textInputValue) {
        textInput.clear();
        textInput.sendKeys("Tested");
        textInputValue.not().equalTo("Tested Header");
        textInput.sendKeys(" Header");
        textInputValue.equalTo("Tested Header");
    }

    protected void checkElementIsPresent(ElementBuilder<?> header) {
        idInput.clear();
        idInput.sendKeys("header2");
        updateButton.click();
        header.is().not().present();
        idInput.clear();
        idInput.sendKeys("header");
        updateButton.click();
        header.is().present();
    }

    protected void checkElementIsSelected(ElementBuilder<?> option1) {
        new GrapheneSelectImpl(select).selectByIndex(0);
        IsNotElementBuilder aa;
        option1.is().selected();
        new GrapheneSelectImpl(select).selectByIndex(1);
        option1.is().not().selected();
    }

    protected void checkElementTextContains(ElementBuilder<?> header) {
        textInput.clear();
        textInput.sendKeys("florence and the machine");
        updateButton.click();
        header.text().contains("machine");
        textInput.clear();
        textInput.sendKeys("Tested Header");
        updateButton.click();
        header.text().not().contains("machine");
    }

    protected void checkElementTextEquals(ElementBuilder<?> header) {
        textInput.clear();
        textInput.sendKeys("florence and the machine");
        updateButton.click();
        header.text().equalTo("florence and the machine");
        textInput.sendKeys("Tested Header");
        updateButton.click();
        header.text().not().equalTo("florence and the machine");
    }

    protected void checkElementIsVisible(ElementBuilder<?> header) {
        hideButton.click();
        header.is().not().visible();
        appearButton.click();
        header.is().visible();
    }

    protected void checkElementIsEnabled(ElementBuilder<?> select) {
        disableSelect.click();
        select.is().not().enabled();
        enableSelect.click();
        select.is().enabled();
    }
}
