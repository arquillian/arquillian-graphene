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
