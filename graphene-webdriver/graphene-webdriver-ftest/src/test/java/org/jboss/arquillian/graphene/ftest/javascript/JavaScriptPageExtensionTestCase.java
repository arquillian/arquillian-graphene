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
package org.jboss.arquillian.graphene.ftest.javascript;

import java.net.URL;
import java.util.List;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.javascript.Dependency;
import org.jboss.arquillian.graphene.javascript.InstallableJavaScript;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class JavaScriptPageExtensionTestCase {

    @Drone
    private WebDriver browser;

    public void loadPage() {
        URL page = this.getClass().getClassLoader().getResource("org/jboss/arquillian/graphene/ftest/javascript/sample.html");
        browser.navigate().to(page);
    }

    @Test
    public void testWithoutSources() {
        loadPage();
        Document document = JSInterfaceFactory.create(Document.class);
        List<WebElement> elements = document.getElementsByTagName("html");
        Assert.assertNotNull(elements);
        Assert.assertEquals(1, elements.size());
    }

    @Test
    public void testWithSources() {
        loadPage();
        HelloWorld helloWorld = JSInterfaceFactory.create(HelloWorld.class);
        Assert.assertEquals("Hello World!", helloWorld.hello());
    }

    @Test
    public void testWithInterfaceDependencies() {
        loadPage();
        HelloWorld2 helloWorld = JSInterfaceFactory.create(HelloWorld2.class);
        Assert.assertEquals("Hello World!", helloWorld.hello());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testWithoutSourceAndWithInterfaceDependencies() {
        loadPage();
        JSInterfaceFactory.create(Document2.class).getTitle();
    }

    @Test
    public void testAbstractClass() {
        loadPage();
        Document3 document = JSInterfaceFactory.create(Document3.class);
        Assert.assertEquals(browser.findElement(By.tagName("h1")), document.getHeader());
    }

    @JavaScript("document")
    public static interface Document {

        String getTitle();

        List<WebElement> getElementsByTagName(String tagName);
    }

    @JavaScript(value="fake")
    @Dependency(interfaces={HelloWorld.class})
    public static interface Document2 {
        String getTitle();
    }

    @JavaScript("document")
    public abstract class Document3 {

        public abstract List<WebElement> getElementsByTagName(String tagName);

        public WebElement getHeader() {
            List<WebElement> elements = getElementsByTagName("h1");
            if (elements.iterator().hasNext()) {
                return elements.iterator().next();
            }
            return null;
        }
    }

    @JavaScript(value = "document.helloworld")
    @Dependency(sources = {"org/jboss/arquillian/graphene/ftest/javascript/hello-world.js"})
    public interface HelloWorld extends InstallableJavaScript {

        String hello();
    }

    @JavaScript(value = "document.helloworld2")
    @Dependency(sources = {"org/jboss/arquillian/graphene/ftest/javascript/hello-world2.js"}, interfaces=HelloWorld.class)
    public interface HelloWorld2 {

        String hello();
    }
}
