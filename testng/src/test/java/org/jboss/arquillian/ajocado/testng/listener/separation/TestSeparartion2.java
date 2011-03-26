package org.jboss.arquillian.ajocado.testng.listener.separation;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(SeparationTestingConfigurationListener2.class)
public class TestSeparartion2 {
	@Test
	public void testSeparartion2() {
		SeparationTestingConfigurationListener2.incrementInvocationCount2();
	}
}
