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
package org.jboss.test.selenium.listener;

import org.jboss.test.selenium.utils.testng.TestLoggingUtils;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

/**
 * This class is used as ITestListener in testNG tests to put test's status to the console output
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>, <a href="mailto:pjha@redhat.com">Prabhat Jha</a>, <a
 *         href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 * 
 */
public class ConsoleStatusTestListener extends TestListenerAdapter {

    @Override
    public void onTestStart(ITestResult result) {
        logStatus(result);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        logStatus(result);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        logStatus(result);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        logStatus(result);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        logStatus(result);
    }

    /**
     * This method will output method name and status on the standard output
     * 
     * @param result
     *            from the fine-grained listener's method such as onTestFailure(ITestResult)
     */
    private void logStatus(ITestResult result) {
        String message = getMessage(result); 
        System.out.println(message);
        if (result.getStatus() != ITestResult.STARTED) {
            System.out.println();
        }
    }
    
    protected String getMessage(ITestResult result) {
        return TestLoggingUtils.getTestDescription(result);
    }
}
