/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.arquillian.graphene.ftest.element;

import java.net.URL;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.openqa.selenium.support.ui.Select;

import static org.jboss.arquillian.graphene.assertions.GrapheneAssert.assertThat;

@RunWith(Arquillian.class)
@RunAsClient
public class TestGrapheneAssertions {

    // this will be used to look up html files in src/test/resources folder, translating package name to directory structure
    private static final String SAMPLE_PACKAGE = "org.jboss.arquillian.graphene.ftest.assertions";

    @ArquillianResource
    private URL contextRoot;

    @Drone
    private WebDriver browser;
    /*EventFiringWebDriver eventDriver = new EventFiringWebDriver(browser);
    EventHandler handler = new EventHandler();
    eventDriver.register(handler);*/

    @FindBy(id = "pseudoroot")
    private WebElement div;

    @FindBy(id = "root")
    private WebElement divHead;

    @FindBy(id="option two")
    private WebElement secondOption;

    @FindBy(id="form")
    private WebElement inputForm;

    @FindBy(id="unseen")
    private WebElement invisible;

    @FindBy(id="invisible")
    private WebElement aboveInvisible;

    @FindBy(id="select")
    private WebElement selected;

    @FindBy(id="inactive")
    private WebElement inactiveInputForm;

    @FindBy(id="disabled button")
    private WebElement disabledButton;

    @FindBy(id = "java1")
    private WebElement javaRadioButton;

    @FindBy(id = "php1")
    private WebElement phpRadioButton;

    @FindBy(id = "seleniumbox")
    private WebElement seleniumCheckBox;

    @FindBy(id = "restapibox")
    private WebElement restApiCheckBox;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inPackage(SAMPLE_PACKAGE).all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inPackage(SAMPLE_PACKAGE).find("sample.html").loadPage(browser, contextRoot);
    }

    @Test
    public void should_be_able_to_assert_text_on_web_element() {
        assertThat(div).hasText("pseudo root");
    }

    @Test
    public void should_confirm_element_has_child_web_element(){
        assertThat(divHead).hasChild();}

    /*@Test
    public void should_confirm_element_has_parent_web_element(){
        assertThat(div).hasParent();}*/

    @Test
    public void should_confirm_that_web_element_is_displayed_on_page(){
        assertThat(div).isDisplayed();}

    @Test
    public void should_confirm_that_dropdown_element_is_Chosen(){
        Select dropdown = new Select(browser.findElement(By.id("select")));
        dropdown.selectByVisibleText("option two");
        assertThat(secondOption).isChosenD(dropdown);
    }

    @Test
    public void should_assert_text_in_input_form(){
        inputForm.sendKeys("should assert correct");
        assertThat(inputForm).containsText("should assert correct");
    }

    @Test
    public void should_assert_input_is_empty(){
        inputForm.sendKeys("not empty");
        assertThat(inputForm).isNot().isEmpty();
    }

    @Test
    public void should_assert_type_attribute_of_WebElement(){
        assertThat(inputForm).isTypeOf("text");
    }

    @Test
    public void should_assert_css_class_of_element(){
        assertThat(aboveInvisible).hasCssClass("opague");
    }

    @Test
    public void confirm_the_element_is_in_focus(){
        new Actions(browser).moveToElement(inputForm).click().perform();
        assertThat(inputForm).isFocused(browser);
    }

    @Test
    public void confirm_the_element_is_enabled(){
        System.out.println(disabledButton.isEnabled());
        assertThat(disabledButton.isEnabled());
    }

    @Test
    public void confirm_element_text_matches_regex(){
        System.out.println(invisible.getText().matches("([A-Z])\\w+"));
        assertThat(invisible).isNot().textMatchesRegex("([A-Z])\\w+");
    }


    /*@Test
    public void asserts_that_the_correct_radio_button_is_chosen(){
        new Actions(browser).moveToElement(phpRadioButton).click();
        assertThat(phpRadioButton).isDisplayed();
    }*/


    @Test
    public void should_assert_the_correct_checkbox_is_chosen(){
        new Actions(browser).moveToElement(restApiCheckBox).click();
        assertThat(restApiCheckBox).isSelected();

    }

    /*@Test
    public void should_assert_the_element_has_no_children(){
        assertThat(div).isNot().hasChild();
    }*/

    /*@Test
    public void should_check_error_messages_are_working_as_expected(){
        assertThat(inputForm).isNot().isEnabled();
    }*/

    /*@Test
    public void should_confirm_if_exists_method_works(){
        find("pseudoroot", browser);
    }*/

    /*@Test
    public void should_confirm_web_interactions_work_as_expected(){
        new Actions(browser).moveToElement(inputForm).click().perform().verify();//possible incarnation
    }*/

    /*@Test
    public void should_confirm_case_sensitive_matching_works_as_expected(){
        assertThat(div).caseSensitiveMatch("pseudo test");
    }*/

    /*@Test
    public void should_confirm_that_case_insensitive_matching_works_as_expected(){
        assertThat(inactiveInputForm).subStringMatching("zz");
    }*/
}