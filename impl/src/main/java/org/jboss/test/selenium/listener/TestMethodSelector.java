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
package org.jboss.test.selenium.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

/**
 * <p>
 * Uses system property "method" to determine, if the test method should be run or not.
 * </p>
 * 
 * <p>
 * Disables the such test methods, which doesn't match the given method name. The '*' wildcard can be used to filter
 * methods.
 * </p>
 * 
 * <p>
 * Must be registered in TestNG listeners to filter the methods.
 * <p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestMethodSelector implements IAnnotationTransformer {

    /**
     * Disables the test methods which doesn't match the given method name.
     */
    @SuppressWarnings("rawtypes")
    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        String selectedMethod = System.getProperty("method", "*");

        selectedMethod = StringUtils.replace(selectedMethod, "*", ".*");

        if (!testMethod.getName().matches(selectedMethod)) {
            annotation.setEnabled(false);
        }
    }
}
