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
package org.jboss.arquillian.graphene.enricher;

import java.net.URL;
import junit.framework.Assert;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class TestSamplePageFragment {

    @Drone
    private WebDriver browser;

    @FindBy(id="root")
    private SamplePageFragment pageFragment;

    @FindBy(id="root")
    private SamplePageFragmentWithRootAsTheLastField pageFragmentWithRootAsTheLastField;

    @FindBy(id="span")
    private WebElement spanNotCorrect;

    public void loadPage() {
        URL page = this.getClass().getClassLoader().getResource("org/jboss/arquillian/graphene/ftest/enricher/sample.html");
        browser.get(page.toString());
    }

    @Test
    public void testRelativePath() {
        loadPage();
        Assert.assertEquals("Fields in page fragment are not initialized relatively to root element.", "correct", pageFragment.getText().toLowerCase().trim());
    }

    @Test
    public void testNotStandardOrder() {
        loadPage();
        Assert.assertEquals("pseudo root", pageFragmentWithRootAsTheLastField.getPseudoroot().getText().toLowerCase().trim());
        Assert.assertTrue(pageFragmentWithRootAsTheLastField.getRoot().getText().toLowerCase().trim().contains("pseudo root"));
        Assert.assertFalse(pageFragmentWithRootAsTheLastField.getRoot().getText().toLowerCase().trim().equals("pseudo root"));
    }

    public void testCommonWebElement() {
        loadPage();
        Assert.assertEquals("not correct", spanNotCorrect.getText().toLowerCase().trim());
    }

    public static class SamplePageFragment {
        @SuppressWarnings("unused")
        @Root
        private WebElement root;
        @FindBy(tagName = "span")
        private WebElement span;

        public String getText() {
            return span.getText();
        }
    }

    public static class SamplePageFragmentWithRootAsTheLastField {

        @FindBy(id = "pseudoroot")
        private WebElement pseudoroot;

        @Root
        private WebElement root;

        public WebElement getRoot() {
            return root;
        }

        public WebElement getPseudoroot() {
            return pseudoroot;
        }
    }
}
