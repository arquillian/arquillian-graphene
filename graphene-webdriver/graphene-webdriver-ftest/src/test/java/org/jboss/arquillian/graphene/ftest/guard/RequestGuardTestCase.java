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

import static org.jboss.arquillian.graphene.Graphene.waitAjax;
import static org.junit.Assert.assertEquals;

import java.net.URL;
import org.jboss.arquillian.drone.api.annotation.Default;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.guard.RequestGuard;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.page.RequestType;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class RequestGuardTestCase {

    @Drone
    private WebDriver browser;

    @FindBy(id = "xhr")
    private WebElement xhr;
    @FindBy(id = "http")
    private WebElement http;
    @FindBy(id = "status")
    private WebElement status;

    @Before
    public void loadPage() {
        URL page = this.getClass().getClassLoader().getResource("org/jboss/arquillian/graphene/ftest/guard/sample1.html");
        browser.get(page.toString());
    }

    @Test
    public void testXhr() throws InterruptedException {
        RequestGuard guard = JSInterfaceFactory.create(GrapheneContext.getContextFor(Default.class), RequestGuard.class);
        assertEquals(RequestType.HTTP, guard.getRequestType());
        guard.clearRequestDone();
        assertEquals(RequestType.NONE, guard.getRequestType());
        xhr.click();
        waitAjax().until().element(status).text().contains("DONE");
        assertEquals(RequestType.XHR, guard.getRequestType());
    }

    @Test
    public void testHttp() {
        RequestGuard guard = JSInterfaceFactory.create(GrapheneContext.getContextFor(Default.class), RequestGuard.class);
        assertEquals(RequestType.HTTP, guard.getRequestType());
        guard.clearRequestDone();
        assertEquals(RequestType.NONE, guard.getRequestType());
        http.click();
        assertEquals(RequestType.HTTP, guard.getRequestType());
    }

}
