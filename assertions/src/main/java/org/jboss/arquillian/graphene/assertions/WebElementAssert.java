package org.jboss.arquillian.graphene.assertions;

import org.assertj.core.api.AbstractAssert;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import static org.assertj.core.api.Assertions.assertThat;

public class WebElementAssert extends AbstractAssert<WebElementAssert, WebElement> {

    private boolean negated;

    public WebElementAssert(WebElement actual) {
        super(actual, WebElementAssert.class);
    }

    public WebElementAssert hasText(String expectedText) {
        if(this.negated){
            assertThat(this.actual.getText()).as("checking strings are not equal").overridingErrorMessage("The content of the element should not be equal to " + expectedText +", but it is.").isNotEqualTo(expectedText);
            this.negated = false;
            return this;
        }
        else {
            assertThat(this.actual.getText()).as("checking equality").overridingErrorMessage("The content of the element should be equal to" + expectedText + "but it is not.").isEqualTo(expectedText);
            return this;
        }
    }

    public WebElementAssert hasChild(){
            if (this.negated) {
                assertThat(this.actual.findElements(ByJQuery.selector("*"))).asList().size().isEqualTo(0);
                this.negated = false;
                return this;
            } else {
                assertThat(this.actual.findElement(ByJQuery.selector("*")));
                return this;
            }
    }

    public WebElementAssert hasParent(){
        if (this.negated){
            assertThat(this.actual.findElements(By.xpath(".."))).asList().size().isEqualTo(0);
            return this;
        }
        else {
            assertThat(this.actual.findElement(By.xpath("..")));
            return this;
        }
    }

    public WebElementAssert isDisplayed(){
        if(this.negated){
            assertThat(Boolean.valueOf(this.actual.isDisplayed())).as("checking element is not displayed").overridingErrorMessage("element is displayed").isFalse();
            this.negated = false;
            return this;
        }
        else {
            assertThat(Boolean.valueOf(this.actual.isDisplayed())).as("checking element is displayed").overridingErrorMessage("element is not displayed").isTrue();
            return this;
        }
    }

    //doesn't work for dropboxes
    public WebElementAssert isSelected(){
        if(this.negated){
            assertThat(Boolean.valueOf(this.actual.isSelected())).as("checking element is not selected").overridingErrorMessage("element is selected").isFalse();
            this.negated = false;
            return this;
        }
        else {
            assertThat(Boolean.valueOf(this.actual.isSelected())).as("checking element is selected").overridingErrorMessage("element is not selected").isTrue();
            return this;
        }
    }

    //support's dropboxes, supply dropbox and it will assert that the webelement is the chosen option
    public WebElementAssert isChosenD(Select dropdown){
        if(this.negated){
            assertThat(dropdown.getFirstSelectedOption()).as("checking supplied choice is not chosen").overridingErrorMessage("supplied choice is chosen").isNotEqualTo(this.actual);
            this.negated = false;
            return this;
        }
        else {
            assertThat(dropdown.getFirstSelectedOption()).as("checking supplied choice is chosen").overridingErrorMessage("supplied choice is not chosen").isEqualTo(this.actual);
            return this;
        }
    }


    public WebElementAssert containsText(String expectedText){
        String text = this.actual.getAttribute("value");
        if(this.negated){
            assertThat(text).as("checking text value is not the same as the supplied string").overridingErrorMessage("The element contains " + expectedText +", the supplied string.").isNotEqualTo(expectedText);
            this.negated = false;
            return this;
        }
        else {
            assertThat(text).as("checking text value is the same as the supplied string").overridingErrorMessage("The element does not contain " + expectedText + ", it contains some other string.").isEqualTo(expectedText);
            return this;
        }
    }

    public WebElementAssert isEmpty(){
        String content = this.actual.getAttribute("value");
        if(this.negated){
            assertThat(content).as("checking text input is not empty").overridingErrorMessage("is actually empty").isNotEqualTo("");
            this.negated = false;
            return this;
        }
        else {
            assertThat(content).as("checking text input is empty").overridingErrorMessage("is not empty").isEqualTo("");
            return this;
        }
    }

