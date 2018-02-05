package org.jboss.arquillian.graphene.ftest.wait;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.findby.ByJQuery;
import org.junit.Test;

public class JQueryLocatorElementJUnitRulesTest extends AbstractWaitJUnitRulesTest {

   protected static final ByJQuery BY_JQUERY_HEADER = ByJQuery.selector("#header");
   protected static final ByJQuery BY_JQUERY_OPTION1 = ByJQuery.selector("#option1");
   protected static final ByJQuery BY_JQUERY_TEXT_INPUT = ByJQuery.selector("#textInput");

   @Test
   public void textElementIsPresent() {
      loadPage();
      checkElementIsPresent(Graphene.waitModel().until().element(BY_JQUERY_HEADER));
   }

   @Test
   public void testElementIsSelected() {
      loadPage();
      checkElementIsSelected(Graphene.waitModel().until().element(BY_JQUERY_OPTION1));
   }

   @Test
   public void testElementIsVisible() {
      loadPage();
      checkElementIsVisible(Graphene.waitModel().until().element(BY_JQUERY_HEADER));
   }

   @Test
   public void testElementIsVisibleDirectly() {
      loadPage();
      hideButton.click();
      Graphene.waitModel().until().element(BY_JQUERY_HEADER).is().not().visible();
      appearButton.click();
      Graphene.waitModel().until().element(BY_JQUERY_HEADER).is().visible();
   }

   @Test
   public void testElementTextContains() {
      loadPage();
      checkElementTextContains(Graphene.waitModel().until().element(BY_JQUERY_HEADER));
   }

   @Test
   public void testElementTextEquals() {
      loadPage();
      checkElementTextEquals(Graphene.waitModel().until().element(BY_JQUERY_HEADER));
   }

}
