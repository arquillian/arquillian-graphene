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
import org.jboss.arquillian.graphene.screenshooter.ftest.when.util.ValidationUtil;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test if the {@code takeWhenTestFailed} property works correcty and if the associate screenshot is (not) taken it the
 * right moments
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class AfterTestFailScreenshotTestCase extends AbstractScreenshotTestCase {

    /**
     * Should take the "failed" screenshot
     */
    @Test
    public void testFail() {
        ArquillianXmlUtil.setProperties(When.FAILED);

        runFailingTest(FailingTestClass.class);
        verify(FailingTestClass.class);
    }

    /**
     * Should take the "failed" screenshot based on the annotation settings
     */
    @Test
    public void testFailWithAnnotation() {
        ArquillianXmlUtil.setProperties();

        runFailingTest(FailingWithAnnotationTestClass.class);
        verify(FailingWithAnnotationTestClass.class);
    }

    /**
     * Should NOT take the "failed" screenshot based on the annotation settings
     */
    @Test
    public void testFailDisablingAnnotation() {
        ArquillianXmlUtil.setProperties(When.FAILED);

        runFailingTest(FailingDisablingAnnotationTestClass.class);
        verify(FailingDisablingAnnotationTestClass.class);
        ValidationUtil.verifyScreenshotPresence(FailingDisablingAnnotationTestClass.class, "withoutScreenshot");
    }

    /**
     * Should take the "failed" screenshot based on the default annotation settings
     */
    @Test
    public void testFailDefaultAnnotation() {
        ArquillianXmlUtil.setProperties();

        runFailingTest(FailingDefaultAnnotationTestClass.class);
        verify(FailingDefaultAnnotationTestClass.class);
    }

    private void verify(Class testClass) {
        ValidationUtil.verifyScreenshotPresence(testClass, When.FAILED);
    }

    public static class FailingTestClass extends AbstractTestClass {

        @Override public void testMethod() {
            loadPage();
            Assert.fail();
        }
    }

    public static class FailingWithAnnotationTestClass extends AbstractTestClass {

        @Test
        @Screenshot(takeWhenTestFailed = true)
        @Override
        public void testMethod() {
            loadPage();
            Assert.fail();
        }
    }

    public static class FailingDisablingAnnotationTestClass extends AbstractTestClass {

        @Override
        public void testMethod() {
            loadPage();
            Assert.fail();
        }

        @Test
        @Screenshot(takeWhenTestFailed = false)
        public void withoutScreenshot() {
            loadPage();
            Assert.fail();
        }
    }

    public static class FailingDefaultAnnotationTestClass extends AbstractTestClass {

        @Test
        @Screenshot
        @Override
        public void testMethod() {
            loadPage();
            Assert.fail();
        }
    }
}
