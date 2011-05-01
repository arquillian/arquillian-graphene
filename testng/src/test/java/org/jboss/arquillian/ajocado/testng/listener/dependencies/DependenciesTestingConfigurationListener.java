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
package org.jboss.arquillian.ajocado.testng.listener.dependencies;

import static org.jboss.arquillian.ajocado.testng.listener.dependencies.DependenciesTestingConfigurationListener.Phase.BEFORE_CLASS1;
import static org.jboss.arquillian.ajocado.testng.listener.dependencies.DependenciesTestingConfigurationListener.Phase.BEFORE_CLASS2;
import static org.jboss.arquillian.ajocado.testng.listener.dependencies.DependenciesTestingConfigurationListener.Phase.BEFORE_METHOD1;
import static org.jboss.arquillian.ajocado.testng.listener.dependencies.DependenciesTestingConfigurationListener.Phase.BEFORE_METHOD2;
import static org.jboss.arquillian.ajocado.testng.listener.dependencies.DependenciesTestingConfigurationListener.Phase.TEST1;
import static org.jboss.arquillian.ajocado.testng.listener.dependencies.DependenciesTestingConfigurationListener.Phase.TEST2;
import static org.testng.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;
import org.jboss.arquillian.ajocado.testng.listener.AbstractConfigurationListener;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(enabled = false)
public class DependenciesTestingConfigurationListener extends AbstractConfigurationListener {

    private static final Phase[] PHASES = new Phase[] { BEFORE_CLASS1, BEFORE_CLASS2, BEFORE_METHOD1, BEFORE_METHOD2,
            TEST1, BEFORE_METHOD1, BEFORE_METHOD2, TEST2, BEFORE_CLASS1, BEFORE_CLASS2, BEFORE_METHOD1, BEFORE_METHOD2,
            TEST1, BEFORE_METHOD1, BEFORE_METHOD2, TEST2 };

    private static int phase = 0;

    @BeforeClass(dependsOnMethods = "beforeClass1")
    public void beforeClass2() {
        assertPhase(BEFORE_CLASS2);
    }

    @BeforeClass
    public void beforeClass1() {
        assertPhase(BEFORE_CLASS1);
    }

    @BeforeMethod(dependsOnMethods = "beforeMethod1")
    public void beforeMethod2() {
        assertPhase(BEFORE_METHOD2);
    }

    @BeforeMethod
    public void beforeMethod1() {
        assertPhase(BEFORE_METHOD1);
    }

    public static void assertPhase(Phase... actualPhases) {
        final Phase phaseName = PHASES[phase++];
        assertTrue(ArrayUtils.contains(actualPhases, phaseName),
            "Actual phase options (" + Arrays.deepToString(actualPhases) + ") doesn't match expected phase ("
                + phaseName + ")");
    }

    public static enum Phase {
        TEST1, TEST2, BEFORE_CLASS1, BEFORE_CLASS2, BEFORE_METHOD1, BEFORE_METHOD2
    }

}
