package org.jboss.arquillian.ajocado.testng.listener.separation;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(SeparationTestingConfigurationListener1.class)
public class TestSeparartion1 {
	@Test
	public void testSeparartion1() {
		SeparationTestingConfigurationListener1.incrementInvocationCount1();
	}
}
