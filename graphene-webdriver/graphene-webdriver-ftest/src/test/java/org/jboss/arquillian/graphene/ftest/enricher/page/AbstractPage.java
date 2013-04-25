package org.jboss.arquillian.graphene.ftest.enricher.page;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AbstractPage {

    @FindBy(xpath = "//input")
    private WebElement input;

    public WebElement getInput() {
        return input;
    }

    public void setInput(WebElement input) {
        this.input = input;
    }
}
