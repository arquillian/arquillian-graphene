package org.jboss.arquillian.graphene.enricher.pageObjects.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class EmbeddedPage {

    public static final String EXPECTED_TEXT_OF_EMBEDDED_ELEM = "This is embedded element";
    
    @FindBy(id="embeddedElement")
    private WebElement embeddedElement;

    public String invokeMethodOnEmbeddedElement() {
        return embeddedElement.getText();
    }
}
