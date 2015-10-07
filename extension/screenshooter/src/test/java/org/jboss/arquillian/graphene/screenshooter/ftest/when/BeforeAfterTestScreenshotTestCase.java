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
