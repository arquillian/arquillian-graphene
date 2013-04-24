package org.jboss.arquillian.graphene.ftest.wait;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;

@RunWith(Arquillian.class)
public class JQueryWebElementTest extends AbstractWaitTest {

    @FindBy(jquery="#appear")
    protected WebElement jqueryAppearButton;
    @FindBy(jquery="#header")
    protected WebElement jqueryHeader;
    @FindBy(jquery="#hide")
    protected WebElement jqueryHideButton;
    @FindBy(jquery="#idInput")
    protected WebElement jqueryIdInput;
    @FindBy(jquery="#option1")
    protected WebElement jqueryOption1;
    @FindBy(jquery="#select")
    protected WebElement jquerySelect;
    @FindBy(jquery="#textInput")
    protected WebElement jqueryTextInput;
    @FindBy(jquery="#submit")
    protected WebElement jqueryUpdateButton;
    @FindBy(jquery="#outside")
    protected WebElement outsideLink;

    @Test
    public void textElementIsPresent() {
        loadPage();
        checkElementIsPresent(Graphene.waitModel().until().element(jqueryHeader));
    }

    @Test
    public void testElementIsSelected() {
        loadPage();
        checkElementIsSelected(Graphene.waitModel().until().element(jqueryOption1));
    }

    @Test
    public void testElementIsVisible() {
        loadPage();
        checkElementIsVisible(Graphene.waitModel().until().element(jqueryHeader));
    }

    @Test
    public void testElementIsVisibleDirectly() {
        loadPage();
        jqueryHideButton.click();
        Graphene.waitModel().until().element(jqueryHeader).is().not().visible();
        jqueryAppearButton.click();
        Graphene.waitModel().until().element(jqueryHeader).is().visible();
    }

    @Test
    public void testElementTextContains() {
        loadPage();
        checkElementTextContains(Graphene.waitModel().until().element(jqueryHeader));
    }

    @Test
    public void testElementTextEquals() {
        loadPage();
        checkElementTextEquals(Graphene.waitModel().until().element(jqueryHeader));
    }

    @Test
    public void testElementIsEnabled() {
        loadPage();
        checkElementIsEnabled(Graphene.waitModel().until().element(jquerySelect));
    }

    @Test
    public void testJQueryWhenNewPageIsLoaded() {
        loadPage();
        outsideLink.click();
        Graphene.waitAjax()
                .until()
                .element(jqueryHeader)
                .text().equalTo("Outside");
    }

}
