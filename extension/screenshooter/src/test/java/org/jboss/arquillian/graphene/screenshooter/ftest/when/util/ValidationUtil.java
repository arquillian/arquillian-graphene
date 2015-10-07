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
package org.jboss.arquillian.graphene.screenshooter.ftest.when.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.arquillian.extension.recorder.When;
import org.junit.Assert;

/**
 * Util class used for checking screenshots for their presence there in the {@link Constants#SCREENSHOTS_DIRECTORY}
 * directory
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ValidationUtil {

    public static void verifyScreenshotPresence(Class testClass, When... whenArray) {
        verifyScreenshotPresence(testClass, Constants.TEST_METHOD_NAME, whenArray);
    }

    public static void verifyScreenshotPresence(Class testClass, List<String> namesOfAllFiles, When... whenArray) {
        verifyScreenshotPresence(testClass, Constants.TEST_METHOD_NAME, namesOfAllFiles, whenArray);
    }

    public static void verifyScreenshotPresence(Class testClass, String methodName, When... whenArray) {
        verifyScreenshotPresence(testClass, methodName, new ArrayList<String>(whenArray.length), whenArray);
    }

    public static void verifyScreenshotPresence(Class testClass, String methodName, List<String> namesOfAllFiles,
        When... whenArray) {
        String screenshotTestDirectory =
            Constants.SCREENSHOTS_DIRECTORY + testClass.getName() + File.separator + methodName + File.separator;

        File[] screenshotsFiles = new File(screenshotTestDirectory).listFiles();
        if (screenshotsFiles == null) {
            screenshotsFiles = new File[] {};
        }

        for (When when : whenArray) {
            namesOfAllFiles.add(when.toString());
        }
        checkFilesCount(namesOfAllFiles, screenshotsFiles);

        for (File screenshotFile : screenshotsFiles) {
            if (namesOfAllFiles.contains(screenshotFile.getName().replace(".png", ""))) {
                Assert.assertTrue("The size of the screenshot " + screenshotFile.getAbsolutePath() + " should not be 0",
                    screenshotFile.length() > 0);

            } else {
                StringBuffer message = new StringBuffer("The content of screenshot directory is not as expected. ");
                failMessageOfWrongContent(message, namesOfAllFiles, screenshotsFiles);
            }
        }
    }

    private static void checkFilesCount(List<String> namesOfAllFiles, File[] screenshotsFiles) {
        if (namesOfAllFiles.size() != screenshotsFiles.length) {
            StringBuffer message = new StringBuffer("The count of expected files doesn't correspond to the reality. ");
            message.append("Expected count: " + namesOfAllFiles.size() + " actual: " + screenshotsFiles.length);
            failMessageOfWrongContent(message, namesOfAllFiles, screenshotsFiles);
        }
    }

    private static void failMessageOfWrongContent(StringBuffer message, List<String> namesOfAllFiles,
        File[] screenshotsFiles) {
        message.append("\nSpecifically, the expected file name(s):\n");

        for (String name : namesOfAllFiles) {
            message.append(name + ".png\n");
        }
        message.append("but in the directory there are(is):\n");
        for (File f : screenshotsFiles) {
            message.append(f.getName() + "\n");
        }
        Assert.fail(message.toString());
    }
}