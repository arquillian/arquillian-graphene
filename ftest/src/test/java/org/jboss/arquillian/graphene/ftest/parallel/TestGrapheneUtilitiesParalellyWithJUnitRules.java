package org.jboss.arquillian.graphene.ftest.parallel;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.page.Page;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import qualifier.Browser1;
import qualifier.Browser2;

public class TestGrapheneUtilitiesParalellyWithJUnitRules extends AbstractParallelJUnitRulesTest {
   @Browser1
   @FindBy(tagName="h1")
   private WebElement header1;

   @Browser2
   @FindBy(tagName="h1")
   private WebElement header2;

   @FindBy(tagName="h1")
   private WebElement headerDefault;

   @Page
   @Browser1
   private TestGrapheneUtilitiesParalelly.SimplePage page1;

   @Page
   @Browser2
   private TestGrapheneUtilitiesParalelly.SimplePage page2;

   @Page
   private TestGrapheneUtilitiesParalelly.SimplePage pageDefault;


   @Test
   public void testWaitWithElements() {
      Graphene.waitGui()
         .until()
         .element(header1)
         .text()
         .equalTo("Page 1");

      Graphene.waitGui()
         .until()
         .element(header2)
         .text()
         .equalTo("Page 2");

      Graphene.waitGui()
         .until()
         .element(headerDefault)
         .text()
         .equalTo("Page Default");
   }

   @Test
   public void testWaitWithBys() {
      Graphene.waitGui(browser1)
         .until()
         .element(By.tagName("h1"))
         .text()
         .equalTo("Page 1");

      Graphene.waitGui(browser2)
         .until()
         .element(By.tagName("h1"))
         .text()
         .equalTo("Page 2");

      Graphene.waitGui(browserDefault)
         .until()
         .element(By.tagName("h1"))
         .text()
         .equalTo("Page Default");
   }

   @Test
   public void testGuardHttp() {
      checkGuardHttp(page1, page2, pageDefault);
   }

   @Test
   public void testGuardXhr() {
      checkGuardXhr(page1, page2, pageDefault);
   }

   public void checkGuardHttp(TestGrapheneUtilitiesParalelly.SimplePage checkPage1, TestGrapheneUtilitiesParalelly.SimplePage checkPage2, TestGrapheneUtilitiesParalelly.SimplePage checkPageDefault) {
      Graphene.guardHttp(checkPage1).http();
      Graphene.guardHttp(checkPage2).http();
      Graphene.guardHttp(checkPageDefault).http();
   }

   public void checkGuardXhr(TestGrapheneUtilitiesParalelly.SimplePage checkPage1, TestGrapheneUtilitiesParalelly.SimplePage checkPage2, TestGrapheneUtilitiesParalelly.SimplePage checkPageDefault) {
      Graphene.guardAjax(checkPage1).xhr();
      Graphene.guardAjax(checkPage2).xhr();
      Graphene.guardAjax(checkPageDefault).xhr();
   }

   public static class SimplePage {

      @FindBy
      private WebElement http;

      @FindBy
      private WebElement xhr;

      public void http() {
         http.click();
      }

      public void xhr() {
         xhr.click();
      }
   }
}
