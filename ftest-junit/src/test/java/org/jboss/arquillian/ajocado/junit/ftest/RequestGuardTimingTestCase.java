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

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
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
public class RequestGuardTimingTestCase extends SampleApplication {

    private JavaScript twoClicksWithTimeout = JavaScript.fromResource("two-clicks-with-timeout.js");

    private ElementLocator<?> linkNoRequest = id("noRequest");
    private ElementLocator<?> linkAjaxRequest = id("ajax");
    private ElementLocator<?> linkHttpRequest = id("http");

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return createDeploymentForClass(RequestGuardTimingTestCase.class);
    }

    @Test
    public void testGuardXhrTiming() {
        openContext();
        long time = System.currentTimeMillis();
        guardXhr(selenium).click(linkAjaxRequest);
        time -= System.currentTimeMillis();
        Assert.assertTrue("The intercepting of ajax request should not last more than 1 sec", time > -1000);
    }

    @Test
    public void testGuardHttpTiming() {
        openContext();
        long time = System.currentTimeMillis();
        guardHttp(selenium).click(linkHttpRequest);
        time -= System.currentTimeMillis();
        Assert.assertTrue("The intercepting of the http request should not last more than 1 sec", time > -1000);
    }

    @Test
    public void testGuardNoneTiming() {
        openContext();
        long time = System.currentTimeMillis();
        guardNoRequest(selenium).click(linkNoRequest);
        time -= System.currentTimeMillis();
        Assert.assertTrue("The checking of none request should last more than 5 sec", time < -5000);
    }

    @Test
    public void testWaitHttpTiming() {
        openContext();
        long time = System.currentTimeMillis();
        waitForHttp(selenium).getEval(twoClicksWithTimeout.parametrize(linkAjaxRequest, linkHttpRequest));
        time -= System.currentTimeMillis();
        Assert.assertTrue(time < -5000);
    }

    @Test
    public void testWaitForXhrTiming() {
        openContext();
        long time = System.currentTimeMillis();
        waitForXhr(selenium).getEval(twoClicksWithTimeout.parametrize(linkHttpRequest, linkAjaxRequest));
        time -= System.currentTimeMillis();
        Assert.assertTrue(time < -5000);
    }

}
