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

import static org.jboss.arquillian.ajocado.locator.LocatorFactory.jq;
import static org.junit.Assert.*;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @version $Revision$
 */
@RunWith(Arquillian.class)
public class GetAttributeValueTestCase extends SampleApplication {

	private JQueryLocator button = jq("input[type=submit]");
	private JQueryLocator div = jq("div");
	private JQueryLocator paragraph = jq("p#paragraph");

	@Deployment(testable = false)
	public static WebArchive createDeployment() {
		return createDeploymentForClass(GetAttributeValueTestCase.class);
	}

	@Test
	public void testGetDisabledAttributeValue() {
		openContext();
		AttributeLocator<?> disabledAttribute = button
				.getAttribute(new Attribute("disabled"));

		String isDisabled = selenium.getAttribute(disabledAttribute);
		assertEquals( "The value of attribute disabled", "disabled", isDisabled.toLowerCase());
	}

	@Test
	public void testGetUserDefinedAttribute() {
		openContext();
		AttributeLocator<?> fooAttribute = div
				.getAttribute(new Attribute("foo"));

		String foo = selenium.getAttribute(fooAttribute);
		assertEquals("The value of attribute foo", foo.toLowerCase(), "blah");
	}
	
	@Test
	public void testGetValue() {
		openContext();
		String paragraphStyleValue = selenium.getAttribute( paragraph.getAttribute( Attribute.STYLE) );
		
		assertEquals("The value of attribute style", paragraphStyleValue.toLowerCase(), "color: red;");
	}
}
