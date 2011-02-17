/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2009-2010, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/
package org.jboss.arquillian.ajocado.testng.listener;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jboss.arquillian.ajocado.encapsulated.NetworkTrafficType;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumProxy;
import org.jboss.arquillian.ajocado.framework.SystemPropertiesConfiguration;
import org.jboss.arquillian.ajocado.utils.testng.TestInfo;
import org.jboss.arquillian.ajocado.utils.testng.TestLoggingUtils;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import com.thoughtworks.selenium.SeleniumException;

/**
 * Test listener which provides the methods injected in lifecycle of test case to catch the additional information in
 * context of test failure.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class FailureLoggingTestListener extends TestListenerAdapter {

    protected File mavenProjectBuildDirectory = new SystemPropertiesConfiguration().getMavenProjectBuildDirectory();
    protected File failuresOutputDir = new File(mavenProjectBuildDirectory, "failures");

    private AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();

    @Override
    public void onStart(ITestContext testContext) {
        try {
            FileUtils.forceMkdir(failuresOutputDir);
            FileUtils.cleanDirectory(failuresOutputDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onConfigurationFailure(ITestResult result) {
        onFailure(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        onFailure(result);
    }

    protected void onFailure(ITestResult result) {
        if (!selenium.isStarted()) {
            return;
        }

        Throwable throwable = result.getThrowable();
        String stacktrace = null;

        if (throwable != null) {
            stacktrace = ExceptionUtils.getStackTrace(throwable);
        }

        String filenameIdentification = getFilenameIdentification(result);
        // String seleniumLogIdentification = getSeleniumLogIdentification(result);

        // File seleniumLogFile = new File(mavenProjectBuildDirectory, "selenium/selenium-server.log");
        // List<String> methodLog = new ArrayList<String>();
        // try {
        // @SuppressWarnings("unchecked")
        // List<String> seleniumLog = FileUtils.readLines(seleniumLogFile);
        //
        // boolean started = false;
        // for (String line : seleniumLog) {
        // if (line.contains(seleniumLogIdentification)) {
        // started = true;
        // methodLog = new ArrayList<String>();
        // }
        // if (started) {
        // methodLog.add(line);
        // }
        // }
        // } catch (IOException e) {
        // throw new RuntimeException(e);
        // }

        String traffic;
        try {
            traffic = selenium.captureNetworkTraffic(NetworkTrafficType.PLAIN).getTraffic();
        } catch (SeleniumException e) {
            traffic = ExceptionUtils.getFullStackTrace(e);
        }

        BufferedImage screenshot = selenium.captureEntirePageScreenshot();

        String htmlSource = selenium.getHtmlSource();

        File stacktraceOutputFile = new File(failuresOutputDir, filenameIdentification + "/stacktrace.txt");
        File imageOutputFile = new File(failuresOutputDir, filenameIdentification + "/screenshot.png");
        File trafficOutputFile = new File(failuresOutputDir, filenameIdentification + "/network-traffic.txt");
        // File logOutputFile = new File(failuresOutputDir, filenameIdentification + "/selenium-log.txt");
        File htmlSourceOutputFile = new File(failuresOutputDir, filenameIdentification + "/html-source.html");

        try {
            File directory = imageOutputFile.getParentFile();
            FileUtils.forceMkdir(directory);

            FileUtils.writeStringToFile(stacktraceOutputFile, stacktrace);
            ImageIO.write(screenshot, "PNG", imageOutputFile);
            FileUtils.writeStringToFile(trafficOutputFile, traffic);
            // FileUtils.writeLines(logOutputFile, methodLog);
            FileUtils.writeStringToFile(htmlSourceOutputFile, htmlSource);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected String getSeleniumLogIdentification(ITestResult result) {
        final String failure = TestInfo.STATUSES.get(result.getStatus()).toUpperCase();
        final String started = TestInfo.STATUSES.get(ITestResult.STARTED).toUpperCase();
        String testDescription = TestLoggingUtils.getTestDescription(result);
        testDescription = testDescription.replaceFirst(failure, started);
        testDescription = testDescription.replaceFirst("\\[[^\\]]+\\] ", "");
        return testDescription;
    }

    protected String getFilenameIdentification(ITestResult result) {
        return TestInfo.getClassMethodName(result);
    }
}
