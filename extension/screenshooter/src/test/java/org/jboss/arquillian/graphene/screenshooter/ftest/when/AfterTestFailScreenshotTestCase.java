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
