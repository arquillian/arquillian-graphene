package org.jboss.arquillian.graphene.ftest.enricher.page.fragment;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class PageFragmentWithSpan {

    @FindBy(tagName = "span")
    private List<WebElement> spans;

    @FindBy(tagName = "span")
    private WebElement span;

    public List<WebElement> getSpans() {
        return spans;
    }

    public void setSpans(List<WebElement> spans) {
        this.spans = spans;
    }

    public WebElement getSpan() {
        return span;
    }

    public void setSpan(WebElement span) {
        this.span = span;
    }
}
