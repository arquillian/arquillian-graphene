package org.jboss.arquillian.graphene.ftest.condition;

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.elements.GrapheneSelectImpl;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.junit.ArquillianTestClass;
import org.jboss.arquillian.junit.ArquillianTest;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@RunAsClient
public class ConditionsJUnitRulesTestCase {

   @ClassRule
   public static ArquillianTestClass arquillianTestClass = new ArquillianTestClass();

   @Rule
   public ArquillianTest arquillianTest = new ArquillianTest();

   @Drone
   private WebDriver browser;

   private static final By BY_HEADER = By.id("header");
   private static final By BY_OPTION1 = By.id("option1");
   private static final By BY_HIDE_BUTTON = By.id("hide");
   private static final By BY_APPEAR_BUTTON = By.id("appear");


   @FindBy(id="appear")
   private WebElement appearButton;
   @FindBy(id="header")
   private WebElement header;
   @FindBy(id="hide")
   private WebElement hideButton;
   @FindBy(id="idInput")
   private WebElement idInput;
   @FindBy(id="option1")
   private WebElement option1;
   @FindBy(id="select")
   private WebElement select;
   @FindBy(id="textInput")
   private WebElement textInput;
   @FindBy(id="submit")
   private WebElement updateButton;

   @ArquillianResource
   private URL contextRoot;

   @Deployment
   public static WebArchive createTestArchive() {
      return Resources.inCurrentPackage().all().buildWar("test.war");
   }

   @Before
   public void loadPage() {
      Resource.inCurrentPackage().find("sample.html").loadPage(browser, contextRoot);
   }

   @Test
   public void testAttributeIsPresent() {
      hideButton.click();
      Graphene.waitModel().until().element(header).attribute("style").is().present();
      appearButton.click();
      Graphene.waitModel().until().element(header).attribute("style").is().not().present();
   }

   @Test
   public void testAttributeValueContains() {
      textInput.clear();
      Graphene.waitModel().until().element(textInput).value().not().contains("Tested");
      textInput.sendKeys("Tested Header");
      Graphene.waitModel().until().element(textInput).value().contains("Tested");
   }

   @Test
   public void testAttributeValueEquals() {
      textInput.clear();
      textInput.sendKeys("Tested");
      Graphene.waitModel().until().element(textInput).value().not().equalTo("Tested Header");
      textInput.sendKeys(" Header");
      Graphene.waitModel().until().element(textInput).value().equalTo("Tested Header");
   }

   @Test
   public void testAttributeValueEqualsIgnoreCase() {
      textInput.clear();
      textInput.sendKeys("Tested");
      Graphene.waitModel().until().element(textInput).value().not().equalToIgnoreCase("tEsTeD HEADER");
      textInput.sendKeys(" Header");
      Graphene.waitModel().until().element(textInput).value().equalToIgnoreCase("tEsTeD HEADER");
   }

   @Test
   public void testAttributeValueMatches() {
      textInput.clear();
      textInput.sendKeys("Tested");
      Graphene.waitModel().until().element(textInput).value().not().matches("[tT]este. [hH]ea.er");
      textInput.sendKeys(" Header");
      Graphene.waitModel().until().element(textInput).value().matches("[tT]este. [hH]ea.er");
   }

   @Test
   public void testElementIsPresent() {
      idInput.clear();
      idInput.sendKeys("header2");
      updateButton.click();
      Graphene.waitModel(browser).until().element(header).is().not().present();
      idInput.clear();
      idInput.sendKeys("header");
      updateButton.click();
      Graphene.waitModel(browser).until().element(header).is().present();
   }

   @Test
   public void testElementIsPresentWithBy() {
      idInput.clear();
      idInput.sendKeys("header2");
      updateButton.click();
      Graphene.waitModel(browser).until().element(BY_HEADER).is().not().present();
      idInput.clear();
      idInput.sendKeys("header");
      updateButton.click();
      Graphene.waitModel(browser).until().element(BY_HEADER).is().present();
   }

   @Test
   public void testElementIsSelected() {
      new GrapheneSelectImpl(select).selectByIndex(0);
      Graphene.waitModel().until().element(option1).is().selected();
      new GrapheneSelectImpl(select).selectByIndex(1);
      Graphene.waitModel().until().element(option1).is().not().selected();
   }

   @Test
   public void testElementIsSelectedWithBy() {
      new GrapheneSelectImpl(select).selectByIndex(0);
      Graphene.waitModel().until().element(BY_OPTION1).is().selected();
      new GrapheneSelectImpl(select).selectByIndex(1);
      Graphene.waitModel().until().element(BY_OPTION1).is().not().selected();
   }

   @Test
   public void testElementIsVisible() {
      hideButton.click();
      Graphene.waitModel().until().element(header).is().not().visible();
      appearButton.click();
      Graphene.waitModel().until().element(header).is().visible();
   }

