/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.jboss.test.selenium.utils.testng;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import org.testng.ITestResult;

/**
 * Obtains informations about status of test, obtains method names from test result.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class TestInfo {

    /**
     * Mapping of the status ids to string equivalents
     */
    public static final Map<Integer, String> STATUSES = Collections.unmodifiableMap(new TreeMap<Integer, String>() {
        private static final long serialVersionUID = 1L;

        {
            put(ITestResult.FAILURE, "Failure");
            put(ITestResult.SKIP, "Skip");
            put(ITestResult.STARTED, "Started");
            put(ITestResult.SUCCESS, "Success");
            put(ITestResult.SUCCESS_PERCENTAGE_FAILURE, "FailurePercentage");
        }
    });

    private TestInfo() {
    }

    /**
     * Get package + class + method name from ITestResult
     * 
     * @param result
     *            from the fine-grained listener's method such as onTestFailure(ITestResult)
     * @return the package + class + method name in current context
     */
    public static String getPackageClassMethodName(ITestResult result) {
        return getContainingPackageName(result) + "." + getClassMethodName(result);
    }

    /**
     * Get class + method name from ITestResult
     * 
     * @param result
     *            from the fine-grained listener's method such as onTestFailure(ITestResult)
     * @return the class + method name in current context
     */
    public static String getClassMethodName(ITestResult result) {
        return getClassName(result) + "." + getMethodName(result);
    }

    /**
     * Get method name from ITestResult
     * 
     * @param result
     *            from the fine-grained listener's method such as onTestFailure(ITestResult)
     * @return the method name in current context
     */
    public static String getMethodName(ITestResult result) {
        Method method = result.getMethod().getMethod();
        return method.getName();
    }

    /**
     * Get class name from ITestResult
     * 
     * @param result
     *            from the fine-grained listener's method such as onTestFailure(ITestResult)
     * @return the class name in current context
     */
    public static String getClassName(ITestResult result) {
        Class<?> dClass = result.getMethod().getMethod().getDeclaringClass();
        return dClass.getSimpleName();
    }

    /**
     * Get last containing package name from ITestResult
     * 
     * @param result
     *            from the fine-grained listener's method such as onTestFailure(ITestResult)
     * @return the package name in current context
     */
    public static String getContainingPackageName(ITestResult result) {
        Package dPackage = result.getMethod().getMethod().getDeclaringClass().getPackage();
        return dPackage.getName().replaceFirst("^.*\\.", "");
    }
}
