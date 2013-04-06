package org.jboss.arquillian.graphene.enricher.pageObjects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.hamcrest.Matcher;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.spi.annotations.Location;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Rule;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

@RunWith(Arquillian.class)
public class TestPageObjectsLocation {

    @Page
    private MyPageObject1 pageObject1;

    @Page
    private MyPageObject2 pageObject2;

    @Page
    private MyPageObjectEmptyLocationClass pageObjectEmptyLocation;

    @Page
    private MyPageObjectWrongURL pageObjectWrongURL;

    @Drone
    @SuppressWarnings("unused")
    private WebDriver browser;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testPageObjectsAreInitialized() {
        assertNotNull(pageObject1);
        assertNotNull(pageObject2);
    }

    @Test
    public void testCorrectPageIsOpened1(@Location MyPageObject1 obj) {
        String actual = pageObject1.element.getText();
        assertEquals("pseudo root", actual);
    }

    @Test
    public void testCorrectPageIsOpened2(@Location MyPageObject2 obj) {
        String actual = pageObject2.element.getText();
        assertEquals("WebElement content", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLocationIsEmpty(@Location MyPageObjectEmptyLocationClass obj) {
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLocationOfNonExistingURL(@Location MyPageObjectWrongURL obj) {
    }

    /*
     * Nested classes
     */

    @Location("org/jboss/arquillian/graphene/ftest/enricher/sample.html")
    public static class MyPageObject1 {
        @FindBy(css = "#pseudoroot")
        WebElement element;
    }

    @Location("org/jboss/arquillian/graphene/ftest/enricher/empty-findby.html")
    public static class MyPageObject2 {
        @FindBy(css = "#divWebElement")
        WebElement element;
    }

    @Location
    public static class MyPageObjectEmptyLocationClass {

    }

    @Location("/non/existing/url")
    public static class MyPageObjectWrongURL {

    }
}
