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

import static org.jboss.arquillian.ajocado.Ajocado.attributePresent;
import static org.jboss.arquillian.ajocado.locator.LocatorFactory.id;

import java.net.MalformedURLException;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.IdLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.SeleniumException;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestAttributePresent extends AbstractTest {

    final IdLocator existingElement = id("paragraph");
    final AttributeLocator<?> attributeExist = existingElement.getAttribute(Attribute.STYLE);
    final AttributeLocator<?> attributeNotExist = existingElement.getAttribute(Attribute.CLASS);

    final IdLocator notExistentElement = id("no-such-element");
    final AttributeLocator<?> attributeOfNotExistsElement = notExistentElement.getAttribute(Attribute.CLASS);

    final String expectedMessage = "ERROR: element is not found";

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return createDeploymentForClass(TestAttributePresent.class);
    }

    @Test
    public void testAttributePresent() throws MalformedURLException {
        openContext();
        Assert.assertTrue(selenium.isAttributePresent(attributeExist));
    }

    @Test
    public void testAttributeNotPresent() {
        openContext();
        Assert.assertFalse(selenium.isAttributePresent(attributeNotExist));
    }

    @Test
    public void testElementNotPresent() {
        openContext();
        try {
            selenium.isAttributePresent(attributeOfNotExistsElement);
            Assert.fail("should raise a exception pointing that there is not such element");
        } catch (SeleniumException e) {
            Assert.assertTrue(e.getMessage().startsWith("ERROR: element '"), "message was: " + e.getMessage());
            Assert.assertTrue(e.getMessage().endsWith("' is not found"), "message was: " + e.getMessage());
        }
    }

    @Test
    public void testExposedMember() {
        openContext();
        Assert.assertTrue(attributePresent.locator(attributeExist).isTrue());
        Assert.assertFalse(attributePresent.locator(attributeNotExist).isTrue());

        try {
            attributePresent.locator(attributeOfNotExistsElement).isTrue();
            Assert.fail("should raise a exception pointing that there is not such element");
        } catch (SeleniumException e) {
            Assert.assertTrue(e.getMessage().startsWith("ERROR: element '"), "message was: " + e.getMessage());
            Assert.assertTrue(e.getMessage().endsWith("' is not found"), "message was: " + e.getMessage());
        }
    }
}
