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
import org.jboss.arquillian.graphene.guard.RequestGuard;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.page.RequestType;
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
public class RequestGuardTestCase {

    @Drone
    private WebDriver browser;

    public void loadPage() {
        URL page = this.getClass().getClassLoader().getResource("org/jboss/arquillian/graphene/ftest/guard/sample1.html");
        browser.get(page.toString());
    }

    @Test
    public void testXhr() throws InterruptedException {
        loadPage();
        RequestGuard guard = JSInterfaceFactory.create(RequestGuard.class);
        Assert.assertEquals(RequestType.HTTP, guard.getRequestDone());
        guard.clearRequestDone();
        Assert.assertEquals(RequestType.NONE, guard.getRequestDone());
        browser.findElement(By.id("xhr")).click();
        Assert.assertEquals(RequestType.XHR, guard.getRequestDone());
    }

    @Test
    public void testHttp() {
        loadPage();
        RequestGuard guard = JSInterfaceFactory.create(RequestGuard.class);
        Assert.assertEquals(RequestType.HTTP, guard.getRequestDone());
        guard.clearRequestDone();
        Assert.assertEquals(RequestType.NONE, guard.getRequestDone());
        browser.findElement(By.id("http")).click();
        Assert.assertEquals(RequestType.HTTP, guard.getRequestDone());
    }

}
