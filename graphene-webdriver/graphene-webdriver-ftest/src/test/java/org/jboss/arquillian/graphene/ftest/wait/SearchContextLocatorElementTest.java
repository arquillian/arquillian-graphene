package org.jboss.arquillian.graphene.ftest.wait;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@RunWith(Arquillian.class)
public class SearchContextLocatorElementTest extends AbstractWaitTest {

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
    public void textElementIsPresent() {
        loadPage();
        checkElementIsPresent(Graphene.waitModel().until().element(correct, BY_CLASS_HEADER));
    }

    @Test
    public void testElementIsSelected() {
        loadPage();
        checkElementIsSelected(Graphene.waitModel().until().element(correct, BY_CLASS_OPTION1));
    }

    @Test
    public void testElementIsVisible() {
        loadPage();
        checkElementIsVisible(Graphene.waitModel().until().element(correct, BY_CLASS_HEADER));
    }

    @Test
    public void testElementIsVisibleDirectly() {
        loadPage();
        hideButton.click();
        Graphene.waitModel().until().element(correct, BY_CLASS_HEADER).is().not().visible();
        appearButton.click();
        Graphene.waitModel().until().element(correct, BY_CLASS_HEADER).is().visible();
    }

    @Test
    public void testElementTextContains() {
        loadPage();
        checkElementTextContains(Graphene.waitModel().until().element(correct, BY_CLASS_HEADER));
    }

    @Test
    public void testElementTextEquals() {
        loadPage();
        checkElementTextEquals(Graphene.waitModel().until().element(correct, BY_CLASS_HEADER));
    }

    @Test
    public void testElementIsEnabled() {
        loadPage();
        checkElementIsEnabled(Graphene.waitModel().until().element(correct, BY_CLASS_SELECT));
    }

}
