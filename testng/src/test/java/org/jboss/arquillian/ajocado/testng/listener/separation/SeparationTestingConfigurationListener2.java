package org.jboss.arquillian.ajocado.testng.listener.separation;

import static org.testng.Assert.assertEquals;

import org.jboss.arquillian.ajocado.testng.listener.AbstractConfigurationListener;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class SeparationTestingConfigurationListener2 extends
		AbstractConfigurationListener {

	private static int invocationCount2 = 0;
	
	public static void incrementInvocationCount2() {
		invocationCount2 += 1;
	}

	@BeforeClass
	public void verifyNoInvocation2() {
		assertEquals(invocationCount2, 0);
	}

	@AfterClass
	public void verifyOnlyInvocation2() {
		assertEquals(invocationCount2, 1);
	}
	
	@Override
	public String toString() {
		return SeparationTestingConfigurationListener2.class.getName();
	}
}
