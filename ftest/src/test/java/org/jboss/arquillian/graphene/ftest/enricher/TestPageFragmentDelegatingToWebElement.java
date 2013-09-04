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
package org.jboss.arquillian.graphene.ftest.enricher;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.graphene.ftest.enricher.page.fragment.PageFragmentImplementingWebElement;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Location;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@RunWith(Arquillian.class)
@RunAsClient
public class TestPageFragmentDelegatingToWebElement {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private URL contextRoot;

    @Page
    private TestPage page;

    private static final String pageLocation = "org/jboss/arquillian/graphene/ftest/enricher/delegating-pagefragment.html";

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().find("delegating-pagefragment.html").buildWar("deployment.war");
    }

    @Test
    public void testPageFragmentMethodIsDelegatingCorrectly() {
        browser.get(contextRoot + pageLocation);
        testPage(page);
    }

    @Test
    public void testPageFragmentFromInitialPageIsDelegatingCorrectly(@InitialPage TestPage testedPage) {
        testPage(testedPage);
    }

    private void testPage(TestPage testedPage) {
        String expectedText = "test";
        testedPage.getInputFragment().sendKeys(expectedText);
        assertEquals(expectedText, testedPage.getInputFragment().getInputText());
        assertEquals("foo-bar", testedPage.getInputFragment().getStyleClass());
    }

    @Location("resource://" + pageLocation)
    public class TestPage {
        @FindBy(tagName = "input")
        private PageFragmentImplementingWebElement inputFragment;

        public PageFragmentImplementingWebElement getInputFragment() {
            return inputFragment;
        }
    }
}
