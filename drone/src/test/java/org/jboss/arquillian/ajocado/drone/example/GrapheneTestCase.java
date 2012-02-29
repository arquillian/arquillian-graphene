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
package org.jboss.arquillian.ajocado.drone.example;

import static org.jboss.arquillian.ajocado.Graphene.elementPresent;
import static org.jboss.arquillian.ajocado.Graphene.guardHttp;
import static org.jboss.arquillian.ajocado.Graphene.id;
import static org.jboss.arquillian.ajocado.Graphene.waitModel;
import static org.jboss.arquillian.ajocado.Graphene.xp;

import java.io.File;
import java.net.URL;

import org.jboss.arquillian.ajocado.ajaxaware.AjaxAwareInterceptor;
import org.jboss.arquillian.ajocado.drone.example.webapp.Credentials;
import org.jboss.arquillian.ajocado.drone.example.webapp.LoggedIn;
import org.jboss.arquillian.ajocado.drone.example.webapp.Login;
import org.jboss.arquillian.ajocado.drone.example.webapp.User;
import org.jboss.arquillian.ajocado.drone.example.webapp.Users;
import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.locator.IdLocator;
import org.jboss.arquillian.ajocado.locator.XPathLocator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests Arquillian Drone extension against Weld Login example.
 *
 * Uses Ajocado driver bound to Firefox browser.
 *
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 *
 */
@RunWith(Arquillian.class)
public class GrapheneTestCase {
    // load ajocado driver
    @Drone
    GrapheneSelenium driver;

    @ArquillianResource
    URL contextPath;

    protected XPathLocator LOGGED_IN = xp("//li[contains(text(),'Welcome')]");
    protected XPathLocator LOGGED_OUT = xp("//li[contains(text(),'Goodbye')]");

    protected IdLocator USERNAME_FIELD = id("loginForm:username");
    protected IdLocator PASSWORD_FIELD = id("loginForm:password");

    protected IdLocator LOGIN_BUTTON = id("loginForm:login");
    protected IdLocator LOGOUT_BUTTON = id("loginForm:logout");

    /**
     * Creates a WAR of a Weld based application using ShrinkWrap
     *
     * @return WebArchive to be tested
     */
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebArchive war = ShrinkWrap
            .create(WebArchive.class, "weld-login.war")
            .addClasses(Credentials.class, LoggedIn.class, Login.class, User.class, Users.class)
            .addAsWebInfResource(new File("src/test/webapp/WEB-INF/beans.xml"))
            .addAsWebInfResource(new File("src/test/webapp/WEB-INF/faces-config.xml"))
            .addAsWebInfResource(new File("src/test/resources/import.sql"))
            .addAsWebResource(new File("src/test/webapp/index.html"))
            .addAsWebResource(new File("src/test/webapp/home.xhtml"))
            .addAsWebResource(new File("src/test/webapp/template.xhtml"))
            .addAsWebResource(new File("src/test/webapp/users.xhtml"))
            .addAsResource(new File("src/test/resources/META-INF/persistence.xml"), ArchivePaths.create("META-INF/persistence.xml"))
            .addAsResource(new File("src/test/resources/import.sql"), ArchivePaths.create("import.sql"))
            .setWebXML(new File("src/test/webapp/WEB-INF/web.xml"));

        return war;
    }

    @Test
    public void testLoginAndLogout() {
        
    	Assert.assertNotNull("Path is not null", contextPath);
        Assert.assertNotNull("AjaxSelenium is not null", driver);

        driver.open(contextPath);
        waitModel.until(elementPresent.locator(USERNAME_FIELD));
        Assert.assertFalse("User should not be logged in!", driver.isElementPresent(LOGOUT_BUTTON));
        driver.type(USERNAME_FIELD, "demo");
        driver.type(PASSWORD_FIELD, "demo");

        guardHttp(driver).click(LOGIN_BUTTON);
        Assert.assertTrue("User should be logged in!", driver.isElementPresent(LOGGED_IN));

        guardHttp(driver).click(LOGOUT_BUTTON);
        Assert.assertTrue("User should not be logged in!", driver.isElementPresent(LOGGED_OUT));
    }

}
