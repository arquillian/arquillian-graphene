package org.jboss.arquillian.graphene.ftest.wait;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class WebElementAttributeTest extends AbstractWaitTest {

    @Test
    public void testAttributeIsPresent() {
        loadPage();
        checkAttributeIsPresent(Graphene.waitModel().until().element(header).attribute("style"));
    }

    @Test
    public void testAttributeIsPresentDirectly() {
        loadPage();
        hideButton.click();
        Graphene.waitModel().until().element(header).attribute("style").is().present();
        appearButton.click();
        Graphene.waitModel().until().element(header).attribute("style").is().not().present();
    }

    @Test
    public void testAttributeValueContains() {
        loadPage();
        checkAttributeValueContains(Graphene.waitModel().until().element(textInput).attribute("value"));
    }

    @Test
    public void testAttributeValueEquals() {
        loadPage();
        checkAttributeValueEquals(Graphene.waitModel().until().element(textInput).attribute("value"));
    }

    @Test
    public void testEmptyAttribute() {
        loadPage();

        Graphene.waitModel().until().element(inputWithEmptyStyle).attribute("style").is().not().present();
        Graphene.waitModel().until().element(inputWithNoStyleDefined).attribute("style").is().not().present();
        Graphene.waitModel().until().element(inputWithEmptyStyleWhiteSpaces).attribute("style").is().not().present();
        Graphene.waitModel().until().element(inputWithEmptyReadonly).attribute("readonly").is().present();
    }

    // shortcuts

    @Test
    public void testValueContains() {
        loadPage();
        checkAttributeValueContains(Graphene.waitModel().until().element(textInput).value());
    }

    @Test
    public void testValueEquals() {
        loadPage();
        checkAttributeValueEquals(Graphene.waitModel().until().element(textInput).value());
    }
}
