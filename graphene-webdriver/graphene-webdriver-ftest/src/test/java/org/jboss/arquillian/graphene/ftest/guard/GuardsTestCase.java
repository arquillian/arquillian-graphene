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

import static org.jboss.arquillian.graphene.Graphene.guardAjax;
import static org.jboss.arquillian.graphene.Graphene.guardHttp;
import static org.jboss.arquillian.graphene.Graphene.guardNoRequest;
import static org.jboss.arquillian.graphene.Graphene.waitForHttp;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.guard.RequestGuard;
import org.jboss.arquillian.graphene.guard.RequestGuardException;
import org.jboss.arquillian.graphene.javascript.JavaScript;
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
public class GuardsTestCase {

    @FindBy(id = "http")
    private WebElement http;
    @FindBy(id = "none")
    private WebElement none;
    @FindBy(id = "xhr")
    private WebElement xhr;
    @FindBy(id = "xhr-delayed-trigerring")
    private WebElement xhrDelayedTrigerring;
    @FindBy(id = "xhr-delayed-processing")
    private WebElement xhrDelayedProcessing;
    @FindBy(id = "xhr-delayed-trigerring-and-processing")
    private WebElement xhrDelayedTrigerringAndProcessing;
    @FindBy(id = "xhr-delayed-processing-with-code-arg")
    private WebElement xhrDelayedProcessingWithCodeArgument;

    @FindBy(id = "status")
    private WebElement status;

    @JavaScript
    private RequestGuard guard;

    @Drone
    private WebDriver browser;

    @Before
    public void loadPage() {
        URL url = this.getClass().getClassLoader().getResource("org/jboss/arquillian/graphene/ftest/guard/sample1.html");
        browser.get(url.toString());
    }

    @Test
    public void testGuardType() {
        Assert.assertTrue(guardAjax(browser) instanceof WebDriver);
        Assert.assertTrue(guardHttp(browser) instanceof WebDriver);
        Assert.assertTrue(guardNoRequest(browser) instanceof WebDriver);
    }

    @Test
    public void testGuardHttp() {
        guardHttp(http).click();
    }

    @Test
    public void testGuardNoRequest() {
        guardNoRequest(none).click();
    }

    @Test
    public void testGuardAjax() {
        guardAjax(xhr).click();
        assertTrue(status.getText().contains("DONE"));
    }

    @Test
    public void testGuardDelayedAjax() {
        guardAjax(xhrDelayedTrigerring).click();
        assertTrue(status.getText().contains("DONE"));
    }

    @Test
    public void testGuardDelayedAjaxProcessing() {
        guard.setMaximumCallbackTimeout(1500);

        guardAjax(xhrDelayedProcessing).click();
        assertTrue(status.getText().contains("DONE"));
    }

    @Test
    public void testGuardDelayedTrigerringAndProcessing() {
        guard.setMaximumCallbackTimeout(1500);

        guardAjax(xhrDelayedTrigerringAndProcessing).click();
        assertTrue(status.getText().contains("DONE"));
    }

    @Test
    public void testGuardDelayedAjaxProcessingWithCodeArgument() {
        guard.setMaximumCallbackTimeout(1500);

        guardAjax(xhrDelayedProcessingWithCodeArgument).click();
        assertTrue(status.getText().contains("DONE"));
    }

    @Test
    public void testGuardAjaxWithRelocation() throws Exception {
        Activity relocation = new XhrAndRelocationActivity();
        waitForHttp(relocation).perform();
    }

    @Test(expected = RequestGuardException.class)
    public void testGuardHttpFailure() {
        guardHttp(xhr).click();
    }

    @Test(expected = RequestGuardException.class)
    public void testGuardNoRequestFailure() {
        guardNoRequest(http).click();
    }

    @Test(expected = RequestGuardException.class)
    public void testGuardAjaxFailure() {
        guardAjax(http).click();
    }

    @Test(expected = RequestGuardException.class)
    public void testDelayedGuardNoRequest() {
        guardNoRequest(xhrDelayedTrigerring).click();
    }

    public static interface Activity {
        public void perform() throws Exception;
    }

    public class XhrAndRelocationActivity implements Activity {
        @Override
        public void perform() throws Exception {
            browser.findElement(By.id("xhr")).click();
            String url = browser.getCurrentUrl().replace("sample1", "sample2");
            Thread.sleep(200);
            browser.get(url);
        }
    }
}
