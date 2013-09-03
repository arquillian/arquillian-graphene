package org.jboss.arquillian.graphene.ftest.enricher.page;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.ftest.enricher.page.fragment.PageFragmentWithSpan;
import org.jboss.arquillian.graphene.page.InFrame;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class PageWithIFrames {

    @InFrame(nameOrId = "first")
    @FindBy(tagName = "select")
    private Select select;

    @InFrame(nameOrId = "second")
    @FindBy(id = "root")
    private PageFragmentWithSpan myFragment;

    @InFrame(index = 0)
    @FindBy(tagName = "span")
    private WebElement span;

    @FindBy(className = "divElement")
    private WebElement elementInDefaultFrame;
    
    @Drone
    private WebDriver browser;

    public Select getSelect() {
        return select;
    }

    public PageFragmentWithSpan getMyFragment() {
        return myFragment;
    }

    public WebElement getSpan() {
        return span;
    }

    public WebElement getElementInDefaultFrame() {
        return elementInDefaultFrame;
    }
}
