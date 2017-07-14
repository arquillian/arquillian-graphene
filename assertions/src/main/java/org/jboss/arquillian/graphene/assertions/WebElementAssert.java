package org.jboss.arquillian.graphene.assertions;

import org.apache.commons.lang3.BooleanUtils;
import org.assertj.core.api.AbstractAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.JavascriptExecutor;

import static org.assertj.core.api.Assertions.assertThat;

public class WebElementAssert extends AbstractAssert<WebElementAssert, WebElement> {

    public WebElementAssert(WebElement actual) {
        super(actual, WebElementAssert.class);
    }

    public WebElementAssert hasText(String expectedText) {
        assertThat(this.actual.getText()).isEqualTo(expectedText);
        return this;
    }

//returning webDriverException: Can't find variable $, and the same message even if replaced with jQuery
    /*public WebElementAssert hasChild(WebDriver browser){
        String jQuerySelector = "arguments[0]";
        assertThat(((JavascriptExecutor) browser).executeScript("return jQuery(arguments[0]).children();", this.actual));
        return this;
    }*/

    public WebElementAssert hasParent(){
        assertThat(this.actual.findElement(By.xpath("..")));
        return this;
    }

    public WebElementAssert isVisible(){
        assertThat(this.actual.isDisplayed());
        return this;
    }

    public WebElementAssert isChosen(){
        assertThat(this.actual.isSelected());
        return this;
    }

    public WebElementAssert containsValue(String expectedText){
        String text = this.actual.getAttribute("value");
        assertThat(text).isEqualTo(expectedText);
        return this;
    }

    public WebElementAssert isEmpty(){
        String content = this.actual.getAttribute("value");
        assertThat(content).isEqualTo("");
        return this;
    }

    public WebElementAssert isNotVisible(){
        assertThat(BooleanUtils.isFalse(this.actual.isDisplayed()));
        return this;
    }

    public WebElementAssert addErrorString(String error) {
        assertThat(this.actual).withFailMessage(error);
        return this;
    }

    public WebElementAssert typeIs(String expectedType){
        assertThat(this.actual.getAttribute("type")).isEqualTo(expectedType);
        return this;
    }

    public WebElementAssert hasCssClass(String expectedClass){
        assertThat(this.actual.getAttribute("class")).isEqualTo(expectedClass);
        return this;
    }

    public WebElementAssert isFocused(WebDriver browser){
        assertThat(this.actual).isEqualTo(browser.switchTo().activeElement());
        return this;
    }

    public WebElementAssert isEnabled(){
        assertThat(this.actual.isEnabled());
        return this;
    }

    public WebElementAssert isntEnabled(){
        assertThat(BooleanUtils.isFalse(this.actual.isEnabled()));
        return this;
    }

    public WebElementAssert textMatchesRegex(String regex){
        assertThat(this.actual.getText().matches(regex));
        return this;
    }
}
