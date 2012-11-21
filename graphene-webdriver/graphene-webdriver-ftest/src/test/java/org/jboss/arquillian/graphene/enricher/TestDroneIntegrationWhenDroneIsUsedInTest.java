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
import org.jboss.arquillian.graphene.spi.annotations.Page;
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
public class TestDroneIntegrationWhenDroneIsUsedInTest {

    @Drone
    private WebDriver browser;

    @Page
    private DronePage page;

    @FindBy(id="root")
    private DronePageFragment pageFragment;

    public void loadPage(WebDriver browser) {
        URL url = this.getClass().getClassLoader().getResource("org/jboss/arquillian/graphene/ftest/enricher/sample.html");
        browser.get(url.toString());
    }

    @Test
    public void testDronePageNotNull() {
        Assert.assertNotNull(page.browser);
    }

    @Test
    public void testDronePageFragmentNotNull() {
        Assert.assertNotNull(pageFragment.browser);
    }

    @Test
    public void testDronePageTitle() {
        loadPage(page.browser);
        Assert.assertEquals("Sample Page", page.browser.getTitle());
    }

    @Test
    public void testDronePageFragmentTitle() {
        loadPage(pageFragment.browser);
        Assert.assertEquals("Sample Page", pageFragment.browser.getTitle());
    }

    public static class DronePage {
        @Drone
        private WebDriver browser;
    }

    public static class DronePageFragment {

        @Drone
        private WebDriver browser;

        @FindBy(tagName="span")
        private WebElement span;

    }

}