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
package org.jboss.arquillian.graphene.screenshooter.ftest;

import java.io.File;


import java.net.URL;
import java.util.List;
import org.fest.util.Collections;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public abstract class AbstractScreenshooterTest {

    @FindBy(id = "root")
    protected Fragment fragment;

    @ArquillianResource
    protected URL contextRoot;

    @Drone
    protected WebDriver browser;

    protected static final String SCREEN_DIR = "target/screenshots/org.jboss.arquillian.graphene.screenshooter.ftest.";

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inCurrentPackage().find("sample.html").loadPage(browser, contextRoot);
    }

    protected void make4WebDriverActions() {
        browser.getCurrentUrl();
        browser.getPageSource();
        Resource.inCurrentPackage().find("container-elements.html").loadPage(browser, contextRoot);
        browser.getTitle();
    }

    protected void checkCreatedSreenshots(String location, List<String> expectedNames) {
        List<String> actualNames = Collections.list(new File(SCREEN_DIR + location).list());
        for (String expectedName : expectedNames) {
            assertTrue(actualNames.contains(expectedName));
        }
    }

    protected void checkCreatedSreenshots(String location) {
        assertFalse(new File(SCREEN_DIR + location).exists());
    }
}