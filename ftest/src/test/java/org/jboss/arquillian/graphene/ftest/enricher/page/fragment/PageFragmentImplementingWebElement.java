package org.jboss.arquillian.graphene.ftest.enricher.page.fragment;

import org.jboss.arquillian.graphene.fragment.Root;
import org.openqa.selenium.WebElement;

public abstract class PageFragmentImplementingWebElement implements WebElement {

    @Root
    private WebElement input;
    
    public String getInputText() {
        return input.getAttribute("value");
    }
    
    public String getStyleClass() {
        return input.getAttribute("class");
    }
}
