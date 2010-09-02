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

import static org.jboss.test.selenium.utils.testng.TestInfo.STATUSES;
import static org.jboss.test.selenium.utils.testng.TestInfo.getPackageClassMethodName;

import java.util.Date;

import org.testng.ITestResult;

/**
 * Provides the method for obtaining test description from ITestResult.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class TestLoggingUtils {

    private TestLoggingUtils() {
    }

    /**
     * Obtains detailed test description from ITestResult.
     * 
     * @param result
     *            the ITestResult object
     * @return the detailed test description
     */
    public static String getTestDescription(ITestResult result) {
        final String methodName = getPackageClassMethodName(result);
        final String status = STATUSES.get(result.getStatus());

        // parameters
        StringBuilder parameters = new StringBuilder("(");
        if (result.getParameters() != null && result.getParameters().length != 0) {
            for (int i = 0; i < result.getParameters().length; i++) {
                parameters.append("\"");
                parameters.append(result.getParameters()[i]);
                parameters.append(i == result.getParameters().length - 1 ? "\"" : "\", ");
            }
        }
        parameters.append(")");

        // result
        String message =
            String.format("[%tT] %s: %s%s", new Date(), status.toUpperCase(), methodName, parameters.toString());
        return message;
    }
}
