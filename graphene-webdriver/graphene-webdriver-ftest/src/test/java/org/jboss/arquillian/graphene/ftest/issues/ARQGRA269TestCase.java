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
package org.jboss.arquillian.graphene.ftest.issues;

import java.net.URL;
import junit.framework.Assert;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Test for https://issues.jboss.org/browse/ARQGRA-269
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class ARQGRA269TestCase {

    @Drone
    private WebDriver browser;

    @FindBy(css="h1")
    private WebElement h1;

    @FindBy(tagName="iframe")
    private WebElement iframe;

    @Before
    public void loadPage() {
        URL page = this.getClass().getClassLoader()
                .getResource("org/jboss/arquillian/graphene/ftest/issues/ARQGRA-269_index.html");
        browser.get(page.toString());
    }

    @Test
    public void testIframeFromWebDriverFindElement() {
        WebElement iframe = browser.findElement(By.tagName("iframe"));
        browser.switchTo().frame(iframe);
        Assert.assertEquals("Inside of the Frame!!", h1.getText().trim());
    }

    @Test
    public void testIframeFromWebElementFindElement() {
        WebElement iframe = browser.findElement(By.tagName("body")).findElement(By.tagName("iframe"));
        browser.switchTo().frame(iframe);
        Assert.assertEquals("Inside of the Frame!!", h1.getText().trim());
    }

    @Test
    public void testInjectedIframe() {
        browser.switchTo().frame(iframe);
        Assert.assertEquals("Inside of the Frame!!", h1.getText().trim());
    }

}
