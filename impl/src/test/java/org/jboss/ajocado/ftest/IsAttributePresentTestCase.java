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
package org.jboss.ajocado.ftest;

import java.net.MalformedURLException;

import java.net.URL;

import org.jboss.ajocado.AbstractTestCase;

import org.jboss.ajocado.locator.Attribute;
import org.jboss.ajocado.locator.AttributeLocator;
import org.jboss.ajocado.locator.IdLocator;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

import com.thoughtworks.selenium.SeleniumException;

import static org.jboss.ajocado.utils.SimplifiedFormat.format;
import static org.jboss.ajocado.locator.LocatorFactory.*;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class IsAttributePresentTestCase extends AbstractTestCase {

    final IdLocator noRequest = id("none");
    final AttributeLocator noRequestHref = noRequest.getAttribute(Attribute.HREF);
    final AttributeLocator noRequestStyle = noRequest.getAttribute(Attribute.STYLE);

    final IdLocator notExists = id("no-such-element");
    final AttributeLocator notExistsClass = notExists.getAttribute(Attribute.CLASS);

    final String expectedMessage = "ERROR: element is not found";

    @BeforeMethod(alwaysRun = true)
    public void openContext() throws MalformedURLException {
        selenium.open(new URL(contextPath, "./reguest-type-guards.jsf"));
    }

    public void testAttributePresent() {
        Assert.assertTrue(selenium.isAttributePresent(noRequestHref));
    }

    @Test
    public void testAttributeNotPresent() {
        Assert.assertFalse(selenium.isAttributePresent(noRequestStyle));
    }

    public void testElementNotPresent() {
        try {
            selenium.isAttributePresent(notExistsClass);
            Assert.fail("should raise a exception pointing that there is not such element");
        } catch (SeleniumException e) {
            if (!expectedMessage.equals(e.getMessage())) {
                Assert.fail(format("message should be '{0}'", expectedMessage));
            }
        }
    }

    @Test
    public void testExposedMember() {
        Assert.assertTrue(attributePresent.locator(noRequestHref).isTrue());
        Assert.assertFalse(attributePresent.locator(noRequestStyle).isTrue());

        try {
            attributePresent.locator(notExistsClass).isTrue();
            Assert.fail("should raise a exception pointing that there is not such element");
        } catch (SeleniumException e) {
            if (!expectedMessage.equals(e.getMessage())) {
                Assert.fail(format("message should be '{0}'", expectedMessage));
            }
        }
    }
}