    public WebElementAssert isTypeOf(String expectedType){
        if(this.negated){
            assertThat(this.actual.getAttribute("type")).as("checking not supplied type").overridingErrorMessage("The element contains" + expectedType + ", the supplied type.").isNotEqualTo(expectedType);
            this.negated = false;
            return this;
        }
        else {
            assertThat(this.actual.getAttribute("type")).as("checking is supplied type").overridingErrorMessage("The element does not contain" + expectedType + ", it is some other type.").isEqualTo(expectedType);
            return this;
        }
    }

    public WebElementAssert hasCssClass(String expectedClass){
        if(this.negated){
            assertThat(this.actual.getAttribute("class")).as("checking css class does not match supplied css class").overridingErrorMessage("The element has " + expectedClass + ", the supplied css class.").isNotEqualTo(expectedClass);
            this.negated = false;
            return this;
        }
        else {
            assertThat(this.actual.getAttribute("class")).as("checking css class matches supplied class").overridingErrorMessage("The element has not the " + expectedClass + ", it contains another css class.").isEqualTo(expectedClass);
            return this;
        }
    }

    public WebElementAssert isFocused(WebDriver browser){
        if(this.negated){
            assertThat(this.actual).as("checking element is not in focus").overridingErrorMessage("element is in focus").isNotEqualTo(browser.switchTo().activeElement());
            this.negated = false;
            return this;
        }
        else {
            assertThat(this.actual).as("checking element is in focus").overridingErrorMessage("element is not in focus").isEqualTo(browser.switchTo().activeElement());
            return this;
        }
    }

    public WebElementAssert isEnabled(){
        if(this.negated){
            assertThat(Boolean.valueOf(this.actual.isEnabled())).as("checking element is not enabled").overridingErrorMessage("element is enabled").isFalse();
            this.negated = false;
            return this;
        }
        else {
            assertThat(Boolean.valueOf(this.actual.isEnabled())).as("checking element is enabled").overridingErrorMessage("element is not enabled").isTrue();
            return this;
        }
    }

    public WebElementAssert textMatchesRegex(String regex){
        if(this.negated){
            assertThat(Boolean.valueOf(this.actual.getText().matches(regex))).as("checking regex does not match text").overridingErrorMessage("The regex " + regex + " matches the text.").isFalse();
            this.negated = false;
            return this;
        }
        else {
            assertThat(Boolean.valueOf(this.actual.getText().matches(regex))).as("checking regex matches text").overridingErrorMessage("The regex " + regex + " does not match text.").isTrue();
            return this;
        }
    }

    public WebElementAssert caseInsensitiveMatching(String match) {
        if (this.negated) {
            assertThat(this.actual.getText()).as("checking string does not matches text, regardless of capitalisaton").overridingErrorMessage("The string, " + match + " is the same as the text.").isNotEqualTo(match.toLowerCase());
            this.negated = false;
            return this;
        }
        else {
            assertThat(this.actual.getText()).as("checking string matches text, regardless of capitalisaton").overridingErrorMessage("The string, " + match + " differs to the text.").isEqualTo(match.toLowerCase());
            return this;
        }
    }

    public WebElementAssert subStringMatching(String match){
        if(this.negated){
            assertThat(Boolean.valueOf(this.actual.getText().contains(match))).as("checking if substring is not in text").overridingErrorMessage("The substring " + match + " is in the text.").isFalse();
            this.negated = false;
            return this;
        }
        else {
            assertThat(Boolean.valueOf(this.actual.getText().contains(match))).as("checking if substring is in text").overridingErrorMessage("The substring " + match + " is not in the text.").isTrue();
            return this;
        }
    }

    public WebElementAssert isNot(){
        this.negated = true;
        return this;
    }

}