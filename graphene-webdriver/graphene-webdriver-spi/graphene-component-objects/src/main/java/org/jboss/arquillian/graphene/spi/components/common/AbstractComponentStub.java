package org.jboss.arquillian.graphene.spi.components.common;

import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AbstractComponentStub extends AbstractComponent {

    @Root
    private WebElement root;

    @FindBy(className = "classNameRef")
    private WebElement locatorRefByClassName;

    @FindBy(name = "nameRef")
    private WebElement locatorRefByName;

    @FindBy(id = "idRef")
    private WebElement locatorRefById;

    @FindBy(tagName = "tagNameRef")
    private WebElement locatorRefByTagName;

    @FindBy(linkText = "linkTextRef")
    private WebElement locatorRefByLinkText;

    @FindBy(partialLinkText = "partiaLinkTextRef")
    private WebElement locatorRefByPartialLinkText;

    @FindBy(xpath = "//div[@class='refByXpath']")
    private WebElement locatorRefByXPath;

    @FindBy(css = "cssSelectorRef")
    private WebElement locatorRefByCssSelector;

    public String invokeMethodOnRoot() {
        return root.getText();
    }

    public String invokeMethodOnElementRefByClass() {
        return this.locatorRefByClassName.getText();
    }

    public String invokeMethodOnElementRefById() {
        return this.locatorRefById.getText();
    }

    public String invokeMethodOnElementRefByCSS() {
        return this.locatorRefByCssSelector.getText();
    }

    public String invokeMethodOnElementRefByName() {
        return this.locatorRefByName.getText();
    }

    public String invokeMethodOnElementRefByTagName() {
        return locatorRefByTagName.getAttribute("id");
    }

    public String invokeMethodOnElementRefByXpath() {
        return this.locatorRefByXPath.getText();
    }

    public String invokeMethodOnElementRefByLinkText() {
        return this.locatorRefByLinkText.getText();
    }

    public String invokeMethodOnElementRefByPartialLinkText() {
        return this.locatorRefByPartialLinkText.getText();
    }

    public WebElement getRootProxy() {
        return this.root;
    }

    public WebElement getLocatorRefByClassName() {
        return locatorRefByClassName;
    }

    public void setLocatorRefByClassName(WebElement locatorRefByClassName) {
        this.locatorRefByClassName = locatorRefByClassName;
    }

    public WebElement getLocatorRefByName() {
        return locatorRefByName;
    }

    public void setLocatorRefByName(WebElement locatorRefByName) {
        this.locatorRefByName = locatorRefByName;
    }

    public WebElement getLocatorRefById() {
        return locatorRefById;
    }

    public void setLocatorRefById(WebElement locatorRefById) {
        this.locatorRefById = locatorRefById;
    }

    public WebElement getLocatorRefByTagName() {
        return locatorRefByTagName;
    }

    public void setLocatorRefByTagName(WebElement locatorRefByTagName) {
        this.locatorRefByTagName = locatorRefByTagName;
    }

    public WebElement getLocatorRefByLinkText() {
        return locatorRefByLinkText;
    }

    public void setLocatorRefByLinkText(WebElement locatorRefByLinkText) {
        this.locatorRefByLinkText = locatorRefByLinkText;
    }

    public WebElement getLocatorRefByPartialLinkText() {
        return locatorRefByPartialLinkText;
    }

    public void setLocatorRefByPartialLinkText(WebElement locatorRefByPartialLinkText) {
        this.locatorRefByPartialLinkText = locatorRefByPartialLinkText;
    }

    public WebElement getLocatorRefByXPath() {
        return locatorRefByXPath;
    }

    public void setLocatorRefByXPath(WebElement locatorRefByXPath) {
        this.locatorRefByXPath = locatorRefByXPath;
    }

    public WebElement getLocatorRefByCssSelector() {
        return locatorRefByCssSelector;
    }

    public void setLocatorRefByCssSelector(WebElement locatorRefByCssSelector) {
        this.locatorRefByCssSelector = locatorRefByCssSelector;
    }

}
