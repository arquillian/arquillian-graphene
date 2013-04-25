/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.graphene.ftest.drone;

import org.jboss.arquillian.drone.api.annotation.Default;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * @author Lukas Fryc
 * @author Jan Papousek
 */
@RunWith(Arquillian.class)
public class GrapheneDroneHtmlUnitDriverIntegrationTestCase {

    @Drone
    HtmlUnitDriver browser;

    @Test
    public void created_instance_should_be_instance_of_requested_driver() {
        assertTrue("browser must be HtmlUnitDriver", browser instanceof HtmlUnitDriver);
    }

    @Test
    public void created_instance_should_be_instance_of_GrapheneProxyInstance() {
        assertTrue("browser must be proxy", browser instanceof GrapheneProxyInstance);
    }

    @Test
    public void created_instance_should_be_able_to_navigate_to_some_page() {
        browser.navigate().to("http://127.0.0.1:14444");
    }

    @Test
    public void context_instance_should_be_instance_of_GrapheneProxyInstance() {
        assertTrue("context browser must be proxy", browser instanceof GrapheneProxyInstance);
    }
}