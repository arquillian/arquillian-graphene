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
import org.jboss.arquillian.graphene.page.InitialPage;
import org.jboss.arquillian.graphene.page.Location;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.arquillian.junit.Arquillian;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import static org.jboss.arquillian.graphene.screenshooter.ftest.AbstractScreenshooterTest.SCREEN_DIR;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class TestScreenshooterWithLocationAndInitialPage {

    @Drone
    protected WebDriver browser;

    private static final String TEST_NAME = TestScreenshooterWithLocationAndInitialPage.class.getSimpleName();

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inCurrentPackage().all().buildWar("test.war");
    }

    @ArquillianResource
    protected URL contextRoot;

    @Test
    public void screenshooter_conf_should_be_used(@InitialPage PageObject po) {
        assertEquals(po.getFragment().getInnerElement().getText(), "pseudo root");
        make4WebDriverActions();
        List<String> actualNames = Collections.list(
                new File(SCREEN_DIR + TEST_NAME + "/screenshooter_conf_should_be_used").list());
        actualNames.contains("before.png");
    }

    protected void make4WebDriverActions() {
        browser.getCurrentUrl();
        browser.getPageSource();
        Resource.inCurrentPackage().find("container-elements.html").loadPage(browser, contextRoot);
        browser.getTitle();
    }

    @Location("org/jboss/arquillian/graphene/screenshooter/ftest/sample.html")
    public class PageObject {

        @FindBy(id = "root")
        protected Fragment fragment;

        public Fragment getFragment() {
            return fragment;
        }
    }

}