/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.ftest.wait;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.junit.Test;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
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
        checkElementIsPresent(Graphene.waitModel().until().element(jqueryHeader));
    }

    @Test
    public void testElementIsSelected() {
        checkElementIsSelected(Graphene.waitModel().until().element(jqueryOption1));
    }

    @Test
    public void testElementIsVisible() {
        checkElementIsVisible(Graphene.waitModel().until().element(jqueryHeader));
    }

    @Test
    public void testElementIsVisibleDirectly() {
        jqueryHideButton.click();
        Graphene.waitModel().until().element(jqueryHeader).is().not().visible();
        jqueryAppearButton.click();
        Graphene.waitModel().until().element(jqueryHeader).is().visible();
    }

    @Test
    public void testElementTextContains() {
        checkElementTextContains(Graphene.waitModel().until().element(jqueryHeader));
    }

    @Test
    public void testElementTextEquals() {
        checkElementTextEquals(Graphene.waitModel().until().element(jqueryHeader));
    }

    @Test
    public void testElementIsEnabled() {
        checkElementIsEnabled(Graphene.waitModel().until().element(jquerySelect));
    }

    @Test
    public void testJQueryWhenNewPageIsLoaded() {
        outsideLink.click();
        Graphene.waitAjax()
                .until()
                .element(jqueryHeader)
                .text().equalTo("Outside");
    }

}
