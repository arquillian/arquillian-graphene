package org.jboss.arquillian.graphene.ftest.wait;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class WebElementTest extends AbstractWaitTest {

    @Test
    public void textElementIsPresent() {
        loadPage();
        checkElementIsPresent(Graphene.waitModel().until().element(header));
    }

    @Test
    public void testElementIsSelected() {
        loadPage();
        checkElementIsSelected(Graphene.waitModel().until().element(option1));
    }

    @Test
    public void testElementIsVisible() {
        loadPage();
        checkElementIsVisible(Graphene.waitModel().until().element(header));
    }

    @Test
    public void testElementIsVisibleDirectly() {
        loadPage();
        hideButton.click();
        Graphene.waitModel().until().element(header).is().not().visible();
        appearButton.click();
        Graphene.waitModel().until().element(header).is().visible();
    }

    @Test
    public void testElementTextContains() {
        loadPage();
        checkElementTextContains(Graphene.waitModel().until().element(header));
    }

    @Test
    public void testElementTextEquals() {
        loadPage();
        checkElementTextEquals(Graphene.waitModel().until().element(header));
    }

    @Test
    public void testElementIsEnabled() {
        loadPage();
        checkElementIsEnabled(Graphene.waitModel().until().element(select));
    }

}
