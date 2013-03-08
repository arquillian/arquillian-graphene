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
package org.jboss.arquillian.graphene.webdriver;

import java.net.URL;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class FindElementTestCase {

    @Drone
    private WebDriver browser;

    @FindBy(className = "make-stale")
    private WebElement makeStale;

    @Before
    public void loadPage() {
        URL page = this.getClass().getClassLoader()
                .getResource("org/jboss/arquillian/graphene/ftest/webdriver/staleelements.html");
        browser.get(page.toString());
    }

    @Test
    public void testFindStaleElementOnWebDriver() {
        WebElement stale = browser.findElement(By.className("stale"));
        Assert.assertTrue(stale.isDisplayed());
        makeStale.click();
        Assert.assertTrue(stale.isDisplayed());
    }

    @Test
    public void testFindStaleElementsOnWebDriver() {
        WebElement stale = browser.findElements(By.className("stale")).get(0);
        Assert.assertTrue(stale.isDisplayed());
        makeStale.click();
        Assert.assertTrue(stale.isDisplayed());
    }

    @Test
    public void testFindStaleElementOnWebElement() {
        WebElement stale = browser.findElement(By.tagName("body")).findElement(By.className("stale"));
        Assert.assertTrue(stale.isDisplayed());
        makeStale.click();
        Assert.assertTrue(stale.isDisplayed());
    }

    @Test
    public void testFindStaleElementsOnWebElement() {
        WebElement stale = browser.findElement(By.tagName("body")).findElements(By.className("stale")).get(0);
        Assert.assertTrue(stale.isDisplayed());
        makeStale.click();
        Assert.assertTrue(stale.isDisplayed());
    }

}
