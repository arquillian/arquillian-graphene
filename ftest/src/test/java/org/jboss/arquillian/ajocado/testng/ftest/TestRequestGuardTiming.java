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
package org.jboss.arquillian.ajocado.testng.ftest;

import static org.jboss.arquillian.ajocado.Ajocado.guardHttp;
import static org.jboss.arquillian.ajocado.Ajocado.guardNoRequest;
import static org.jboss.arquillian.ajocado.Ajocado.guardXhr;
import static org.jboss.arquillian.ajocado.Ajocado.id;
import static org.jboss.arquillian.ajocado.Ajocado.waitForHttp;
import static org.jboss.arquillian.ajocado.Ajocado.waitForXhr;

import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestRequestGuardTiming extends AbstractTest {

    private JavaScript twoClicksWithTimeout = JavaScript.fromResource("two-clicks-with-timeout.js");

    private ElementLocator<?> linkNoRequest = id("noRequest");
    private ElementLocator<?> linkAjaxRequest = id("ajax");
    private ElementLocator<?> linkHttpRequest = id("http");

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return createDeploymentForClass(TestRequestGuardTiming.class);
    }

    @Test
    public void testGuardXhrTiming() {

        long time = System.currentTimeMillis();
        guardXhr(selenium).click(linkAjaxRequest);
        time -= System.currentTimeMillis();
        Assert.assertTrue(time > -1000, "The intercepting of ajax request should not last more than 1 sec");
    }

    @Test
    public void testGuardHttpTiming() {

        long time = System.currentTimeMillis();
        guardHttp(selenium).click(linkHttpRequest);
        time -= System.currentTimeMillis();
        Assert.assertTrue(time > -1000, "The intercepting of the http request should not last more than 1 sec");
    }

    @Test
    public void testGuardNoneTiming() {

        long time = System.currentTimeMillis();
        guardNoRequest(selenium).click(linkNoRequest);
        time -= System.currentTimeMillis();
        Assert.assertTrue(time < -5000, "The checking of none request should last more than 5 sec");
    }

    @Test
    public void testWaitHttpTiming() {
        long time = System.currentTimeMillis();
        waitForHttp(selenium).getEval(twoClicksWithTimeout.parametrize(linkAjaxRequest, linkHttpRequest));
        time -= System.currentTimeMillis();
        Assert.assertTrue(time < -5000);
    }

    @Test
    public void testWaitForXhrTiming() {
        long time = System.currentTimeMillis();
        waitForXhr(selenium).getEval(twoClicksWithTimeout.parametrize(linkHttpRequest, linkAjaxRequest));
        time -= System.currentTimeMillis();
        Assert.assertTrue(time < -5000);
    }

}
