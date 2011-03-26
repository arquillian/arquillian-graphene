package org.jboss.arquillian.ajocado.testng.listener.separationAbstract;

import org.testng.annotations.Test;

public class TestSeparartion1 extends AbstractTestSeparartion1 {
	@Test
	public void testAbstractSeparartion1() {
		SeparationTestingConfigurationListener1.incrementInvocationCount1();
	}
}
