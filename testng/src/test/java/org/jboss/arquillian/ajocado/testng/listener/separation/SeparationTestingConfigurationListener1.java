package org.jboss.arquillian.ajocado.testng.listener.separation;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.testng.listener.AbstractConfigurationListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class SeparationTestingConfigurationListener1 extends
		AbstractConfigurationListener {

	private static int invocationCount1 = 0;
	
	public static void incredemtInvocationCount1() {
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
