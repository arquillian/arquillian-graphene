package org.jboss.arquillian.ajocado.testng.listener.separation;

import org.testng.ITestContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(SeparationTestingConfigurationListener2.class)
public class TestSeparartion2 {
	@Test
	public void testSeparartion2(ITestContext context) {
		System.out.println(context);
		SeparationTestingConfigurationListener2.incrementInvocationCount2();
	}
}
