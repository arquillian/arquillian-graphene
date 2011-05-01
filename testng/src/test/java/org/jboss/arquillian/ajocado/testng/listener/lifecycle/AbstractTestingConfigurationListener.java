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
package org.jboss.arquillian.ajocado.testng.listener.lifecycle;

import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.AFTER_CLASS;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.AFTER_METHOD;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.BEFORE_CLASS;
import static org.jboss.arquillian.ajocado.testng.listener.lifecycle.LifecycleTestingConfigurationListener.Phase.BEFORE_METHOD;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(LifecycleTestingConfigurationListener.class)
public class AbstractTestingConfigurationListener {

    @BeforeClass
    public void beforeClass() {
        LifecycleTestingConfigurationListener.assertPhase(BEFORE_CLASS);
    }

    @BeforeMethod
    public void beforeMethod() {
        LifecycleTestingConfigurationListener.assertPhase(BEFORE_METHOD);
    }

    @AfterMethod
    public void afterMethod() {
        LifecycleTestingConfigurationListener.assertPhase(AFTER_METHOD);
    }

    @AfterClass
    public void afterClass() {
        LifecycleTestingConfigurationListener.assertPhase(AFTER_CLASS);
    }
}
