/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.ftest.enricher;

import java.io.IOException;
import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class TestAssertionErrorOnPageFragmentOrObject {

    @Drone
    private WebDriver browser;
    @ArquillianResource
    private URL contextRoot;

    @FindBy(css = "body")
    private PageOrFragmentThrowingAssertionError fragment;
    @Page
    private PageOrFragmentThrowingAssertionError page;

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inCurrentPackage().find("sample.html").loadPage(browser, contextRoot);
    }

    @Test(expected = AssertionError.class)
    public void testAssertionErrorFromPageFragmentIsNotWrapped() {
        fragment.throwAssertionError();
    }

    @Test(expected = AssertionError.class)
    public void testAssertionErrorFromPageObjectIsNotWrapped() {
        page.throwAssertionError();
    }

    @Test(expected = IOException.class)
    public void testCheckedExceptionFromPageFragmentIsNotWrapped() throws Exception {
    	fragment.throwCheckedException();
    }

    @Test(expected = IOException.class)
    public void testCheckedExceptionFromPageObjectIsNotWrapped() throws Exception {
        page.throwCheckedException();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRuntimeExceptionFromPageFragmentIsNotWrapped() {
    	fragment.throwRuntimeException();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRuntimeExceptionFromPageObjectIsNotWrapped() {
        page.throwRuntimeException();
    }

    public static class PageOrFragmentThrowingAssertionError {

        public void throwAssertionError() {
            throw new AssertionError("This is assertion error!");
        }
        
        public void throwCheckedException() throws Exception {
        	throw new IOException("this is checked exception");
        }
        
        public void throwRuntimeException() {
        	throw new IllegalArgumentException("this is runtime exception");
        }
    }
}
