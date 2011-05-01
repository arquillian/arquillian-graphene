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
package org.jboss.arquillian.ajocado.testng.listener.separation;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.testng.listener.AbstractConfigurationListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class SeparationTestingConfigurationListener1 extends
		AbstractConfigurationListener {

	private static int invocationCount1 = 0;
	
	public static void incrementInvocationCount1() {
		invocationCount1 += 1;
	}

	@BeforeClass
	public void verifyNoInvocation1() {
		assertEquals(invocationCount1, 0);
	}

	@AfterClass
	public void verifyOnlyInvocation1() {
		assertEquals(invocationCount1, 1);
	}
	
	@Override
	public String toString() {
		return SeparationTestingConfigurationListener1.class.getName();
	}
}
