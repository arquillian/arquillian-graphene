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

import static org.jboss.test.selenium.listener.SeleniumLoggingTestListener.STATUSES;
import static org.jboss.test.selenium.listener.SeleniumLoggingTestListener.getMethodName;

import java.util.Date;

import org.testng.IObjectFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.internal.ObjectFactoryImpl;

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
     * @param ctx
     *            test context
     */
    private void logStatus(ITestResult result) {
        final String methodName = getMethodName(result);
        final String status = STATUSES.get(result.getStatus());

        StringBuilder parameters = new StringBuilder("(");
        if (result.getParameters() != null && result.getParameters().length != 0) {
            parameters.append("\"");
            parameters.append(result.getParameters()[0]);
            parameters.append("\"");
        }
        parameters.append(")");
        
        String message = String.format("[%tT] %s: %s%s", new Date(), status.toUpperCase(), methodName, parameters
            .toString());
        System.out.println(message);
    }
}
