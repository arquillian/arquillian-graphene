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
import org.openqa.selenium.support.FindBy;
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

    @FindBy(id = "pseudoroot")
    private WebElement div;

    @FindBy(id = "root")
    private WebElement divHead;

    @FindBy(id="option two")
    private WebElement selection;

    @FindBy(id="form")
    private WebElement input_form;

    @FindBy(id="unseen")
    private WebElement invisible;

    @FindBy(id="invisible")
    private WebElement top;

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

    @Test
    public void should_confirm_element_has_parent_web_element(){
        assertThat(div).hasParent();}

    @Test
    public void should_confirm_that_web_element_is_displayed_on_page(){
        assertThat(div).isVisible();}

    @Test
    public void should_confirm_that_element_is_selected(){
        Select dropdown = new Select(browser.findElement(By.id("select")));
        dropdown.selectByVisibleText("option one");
        assertThat(selection).isChosen();
    }

    @Test
    public void should_assert_text_in_input_form(){
        input_form.sendKeys("should assert correct");
        assertThat(input_form).containsValue("should assert correct");
    }

    @Test
    public void should_assert_input_is_empty(){
        assertThat(input_form).isEmpty();
    }

    @Test
    public void combining_assertions(){
        assertThat(div).hasText("pseudo root").isVisible();
    }

    @Test
    public void should_assert_item_is_not_visible(){
        assertThat(invisible).isNotVisible();
    }



}