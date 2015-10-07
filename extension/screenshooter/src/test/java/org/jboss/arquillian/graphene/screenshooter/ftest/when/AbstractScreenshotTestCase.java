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

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.jboss.arquillian.graphene.screenshooter.ftest.when.util.Constants;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.runner.JUnitCore;
import org.junit.runner.notification.Failure;

/**
 * An abstract class for other test cases - it just clears the {@link Constants#SCREENSHOTS_DIRECTORY} directory
 * before every test method.
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public abstract class AbstractScreenshotTestCase {

    @Before
    public void cleanScreenshotDirectory() throws IOException {
        Assume.assumeFalse("htmlUnit".equals(System.getProperty("browser")));

        System.setProperty("arquillian.xml", Constants.PATH_TO_ARQ_XML);
        FileUtils.deleteDirectory(new File(Constants.SCREENSHOTS_DIRECTORY));
    }

    protected void runFailingTest(Class testClass) {
        List<Failure> failures = JUnitCore.runClasses(testClass).getFailures();
        Assert.assertFalse("The test should have failed but it didn't!", failures.isEmpty());
    }

    protected void runTest(Class testClass) {
        List<Failure> failures = JUnitCore.runClasses(testClass).getFailures();
        StringBuffer message = new StringBuffer("The test should have NOT failed but it did!");

        if (!failures.isEmpty()) {
            message.append("The stacktrace:\n" + failures.get(0).getTrace());
        }
        Assert.assertTrue(message.toString(), failures.isEmpty());
    }
}
