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
package org.jboss.arquillian.ajocado.junit.ftest;

import static org.jboss.arquillian.ajocado.Ajocado.guardHttp;
import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.id;
import static org.jboss.arquillian.ajocado.Ajocado.waitForHttp;
import static org.jboss.arquillian.ajocado.Ajocado.waitForXhr;

import org.jboss.arquillian.ajocado.guard.RequestGuardException;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.request.RequestType;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
@RunWith(Arquillian.class)
public class RequestGuardTestCase extends SampleApplication {

    private JavaScript twoClicksWithTimeout = JavaScript.fromResource("two-clicks-with-timeout.js");

    private ElementLocator<?> linkNoRequest = id("noRequest");
    private ElementLocator<?> linkAjaxRequest = id("ajax");
    private ElementLocator<?> linkHttpRequest = id("http");

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return createDeploymentForClass(RequestGuardTestCase.class);
    }

    @Test
    public void testGuardNone() {
        openContext();
        guardNoRequest(selenium).click(linkNoRequest);
    }

    @Test
    public void testGuardNoneButHttpDone() {
        openContext();
        try {
            guardNoRequest(selenium).click(linkHttpRequest);
            Assert.fail("The NO request was observed, however HTTP request was expected");
        } catch (RequestGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.HTTP);
        }
    }

    @Test
    public void testGuardNoneButXhrDone() {
        openContext();
        try {
            guardNoRequest(selenium).click(linkAjaxRequest);
            Assert.fail("The NO request was observed, however XHR request was expected");
        } catch (RequestGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.XHR);
        }
    }

    @Test
    public void testGuardHttp() {
        openContext();
        guardHttp(selenium).click(linkHttpRequest);
    }

    @Test
    public void testGuardHttpButNoneDone() {
        openContext();
        try {
            guardHttp(selenium).click(linkNoRequest);
            Assert.fail("The HTTP request was observed, however NONE request was expected");
        } catch (RequestGuardException e) {
            Assert.assertTrue("NONE request expected, but " + e.getRequestDone() + " was done",
                    e.getRequestDone() == RequestType.NONE);
        }
    }

    @Test
    public void testGuardHttpButXhrDone() {
        openContext();
        try {
            guardHttp(selenium).click(linkAjaxRequest);
            Assert.fail("The HTTP request was observed, however XHR request was expected");
        } catch (RequestGuardException e) {
            Assert.assertTrue("XHR request expected, but " + e.getRequestDone() + " was done",
                    e.getRequestDone() == RequestType.XHR);
        }
    }

    @Test
    public void testGuardXhr() {
        openContext();
        guardXhr(selenium).click(linkAjaxRequest);
    }

    @Test
    public void testGuardXhrButNoneDone() {
        openContext();
        try {
            guardXhr(selenium).click(linkNoRequest);
            Assert.fail("The XHR request was observed, however NONE request was expected");
        } catch (RequestGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.NONE);
        }
    }

    @Test
    public void testGuardXhrButHttpDone() {
        openContext();
        try {
            guardXhr(selenium).click(linkHttpRequest);
            Assert.fail("The XHR request was observed, however HTTP request was expected");
        } catch (RequestGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.HTTP);
        }
    }

    @Test
    public void testWaitForXhr() {
        openContext();
        waitForXhr(selenium).getEval(twoClicksWithTimeout.parametrize(linkHttpRequest, linkAjaxRequest));
    }

    @Test
    public void testWaitXhrButNoneAndHttpDone() {
        openContext();
        try {
            waitForXhr(selenium).getEval(twoClicksWithTimeout.parametrize(linkHttpRequest, linkNoRequest));
            Assert.fail();
        } catch (RequestGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.HTTP);
        }
    }

    @Test
    public void testWaitXhrButTwoHttpDone() {
        openContext();
        try {
            waitForXhr(selenium).getEval(twoClicksWithTimeout.parametrize(linkHttpRequest, linkHttpRequest));
            Assert.fail();
        } catch (RequestGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.HTTP);
        }
    }

    @Test
    public void testWaitHttp() {
        openContext();
        waitForHttp(selenium).getEval(twoClicksWithTimeout.parametrize(linkAjaxRequest, linkHttpRequest));
    }

    @Test
    public void testWaitHttpButNoneAndXhrDone() {
        openContext();
        try {
            waitForHttp(selenium).getEval(twoClicksWithTimeout.parametrize(linkAjaxRequest, linkNoRequest));
            Assert.fail();
        } catch (RequestGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.XHR);
        }
    }

    @Test
    public void testWaitHttpButTwoXhrDone() {
        openContext();
        try {
            waitForHttp(selenium).getEval(twoClicksWithTimeout.parametrize(linkAjaxRequest, linkAjaxRequest));
            Assert.fail();
        } catch (RequestGuardException e) {
            Assert.assertTrue(e.getRequestDone() == RequestType.XHR);
        }
    }
}
