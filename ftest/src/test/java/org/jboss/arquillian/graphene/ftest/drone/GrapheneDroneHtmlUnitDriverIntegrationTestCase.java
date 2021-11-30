/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.ftest.drone;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.ftest.StubbedHttpServerRule;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import static org.junit.Assert.assertTrue;

/**
 * @author Lukas Fryc
 * @author Jan Papousek
 */
@RunWith(Arquillian.class)
@RunAsClient
public class GrapheneDroneHtmlUnitDriverIntegrationTestCase {

    @ClassRule
    public static final StubbedHttpServerRule httpServer = new StubbedHttpServerRule(4321);

    @Drone
    PhantomJSDriver browser;

    @Test
    public void created_instance_should_be_instance_of_requested_driver() {
        assertTrue("browser must be PhantomJS driver", browser instanceof PhantomJSDriver);
    }

    @Test
    public void created_instance_should_be_instance_of_GrapheneProxyInstance() {
        assertTrue("browser must be proxy", browser instanceof GrapheneProxyInstance);
    }

    @Test
    public void created_instance_should_be_able_to_navigate_to_some_page() {
        browser.navigate().to("http://127.0.0.1:" + httpServer.getPort());
    }

    @Test
    public void context_instance_should_be_instance_of_GrapheneProxyInstance() {
        assertTrue("context browser must be proxy", browser instanceof GrapheneProxyInstance);
    }
}