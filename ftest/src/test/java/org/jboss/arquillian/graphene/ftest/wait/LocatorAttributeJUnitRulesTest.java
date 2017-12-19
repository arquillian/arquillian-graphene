package org.jboss.arquillian.graphene.ftest.wait;

import org.jboss.arquillian.graphene.Graphene;
import org.junit.Test;

public class LocatorAttributeJUnitRulesTest extends AbstractWaitTest {

   @Test
   public void testAttributeIsPresent() {
      loadPage();
      checkAttributeIsPresent(Graphene.waitAjax().until().element(BY_HEADER).attribute("style"));
   }

   @Test
   public void testAttributeIsPresentDirectly() {
      hideButton.click();
      Graphene.waitModel().until().element(BY_HEADER).attribute("style").is().present();
      appearButton.click();
      Graphene.waitModel().until().element(BY_HEADER).attribute("style").is().not().present();
   }

   @Test
   public void testAttributeValueContains() {
      checkAttributeValueContains(Graphene.waitModel().until().element(BY_TEXT_INPUT).attribute("value"));
   }

   @Test
   public void testAttributeValueEquals() {
      checkAttributeValueEquals(Graphene.waitModel().until().element(BY_TEXT_INPUT).attribute("value"));
   }

   @Test
   public void testEmptyAttribute() {
      Graphene.waitModel().until().element(BY_INPUT_WITH_EMPTY_STYLE).attribute("style").is().not().present();
      Graphene.waitModel().until().element(BY_INPUT_WITH_NO_STYLE_DEFINED).attribute("style").is().not().present();
      Graphene.waitModel().until().element(BY_INPUT_WITH_EMPTY_STYLE_WHITE_SPACES).attribute("style").is().not().present();
      Graphene.waitModel().until().element(BY_INPUT_WITH_EMPTY_READONLY).attribute("readonly").is().present();
   }

   // shortcuts

   @Test
   public void testValueContains() {
      checkAttributeValueContains(Graphene.waitModel().until().element(BY_TEXT_INPUT).value());
   }

   @Test
   public void testValueEquals() {
      checkAttributeValueEquals(Graphene.waitModel().until().element(BY_TEXT_INPUT).value());
   }
}
