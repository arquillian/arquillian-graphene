package org.jboss.arquillian.graphene.ftest.wait;

import java.net.URL;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.wait.AttributeBuilder;
import org.jboss.arquillian.graphene.wait.ElementBuilder;
import org.jboss.arquillian.graphene.wait.IsNotElementBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class AbstractWaitTest {

    @Drone
    protected WebDriver browser;

    protected static final By BY_HEADER = By.id("header");
    protected static final By BY_OPTION1 = By.id("option1");
    protected static final By BY_TEXT_INPUT = By.id("textInput");

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

    protected void loadPage() {
        URL page = this.getClass().getClassLoader().getResource("org/jboss/arquillian/graphene/ftest/wait/sample.html");
        browser.get(page.toString());
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
        new Select(select).selectByIndex(0);
        IsNotElementBuilder aa;
        option1.is().selected();
        new Select(select).selectByIndex(1);
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
}
