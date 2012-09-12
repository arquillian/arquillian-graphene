package org.jboss.arquillian.graphene.enricher.page;

import org.jboss.arquillian.graphene.enricher.AbstractPageFragmentStub;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class TestPage2 {

    @FindBy(xpath = "//div[@id='rootElement']")
    private AbstractPageFragmentStub abstractPageFragment;

    @FindBy(xpath = "//div[@id='rootElement']")
    private WebElement element;

    @FindBy(xpath = "//input")
    private WebElement input;

    @Page
    private EmbeddedPage embeddedPage;

    public AbstractPageFragmentStub getAbstractPageFragment() {
        return abstractPageFragment;
    }

    public void setAbstractPageFragment(AbstractPageFragmentStub abstractPageFragment) {
        this.abstractPageFragment = abstractPageFragment;
    }

    public WebElement getElement() {
        return element;
    }

    public void setElement(WebElement element) {
        this.element = element;
    }

    public WebElement getInput() {
        return input;
    }

    public void setInput(WebElement input) {
        this.input = input;
    }

    public EmbeddedPage getEmbeddedPage() {
        return embeddedPage;
    }

    public void setEmbeddedPage(EmbeddedPage embeddedPage) {
        this.embeddedPage = embeddedPage;
    }
}