   @Test
   public void testElementIsVisibleWithBy() {
      hideButton.click();
      Graphene.waitModel().until().element(BY_HEADER).is().not().visible();
      appearButton.click();
      Graphene.waitModel().until().element(BY_HEADER).is().visible();
   }

   @Test
   public void testElementIsClickable() {
      Graphene.waitModel().until().element(hideButton).is().clickable();
      hideButton.click();
      Graphene.waitModel().until().element(hideButton).is().not().clickable();
      Graphene.waitModel().until().element(appearButton).is().clickable();
      appearButton.click();
      Graphene.waitModel().until().element(appearButton).is().not().clickable();
   }

   @Test
   public void testElementIsClickableWithBy() {
      Graphene.waitModel().until().element(BY_HIDE_BUTTON).is().clickable();
      hideButton.click();
      Graphene.waitModel().until().element(BY_HIDE_BUTTON).is().not().clickable();
      Graphene.waitModel().until().element(BY_APPEAR_BUTTON).is().clickable();
      appearButton.click();
      Graphene.waitModel().until().element(BY_APPEAR_BUTTON).is().not().clickable();
   }

   @Test
   public void testElementTextContains() {
      textInput.clear();
      textInput.sendKeys("florence and the machine");
      updateButton.click();
      Graphene.waitModel().until().element(header).text().contains("machine");
      textInput.clear();
      textInput.sendKeys("Tested Header");
      updateButton.click();
      Graphene.waitModel().until().element(header).text().not().contains("machine");
   }

   @Test
   public void testElementTextContainsWithBy() {
      textInput.clear();
      textInput.sendKeys("florence and the machine");
      updateButton.click();
      Graphene.waitModel().until().element(BY_HEADER).text().contains("machine");
      textInput.clear();
      textInput.sendKeys("Tested Header");
      updateButton.click();
      Graphene.waitModel().until().element(BY_HEADER).text().not().contains("machine");
   }

   @Test
   public void testElementTextEquals() {
      textInput.clear();
      textInput.sendKeys("florence and the machine");
      updateButton.click();
      Graphene.waitModel().until().element(header).text().equalTo("florence and the machine");
      textInput.sendKeys("Tested Header");
      updateButton.click();
      Graphene.waitModel().until().element(header).text().not().equalTo("florence and the machine");
   }

   @Test
   public void testElementTextEqualsWithBy() {
      textInput.clear();
      textInput.sendKeys("florence and the machine");
      updateButton.click();
      Graphene.waitModel().until().element(BY_HEADER).text().equalTo("florence and the machine");
      textInput.sendKeys("Tested Header");
      updateButton.click();
      Graphene.waitModel().until().element(BY_HEADER).text().not().equalTo("florence and the machine");
   }

   @Test
   public void testElementTextEqualsIgnoreCase() {
      textInput.clear();
      textInput.sendKeys("florence and the machine");
      updateButton.click();
      Graphene.waitModel().until().element(header).text().equalToIgnoreCase("FLORENCE aNd THE machIne");
      textInput.sendKeys("Tested Header");
      updateButton.click();
      Graphene.waitModel().until().element(header).text().not().equalToIgnoreCase("FLORENCE aNd THE machIne");
   }

   @Test
   public void testElementTextEqualsIgnoreCaseWithBy() {
      textInput.clear();
      textInput.sendKeys("florence and the machine");
      updateButton.click();
      Graphene.waitModel().until().element(BY_HEADER).text().equalToIgnoreCase("FLORENCE aNd THE machIne");
      textInput.sendKeys("Tested Header");
      updateButton.click();
      Graphene.waitModel().until().element(BY_HEADER).text().not().equalToIgnoreCase("FLORENCE aNd THE machIne");
   }

   @Test
   public void testElementTextMatches() {
      textInput.clear();
      textInput.sendKeys("florence and the machine");
      updateButton.click();
      Graphene.waitModel().until().element(header).text().matches("[fF]lorenc. and (the)? [mM]a.hine");
      textInput.sendKeys("Tested Header");
      updateButton.click();
      Graphene.waitModel().until().element(header).text().not().matches("[fF]lorenc. and (the)? [mM]a.hine");
   }

   @Test
   public void testElementTextMatchesWithBy() {
      textInput.clear();
      textInput.sendKeys("florence and the machine");
      updateButton.click();
      Graphene.waitModel().until().element(BY_HEADER).text().matches("[fF]lorenc. and (the)? [mM]a.hine");
      textInput.sendKeys("Tested Header");
      updateButton.click();
      Graphene.waitModel().until().element(BY_HEADER).text().not().matches("[fF]lorenc. and (the)? [mM]a.hine");
   }
}
