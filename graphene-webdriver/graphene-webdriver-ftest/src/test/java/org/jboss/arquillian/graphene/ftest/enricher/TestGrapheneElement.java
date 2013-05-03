package org.jboss.arquillian.graphene.ftest.enricher;

import java.net.URL;
import java.util.List;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.enricher.findby.ByJQuery;
import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;

@RunWith(Arquillian.class)
public class TestGrapheneElement {

    @Drone
    private WebDriver browser;

    @FindBy
    private GrapheneElement root;
    @FindBy(id="root")
    private WebElement pureRoot;

    @FindBy
    private GrapheneElement doesntExist;
    @FindBy
    private List<GrapheneElement> doesntExistList;

    @FindBy(tagName="option")
    private List<GrapheneElement> options;
    @FindBy(tagName="option")
    private List<WebElement> pureOptions;

    @JavaScript
    private TestJavascript testJavascript;

    @ArquillianResource
    private Actions actions;

    @Before
    public void loadPage() {
        URL page = this.getClass().getClassLoader()
            .getResource("org/jboss/arquillian/graphene/ftest/enricher/sample.html");
        browser.get(page.toString());
    }

    @Test
    public void testOneIsPresent() {
        loadPage();
        Assert.assertTrue(root.isPresent());
        Assert.assertFalse(doesntExist.isPresent());
    }

    @Test
    public void testOneGetText() {
        loadPage();
        Assert.assertEquals(pureRoot.getText().trim(), root.getText().trim());
    }

    @Test
    public void testOneWithJavascript() {
        loadPage();
        String inner = testJavascript.getInnerHtml(root);
        Assert.assertTrue(inner.contains("<div id=\"pseudoroot\">pseudo root</div>"));
    }

    @Test
    public void testOneWithActions() {
        actions.moveToElement(root).doubleClick().build().perform();
    }

    @Test
    public void testOneFindByTagName() {
        GrapheneElement element = root.findElement(By.tagName("div"));
        Assert.assertEquals("pseudo root", element.getText().trim());
    }

    @Test
    public void testOneFindByCss() {
        GrapheneElement element = root.findElement(By.cssSelector("div"));
        Assert.assertEquals("pseudo root", element.getText().trim());
    }

    @Test
    public void testOneFindByJQuery() {
        GrapheneElement element = root.findElement(ByJQuery.jquerySelector("div"));
        Assert.assertEquals("pseudo root", element.getText().trim());
    }

    @Test
    public void testListIsPresent() {
        loadPage();
        Assert.assertEquals(3, options.size());
        for (GrapheneElement element: options) {
            Assert.assertTrue(element.isPresent());
        }
        Assert.assertEquals(0, doesntExistList.size());
    }

    @Test
    public void testListGetText() {
        loadPage();
        for (int i=0; i<3; i++) {
            Assert.assertEquals(pureOptions.get(i).getText().trim(), options.get(i).getText());
        }
    }

    @Test
    public void testListWithJavascript() {
        loadPage();
        for (GrapheneElement element: options) {
            String inner = testJavascript.getInnerHtml(element);
            Assert.assertTrue(inner.contains("option"));
        }
    }

    @JavaScript("document.test")
    @Dependency(sources = {"org/jboss/arquillian/graphene/ftest/enricher/test.js"})
    public interface TestJavascript {

        String getInnerHtml(WebElement element);

    }
}
