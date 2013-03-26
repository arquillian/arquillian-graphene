package org.jboss.arquillian.graphene.ftest.wait;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class LocatorAttributeTest extends AbstractWaitTest {

    @Test
    public void testAttributeIsPresent() {
        loadPage();
        checkAttributeIsPresent(Graphene.waitAjax().until().element(BY_HEADER).attribute("style"));
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
