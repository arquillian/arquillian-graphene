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
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class EnrichmentTestCase {

    @Drone
    private WebDriver browser;

    @JavaScript
    private Document document;

    @JavaScript
    private Screen screen;

    public void loadPage() {
        URL page = this.getClass().getClassLoader().getResource("org/jboss/arquillian/graphene/ftest/javascript/sample.html");
        browser.get(page.toString());
    }

    @Test
    public void testNotNull() {
        Assert.assertNotNull(document);
        Assert.assertNotNull(screen);
    }

    @Test
    public void testDocumentMethods() {
        loadPage();
        Assert.assertEquals("Hello World!", document.getElementsByTagName("h1").get(0).getText());
    }

    @Test
    public void testScreenMethods() {
        loadPage();
        Assert.assertNotNull(screen.getHeight());
        Assert.assertNotNull(screen.getWidth());
    }

    @JavaScript("document")
    public static interface Document {

        String getTitle();

        List<WebElement> getElementsByTagName(String tagName);
    }

    @JavaScript("window.screen")
    public static interface Screen {
        Long getWidth();
        Long getHeight();
    }

}
