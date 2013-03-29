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

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.SessionId;

import qualifier.Reusable;

/**
 * @author <a href="mailto:lfryc@redhat.com>Lukas Fryc</a>
 */
@RunWith(Arquillian.class)
public class TestReusingWebDriverSession extends AbstractInBrowserTest {

    private static SessionId sessionId;

    @Test
    public void testReusableSessionId1(@Drone @Reusable WebDriver driver) {
        testReusableSessionId(driver);
    }

    @Test
    public void testReusableSessionId2(@Drone @Reusable WebDriver driver) {
        testReusableSessionId(driver);
    }

    @Test
    public void testReusableSessionId3(@Drone @Reusable WebDriver driver) {
        testReusableSessionId(driver);
    }

    @Test
    public void testReusableSessionId4(@Drone @Reusable WebDriver driver) {
        testReusableSessionId(driver);
    }

    private void testReusableSessionId(WebDriver d) {
        RemoteWebDriver rd = (RemoteWebDriver) d;
        if (sessionId == null) {
            sessionId = rd.getSessionId();
        } else {
            assertEquals(sessionId, rd.getSessionId());
        }
        rd.get(HUB_URL.toExternalForm());
    }
}
