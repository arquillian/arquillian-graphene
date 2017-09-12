package org.jboss.arquillian.graphene.assertions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Exists extends WebElementAssert {

    public Exists(WebElement actual) {
        super(actual);
    }

    public WebElement find(String locator, WebDriver browser){
        WebElement possible = browser.findElement(By.id(locator));
        return possible;
    }
}
