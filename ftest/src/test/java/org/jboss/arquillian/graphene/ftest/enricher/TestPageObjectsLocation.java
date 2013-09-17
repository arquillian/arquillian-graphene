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
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Location;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import qualifier.Browser3;

@RunWith(Arquillian.class)
@RunAsClient
public class TestPageObjectsLocation {

    @Drone
    private WebDriver browser;

    @Drone
    @Browser3
    private WebDriver browser3;

    @ArquillianResource
    private URL contextRoot;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().find("empty-findby.html").buildWar("deployment.war");
    }

    @Deployment(name = "deployment1")
    public static WebArchive createTestArchive1() {
        return Resources.inCurrentPackage().find("sample.html").buildWar("deployment1.war");
    }

    @Deployment(name = "deployment2")
    public static WebArchive createTestArchive2() {
        return Resources.inCurrentPackage().all().buildWar("deployment2.war");
    }

    @Test
    @OperateOnDeployment("deployment1")
    public void testInitialPageOnNamedDeployment1(@InitialPage MyPageObject1 obj) {
        checkMyPageObject1(obj);
    }

    @Test
    @OperateOnDeployment("deployment2")
    public void testInitialPageOnNamedDeployment1(@InitialPage MyPageObject2 obj) {
        checkMyPageObject2(obj);
    }

    @Test
    public void testInitialPageOnDefaultDeploymnet(@InitialPage MyPageObject2 obj) {
        checkMyPageObject2(obj);
    }

    @Test
    @OperateOnDeployment("deployment2")
    public void testGoToNamedDeployment() {
        MyPageObject1 page1 = Graphene.goTo(MyPageObject1.class);
        checkMyPageObject1(page1);
        MyPageObject2 page2 = Graphene.goTo(MyPageObject2.class);
        checkMyPageObject2(page2);
    }

    @Test
    public void testGoToDefaultDeployment() {
        MyPageObject2 page2 = Graphene.goTo(MyPageObject2.class);
        checkMyPageObject2(page2);
    }

    @Test
    public void testInitialPageCustomBrowser(@Browser3 @InitialPage MyPageObject2 obj) {
        browser.get(contextRoot.toExternalForm());
        checkMyPageObject2(obj);
    }

    @Test
    public void testGotoPageCustomBrowser() {
        MyPageObject2 page2 = Graphene.goTo(MyPageObject2.class, Browser3.class);
        browser.get(contextRoot.toExternalForm());
        checkMyPageObject2(page2);

        browser3.get(contextRoot.toExternalForm());
        page2 = Graphene.goTo(MyPageObject2.class);
        checkMyPageObject2(page2);
    }



    @Test(expected = IllegalArgumentException.class)
    public void testGotoPageWithoutLocation() {
        Graphene.goTo(PageObjectWithoutLocation.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGotoPageWithoutLocation1(@InitialPage PageObjectWithoutLocation page) {
    }

    /*
     * Nested classes
     */
    @Location("org/jboss/arquillian/graphene/ftest/enricher/sample.html")
    public static class MyPageObject1 {
        @FindBy(css = "#pseudoroot")
        private WebElement element;

        public WebElement getElement() {
            return element;
        }
    }

    @Location("org/jboss/arquillian/graphene/ftest/enricher/empty-findby.html")
    public static class MyPageObject2 {
        @FindBy(css = "#divWebElement")
        private WebElement element;

        public WebElement getElement() {
            return element;
        }
    }

    public static class PageObjectWithoutLocation {
    }

    /*
     * help methods
     */
    private void checkMyPageObject1(MyPageObject1 obj) {
        String actual = obj.getElement().getText();
        assertEquals("pseudo root", actual);
    }

    private void checkMyPageObject2(MyPageObject2 obj) {
        String actual = obj.getElement().getText();
        assertEquals("WebElement content", actual);
    }
}
