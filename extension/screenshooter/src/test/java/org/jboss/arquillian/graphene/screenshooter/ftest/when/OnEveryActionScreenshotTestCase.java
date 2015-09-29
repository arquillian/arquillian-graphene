/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.arquillian.graphene.screenshooter.ftest.when;

import org.arquillian.extension.recorder.When;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.screenshooter.ftest.when.util.AbstractTestClass;
import org.jboss.arquillian.graphene.screenshooter.ftest.when.util.ArquillianXmlUtil;
import org.jboss.arquillian.graphene.screenshooter.ftest.when.util.DefaultTestClass;
import org.jboss.arquillian.graphene.screenshooter.ftest.when.util.ValidationUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Test if the {@code takeOnEveryAction} property works correctly and if the associate screenshots are (not) taken it the
 * right moments
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class OnEveryActionScreenshotTestCase extends AbstractScreenshotTestCase {

    /**
     * Should take only the "BEFORE" screenshot
     * TODO: modify this test when the it will be clear whe the screenshots should be taken
     */
    @Test
    public void onEveryActionDefaultTest() {
        ArquillianXmlUtil.setProperties(When.ON_EVERY_ACTION);
        runTest(DefaultTestClass.class);
        ValidationUtil.verifyScreenshotPresence(DefaultTestClass.class, When.BEFORE);
    }

    /**
     * TODO: modify this test when the it will be clear whe the screenshots should be taken
     */
    @Test
    @Ignore
    public void onEveryActionTest() {
        ArquillianXmlUtil.setProperties(When.ON_EVERY_ACTION);
        runTest(OnEveryActionTestClass.class);
        ValidationUtil.verifyScreenshotPresence(OnEveryActionTestClass.class, When.BEFORE);
    }

    /**
     * TODO: modify this test when the it will be clear whe the screenshots should be taken
     */
    @Test
    @Ignore
    public void onEveryActionFailingTest() {
        ArquillianXmlUtil.setProperties(When.ON_EVERY_ACTION);
        runFailingTest(FailingTestClass.class);
        ValidationUtil.verifyScreenshotPresence(FailingTestClass.class, When.BEFORE);
    }


    public static class OnEveryActionTestClass extends AbstractTestClass {

        @FindBy(id = "username")
        private WebElement usernameField;

        @FindBy(id = "password")
        private WebElement passwordField;

        @FindBy(id = "login-button")
        private WebElement loginButton;

        @Override
        public void testMethod() {
            loadPage();

            // getBrowser().findElement(By.id("username")).sendKeys("cool");

            Graphene.waitAjax().until().element(usernameField).is().visible();
            usernameField.sendKeys("cool");
            passwordField.sendKeys("cool");
            Graphene.guardAjax(loginButton).click();
        }
    }

    public static class FailingTestClass extends AbstractTestClass {

        @Override public void testMethod() {
            loadPage();
            Assert.fail();
        }
    }
}
