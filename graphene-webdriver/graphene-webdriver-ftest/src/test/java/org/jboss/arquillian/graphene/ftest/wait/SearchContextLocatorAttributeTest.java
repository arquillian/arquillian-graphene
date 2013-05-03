package org.jboss.arquillian.graphene.ftest.wait;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@RunWith(Arquillian.class)
public class SearchContextLocatorAttributeTest extends AbstractWaitTest {

    protected static final By BY_CLASS_HEADER = By.className("header");
    protected static final By BY_CLASS_OPTION1 = By.className("option1");
    protected static final By BY_CLASS_SELECT = By.className("select");
    protected static final By BY_CLASS_TEXT_INPUT = By.className("textInput");
    protected static final By BY_CLASS_INPUT_WITH_EMPTY_STYLE = By.className("inputWithEmptyStyle");
    protected static final By BY_CLASS_INPUT_WITH_EMPTY_STYLE_WHITE_SPACES = By.className("inputWithEmptyStyleWhiteSpaces");
    protected static final By BY_CLASS_INPUT_WITH_NO_STYLE_DEFINED = By.className("inputWithNoStyleDefined");
    protected static final By BY_CLASS_INPUT_WITH_EMPTY_READONLY = By.className("inputWithEmptyReadonly");

    @FindBy(id="correct")
    private WebElement correct;

    @Test
    public void testAttributeIsPresent() {
        loadPage();
        checkAttributeIsPresent(Graphene.waitAjax().until().element(correct, BY_CLASS_HEADER).attribute("style"));
    }

    @Test
    public void testAttributeIsPresentDirectly() {
        loadPage();
        hideButton.click();
        Graphene.waitModel().until().element(correct, BY_CLASS_HEADER).attribute("style").is().present();
        appearButton.click();
        Graphene.waitModel().until().element(correct, BY_CLASS_HEADER).attribute("style").is().not().present();
    }

    @Test
    public void testAttributeValueContains() {
        loadPage();
        checkAttributeValueContains(Graphene.waitModel().until().element(correct, BY_CLASS_TEXT_INPUT).attribute("value"));
    }

    @Test
    public void testAttributeValueEquals() {
        loadPage();
        checkAttributeValueEquals(Graphene.waitModel().until().element(correct, BY_CLASS_TEXT_INPUT).attribute("value"));
    }

    @Test
    public void testEmptyAttribute() {
        loadPage();

        Graphene.waitModel().until().element(correct, BY_CLASS_INPUT_WITH_EMPTY_STYLE).attribute("style").is().not().present();
        Graphene.waitModel().until().element(correct, BY_CLASS_INPUT_WITH_NO_STYLE_DEFINED).attribute("style").is().not().present();
        Graphene.waitModel().until().element(correct, BY_CLASS_INPUT_WITH_EMPTY_STYLE_WHITE_SPACES).attribute("style").is().not().present();
        Graphene.waitModel().until().element(correct, BY_CLASS_INPUT_WITH_EMPTY_READONLY).attribute("readonly").is().present();
    }

    // shortcuts

    @Test
    public void testValueContains() {
        loadPage();
        checkAttributeValueContains(Graphene.waitModel().until().element(correct, BY_CLASS_TEXT_INPUT).value());
    }

    @Test
    public void testValueEquals() {
        loadPage();
        checkAttributeValueEquals(Graphene.waitModel().until().element(correct, BY_CLASS_TEXT_INPUT).value());
    }
}
