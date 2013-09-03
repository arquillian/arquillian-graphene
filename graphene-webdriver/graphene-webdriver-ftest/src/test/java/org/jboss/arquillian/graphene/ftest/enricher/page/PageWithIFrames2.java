package org.jboss.arquillian.graphene.ftest.enricher.page;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

public class PageWithIFrames2 {

    @FindBy(tagName = "select")
    private Select select;

    @FindBy(tagName = "span")
    private WebElement span;

    @Drone
    private WebDriver browser;

    public Select getSelect() {
        return select;
    }

    public WebElement getSpan() {
        return span;
    }
}
