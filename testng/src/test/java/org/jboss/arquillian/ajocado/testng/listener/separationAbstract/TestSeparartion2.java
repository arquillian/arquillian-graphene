package org.jboss.arquillian.ajocado.testng.listener.separationAbstract;

import org.testng.annotations.Test;

public class TestSeparartion2 extends AbstractTestSeparartion2 {
	@Test
	public void testAbstractSeparartion2() {
		SeparationTestingConfigurationListener2.incrementInvocationCount2();
	}
}
