package org.jboss.arquillian.graphene.ftest.wait;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@RunWith(Arquillian.class)
public class LocatorAttributeTest extends AbstractWaitTest {

    @FindBy
    WebElement inputWithEmptyStyle;

    @FindBy
    WebElement inputWithEmptyStyleWhiteSpaces;

    @FindBy
    WebElement inputWitNoStyleDefined;

    @Test
    public void testAttributeIsPresent() {
        loadPage();
        checkAttributeIsPresent(Graphene.waitModel().until().element(BY_HEADER).attribute("style"));
    }

    @Test
    public void testAttributeIsPresentDirectly() {
        loadPage();
        hideButton.click();
        Graphene.waitModel().until().element(BY_HEADER).attribute("style").is().present();
        appearButton.click();
        Graphene.waitModel().until().element(BY_HEADER).attribute("style").is().not().present();
    }

    @Test
    public void testAttributeValueContains() {
        loadPage();
        checkAttributeValueContains(Graphene.waitModel().until().element(BY_TEXT_INPUT).attribute("value"));
    }

    @Test
    public void testAttributeValueEquals() {
        loadPage();
        checkAttributeValueEquals(Graphene.waitModel().until().element(BY_TEXT_INPUT).attribute("value"));
    }

    @Test
    public void testEmptyAttribute() {
        loadPage();
        Graphene.waitModel().until().element(inputWithEmptyStyle).attribute("style").is().not().present();

        Graphene.waitModel().until().element(inputWitNoStyleDefined).attribute("style").is().not().present();

        Graphene.waitModel().until().element(inputWithEmptyStyleWhiteSpaces).attribute("style").is().not().present();
    }

    // shortcuts

    @Test
    public void testValueContains() {
        loadPage();
        checkAttributeValueContains(Graphene.waitModel().until().element(BY_TEXT_INPUT).value());
    }

    @Test
    public void testValueEquals() {
        loadPage();
        checkAttributeValueEquals(Graphene.waitModel().until().element(BY_TEXT_INPUT).value());
    }
}
