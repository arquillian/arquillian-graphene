/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.jboss.test.richfacesselenium.library.functionaltest;

import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.test.selenium.AbstractTestCase;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.guard.request.RequestTypeGuardException;
import org.jboss.test.selenium.request.RequestType;
import org.jboss.test.selenium.locator.ElementLocator;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.jboss.test.selenium.locator.LocatorFactory.*;
import static org.jboss.test.selenium.guard.request.RequestTypeGuardFactory.*;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class RequestTypeGuardTestCase extends AbstractTestCase {

    private JavaScript twoClicksWithTimeout = JavaScript.fromResource("two-clicks-with-timeout.js");

    private ElementLocator linkNoRequest = id("none");
    private ElementLocator linkAjaxRequest = id("xhr");
    private ElementLocator linkHttpRequest = id("regular");

    @BeforeMethod
    public void openContext() throws MalformedURLException {
        selenium.open(new URL(contextPath, "./reguest-type-guards.jsf"));
    }

    @Test
    public void testNoRequest() {
        guardNoRequest(selenium).click(linkNoRequest);
    }

    @Test
    public void testNoRequestWrong() {
        try {
            guardNoRequest(selenium).click(linkHttpRequest);
            Assert.fail("The NO request was observed, however HTTP request was expected");
        } catch (RequestTypeGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.HTTP);
        }

        try {
            guardNoRequest(selenium).click(linkAjaxRequest);
            Assert.fail("The NO request was observed, however XHR request was expected");
        } catch (RequestTypeGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.XHR);
        }
    }

    @Test
    public void testHttpRequest() {
        guardHttp(selenium).click(linkHttpRequest);
    }

    @Test
    public void testHttpRequestWrong() {
        try {
            guardHttp(selenium).click(linkNoRequest);
            Assert.fail("The HTTP request was observed, however NONE request was expected");
        } catch (RequestTypeGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.NONE);
        }

        try {
            guardHttp(selenium).click(linkAjaxRequest);
            Assert.fail("The HTTP request was observed, however XHR request was expected");
        } catch (RequestTypeGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.XHR);
            // should catch exception
        }
    }

    @Test
    public void testXhrRequest() {
        guardXhr(selenium).click(linkAjaxRequest);
    }

    @Test
    public void testXhrRequestWrong() {
        try {
            guardXhr(selenium).click(linkNoRequest);
            Assert.fail("The XHR request was observed, however NONE request was expected");
        } catch (RequestTypeGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.NONE);
        }

        try {
            guardXhr(selenium).click(linkHttpRequest);
            Assert.fail("The XHR request was observed, however HTTP request was expected");
        } catch (RequestTypeGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.HTTP);
        }
    }

    @Test
    public void testWaitXhr() {
        long time = System.currentTimeMillis();
        waitXhr(selenium).getEval(twoClicksWithTimeout.parametrize(linkHttpRequest, linkAjaxRequest));
        time -= System.currentTimeMillis();
        Assert.assertTrue(time < -5000);
    }

    @Test
    public void testWaitXhrWrong() {
        try {
            waitXhr(selenium).getEval(twoClicksWithTimeout.parametrize(linkHttpRequest, linkNoRequest));
            Assert.fail();
        } catch (RequestTypeGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.HTTP);
        }

        try {
            waitXhr(selenium).getEval(twoClicksWithTimeout.parametrize(linkHttpRequest, linkHttpRequest));
            Assert.fail();
        } catch (RequestTypeGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.HTTP);
        }
    }

    @Test
    public void testWaitHttp() {
        long time = System.currentTimeMillis();
        waitHttp(selenium).getEval(twoClicksWithTimeout.parametrize(linkAjaxRequest, linkHttpRequest));
        time -= System.currentTimeMillis();
        Assert.assertTrue(time < -5000);
    }

    @Test
    public void testWaitHttpWrong() {
        try {
            waitHttp(selenium).getEval(twoClicksWithTimeout.parametrize(linkAjaxRequest, linkNoRequest));
            Assert.fail();
        } catch (RequestTypeGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.XHR);
        }

        try {
            waitHttp(selenium).getEval(twoClicksWithTimeout.parametrize(linkAjaxRequest, linkAjaxRequest));
            Assert.fail();
        } catch (RequestTypeGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.XHR);
        }
    }
}
