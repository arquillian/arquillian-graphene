package org.jboss.arquillian.graphene.assertions;

import org.apache.commons.lang3.BooleanUtils;
import org.assertj.core.api.AbstractAssert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import static org.assertj.core.api.Assertions.assertThat;

public class WebElementAssert extends AbstractAssert<WebElementAssert, WebElement> {

    public WebElementAssert(WebElement actual) {
        super(actual, WebElementAssert.class);
    }

    public WebElementAssert hasText(String expectedText) {
        assertThat(this.actual.getText()).isEqualTo(expectedText);
        return this;
    }


    public WebElementAssert hasChild(){
        assertThat(this.actual.findElement(By.cssSelector("*")));
        return this;
    }

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
}
