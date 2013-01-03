 package org.jboss.arquillian.graphene.ftest.wait;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class LocatorElementTest extends AbstractWaitTest {

    @Test
    public void textElementIsPresent() {
        loadPage();
        checkElementIsPresent(Graphene.waitModel().until().element(BY_HEADER));
    }

    @Test
    public void testElementIsSelected() {
        loadPage();
        checkElementIsSelected(Graphene.waitModel().until().element(BY_OPTION1));
    }

    @Test
    public void testElementIsVisible() {
        loadPage();
        checkElementIsVisible(Graphene.waitModel().until().element(BY_HEADER));
    }

    @Test
    public void testElementIsVisibleDirectly() {
        loadPage();
        hideButton.click();
        Graphene.waitModel().until().element(BY_HEADER).is().not().visible();
        appearButton.click();
        Graphene.waitModel().until().element(BY_HEADER).is().visible();
    }

    @Test
    public void testElementTextContains() {
        loadPage();
        checkElementTextContains(Graphene.waitModel().until().element(BY_HEADER));
    }

    @Test
    public void testElementTextEquals() {
        loadPage();
        checkElementTextEquals(Graphene.waitModel().until().element(BY_HEADER));
    }

}
