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
package org.jboss.arquillian.graphene.ftest.guard;

import java.net.URL;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.guard.RequestGuardException;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class GuardsTestCase {

    @Drone
    private WebDriver browser;

    public void loadPage() {
        URL page = this.getClass().getClassLoader().getResource("org/jboss/arquillian/graphene/ftest/guard/sample1.html");
        browser.get(page.toString());
    }

    @Test
    public void testGuardType() {
        Assert.assertTrue(Graphene.guardXhr(browser) instanceof WebDriver);
        Assert.assertTrue(Graphene.guardHttp(browser) instanceof WebDriver);
        Assert.assertTrue(Graphene.guardNoRequest(browser) instanceof WebDriver);
    }

    @Test
    public void testGuardHttp() {
        loadPage();
        Graphene.guardHttp(browser.findElement(By.id("http"))).click();
    }

    @Test
    public void testGuardNoRequest() {
        loadPage();
        Graphene.guardNoRequest(browser.findElement(By.id("none"))).click();
    }

    @Test
    public void testGuardXhr() {
        loadPage();
        Graphene.guardXhr(browser.findElement(By.id("xhr"))).click();
    }

    @Test(expected=RequestGuardException.class)
    public void testGuardHttpFailure() {
        loadPage();
        Graphene.guardHttp(browser.findElement(By.id("xhr"))).click();
    }

    @Test(expected=RequestGuardException.class)
    public void testGuardNoRequestFailure() {
        loadPage();
        Graphene.guardNoRequest(browser.findElement(By.id("http"))).click();
    }

    @Test(expected=RequestGuardException.class)
    public void testGuardXhrFailure() {
        loadPage();
        Graphene.guardXhr(browser.findElement(By.id("http"))).click();
    }


}
