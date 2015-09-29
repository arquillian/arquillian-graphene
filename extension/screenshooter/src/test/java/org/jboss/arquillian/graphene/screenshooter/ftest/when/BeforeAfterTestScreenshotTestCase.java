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
import org.arquillian.extension.recorder.screenshooter.api.Screenshot;
import org.jboss.arquillian.graphene.screenshooter.ftest.when.util.AbstractTestClass;
import org.jboss.arquillian.graphene.screenshooter.ftest.when.util.ArquillianXmlUtil;
import org.jboss.arquillian.graphene.screenshooter.ftest.when.util.DefaultTestClass;
import org.jboss.arquillian.graphene.screenshooter.ftest.when.util.ValidationUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test if the {@code takeBeforeTest} and {@code takeAfterTest} properties work correctly and if the associate
 * screenshots are (not) taken it the right moments
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class BeforeAfterTestScreenshotTestCase extends AbstractScreenshotTestCase {

    /**
     * Should take only the "before" screenshot
     */
    @Test
    public void beforeTest() {
        runDefaultTestClass(When.BEFORE);
    }

    /**
     * Should take only the "after" screenshot
     */
    @Test
    public void afterTest() {
        runDefaultTestClass(When.AFTER);
    }

    /**
     * Should take only the both "before" and "after" screenshot
     */
    @Test
    public void beforeAfterTest() {
        runDefaultTestClass(When.BEFORE, When.AFTER);
    }

    /**
     * Should take the only "before" or only "after" screenshot or only both of them based on the annotation settings
     */
    @Test
    public void beforeAfterWithAnnotationTest() {
        ArquillianXmlUtil.setProperties();
        runTest(BeforeAfterWithAnnotationsTestClass.class);
        ValidationUtil.verifyScreenshotPresence(BeforeAfterWithAnnotationsTestClass.class, When.BEFORE, When.AFTER);
        ValidationUtil
            .verifyScreenshotPresence(BeforeAfterWithAnnotationsTestClass.class, "testOnlyBefore", When.BEFORE);
        ValidationUtil.verifyScreenshotPresence(BeforeAfterWithAnnotationsTestClass.class, "testOnlyAfter", When.AFTER);
    }

    /**
     * Should take neither the "before" nor the "after" screenshot based on the default annotation settings
     */
    @Test
    public void beforeAfterDefaultAnnotationTest() {
        ArquillianXmlUtil.setProperties(When.BEFORE, When.AFTER);
        runTest(BeforeAfterDefaultAnnotationTestClass.class);
        ValidationUtil.verifyScreenshotPresence(BeforeAfterDefaultAnnotationTestClass.class);
    }

    /**
     * Should take neither the "before" nor the "after" screenshot based on the default annotation settings even when
     * this test fails
     */
    @Test
    public void beforeAfterFailingTest() {
        ArquillianXmlUtil.setProperties(When.BEFORE, When.AFTER);
        runFailingTest(BeforeAfterFailingTestClass.class);
        ValidationUtil.verifyScreenshotPresence(BeforeAfterFailingTestClass.class, When.BEFORE, When.FAILED);
        ValidationUtil
            .verifyScreenshotPresence(BeforeAfterFailingTestClass.class, "testWithAnnotation", When.BEFORE, When.FAILED);
    }

    private void runDefaultTestClass(When... whenArray) {
        ArquillianXmlUtil.setProperties(whenArray);
        runTest(DefaultTestClass.class);
        ValidationUtil.verifyScreenshotPresence(DefaultTestClass.class, whenArray);
    }

    public static class BeforeAfterWithAnnotationsTestClass extends AbstractTestClass {

        @Test
        @Screenshot(takeBeforeTest = true, takeAfterTest = true)
        @Override
        public void testMethod() {
            loadPage();
        }

        @Test
        @Screenshot(takeBeforeTest = true)
        public void testOnlyBefore() {
            loadPage();
        }

        @Test
        @Screenshot(takeAfterTest = true)
        public void testOnlyAfter() {
            loadPage();
        }
    }

    public static class BeforeAfterDefaultAnnotationTestClass extends AbstractTestClass {

        @Test
        @Screenshot
        @Override
        public void testMethod() {
            loadPage();
        }
    }

    public static class BeforeAfterFailingTestClass extends AbstractTestClass {

        @Test
        @Override
        public void testMethod() {
            loadPage();
            Assert.fail();
        }

        @Test
        @Screenshot(takeBeforeTest = true, takeAfterTest = true, takeWhenTestFailed = false)
        public void testWithAnnotation() {
            loadPage();
            Assert.fail();
        }
    }
}
