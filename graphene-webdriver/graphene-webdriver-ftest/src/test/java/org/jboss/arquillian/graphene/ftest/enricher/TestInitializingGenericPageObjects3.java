/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.ftest.enricher;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.ftest.enricher.page.TestPage;
import org.jboss.arquillian.graphene.ftest.enricher.page.fragment.AbstractPageFragmentStub;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestInitializingGenericPageObjects3 extends AbstractTest<TestPage, TestInitializingGenericPageObjects3.InnerTestPage> {

    @Page
    private InnerTestPage innerTestPageNotGeneric;

    @Test
    public void testInitializingPageObjectsDeclaredAsInnerClassesGeneric() {
        String actual = anotherPageWithGenericType.getElement().getText();
        assertEquals("The Page object declared as inner class was not initialized correctly", "This is embedded element",
            actual);
    }

    @Test
    public void testInitializingPageObjectsDeclaredAsInnerClassesNonGeneric() {
        String actual = innerTestPageNotGeneric.getElement().getText();
        assertEquals("The Page object declared as inner class was not initialized correctly", "This is embedded element",
            actual);
    }

    protected class InnerTestPage {

        @FindBy(xpath = "//div[@id='rootElement']")
        private AbstractPageFragmentStub abstractPageFragment;

        @FindBy(id = "embeddedElement")
        private WebElement element;

        @FindBy(xpath = "//input")
        private WebElement input;

        public WebElement getInput() {
            return input;
        }

        public void setInput(WebElement input) {
            this.input = input;
        }

        public WebElement getElement() {
            return element;
        }

        public void setElement(WebElement element) {
            this.element = element;
        }

        public AbstractPageFragmentStub getAbstractPageFragment() {
            return abstractPageFragment;
        }

        public void setAbstractPageFragment(AbstractPageFragmentStub abstractPageFragment) {
            this.abstractPageFragment = abstractPageFragment;
        }

    }
}
