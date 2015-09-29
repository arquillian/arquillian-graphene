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
