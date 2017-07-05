package org.jboss.arquillian.graphene.assertions;

import org.assertj.core.api.AbstractAssert;
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
}
