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
package org.jboss.arquillian.ajocado.testng.listener;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import org.testng.annotations.Test;

public class TestListenerThreadLocal {

	@Test
	public void testDefaultIsNull() {
		ListenerThreadLocal<String> store = new ListenerThreadLocal<String>();
		assertNull(store.get(TestListener1.class));
		assertNull(store.get(TestListener2.class));
	}

	@Test
	public void testInitialValue() {
		ListenerThreadLocal<String> store = new ListenerThreadLocal<String>() {
			@Override
			protected String initialValue() {
				return "initial";
			}
		};
		assertEquals(store.get(TestListener1.class), "initial");
		assertEquals(store.get(TestListener2.class), "initial");
	}

	@Test
	public void testDefaultChangeOne() {
		ListenerThreadLocal<String> store = new ListenerThreadLocal<String>();
		store.set(TestListener1.class, "test1");
		assertEquals(store.get(TestListener1.class), "test1");
		assertNull(store.get(TestListener2.class));
	}

	@Test
	public void testInitialChangeOne() {
		ListenerThreadLocal<String> store = new ListenerThreadLocal<String>() {
			@Override
			protected String initialValue() {
				return "initial";
			}
		};
		store.set(TestListener1.class, "test1");
		assertEquals(store.get(TestListener1.class), "test1");
		assertEquals(store.get(TestListener2.class), "initial");
	}

	@Test
	public void testInitialSetNull() {
		ListenerThreadLocal<String> store = new ListenerThreadLocal<String>() {
			@Override
			protected String initialValue() {
				return "initial";
			}
		};
		store.set(TestListener1.class, null);
		assertNull(store.get(TestListener1.class));
		assertEquals(store.get(TestListener2.class), "initial");
	}

	@Test
	public void testInitialChangeBoth() {
		ListenerThreadLocal<String> store = new ListenerThreadLocal<String>() {
			@Override
			protected String initialValue() {
				return "initial";
			}
		};
		store.set(TestListener1.class, "test1");
		store.set(TestListener2.class, "test2");
		assertEquals(store.get(TestListener1.class), "test1");
		assertEquals(store.get(TestListener2.class), "test2");

	}

	private class TestListener1 extends AbstractConfigurationListener {
	}

	private class TestListener2 extends AbstractConfigurationListener {
	}
}
