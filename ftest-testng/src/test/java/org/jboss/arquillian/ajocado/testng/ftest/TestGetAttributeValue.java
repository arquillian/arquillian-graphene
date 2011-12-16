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

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.annotations.Test;

/**
 * @author <a href="mailto:lfryc@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
public class TestGetAttributeValue extends AbstractTest {

	private JQueryLocator button = jq("input[type=submit]");
	private JQueryLocator div = jq("div");
	private JQueryLocator paragraph = jq("p#paragraph");

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return createDeploymentForClass(TestGetAttributeValue.class);
	}

	@Test
	public void testGetDisabledAttributeValue() {

		AttributeLocator<?> disabledAttribute = button
				.getAttribute(new Attribute("disabled"));

		String isDisabled = selenium.getAttribute(disabledAttribute);
		assertEquals(isDisabled.toLowerCase(), "disabled",
				"The value of attribute disabled");
	}

	@Test
	public void testGetUserDefinedAttribute() {

		AttributeLocator<?> fooAttribute = div
				.getAttribute(new Attribute("foo"));

		String foo = selenium.getAttribute(fooAttribute);
		assertEquals(foo.toLowerCase(), "blah", "The value of attribute foo");
	}
	
	@Test
	public void testGetValue() {
		
		String paragraphStyleValue = selenium.getAttribute( paragraph.getAttribute( Attribute.STYLE) );
		
		assertEquals(paragraphStyleValue.toLowerCase(), "color: red;", "The value of attribute style");
	}
}
