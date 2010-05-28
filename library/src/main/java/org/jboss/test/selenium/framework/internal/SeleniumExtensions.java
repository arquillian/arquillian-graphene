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
package org.jboss.test.selenium.framework.internal;

import java.util.List;

import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.framework.AjaxSelenium;

/**
 * Defines the methods for loading the Selenium JS extensions to the Selenium Test Runner window.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class SeleniumExtensions {

    /**
     * The associated AjaxSelenium object
     */
    AjaxSelenium selenium;

    /**
     * Construct the {@link SeleniumExtensions} object with associated selenium object.
     * 
     * @param selenium
     *            the associated selenium object
     */
    public SeleniumExtensions(AjaxSelenium selenium) {
        this.selenium = selenium;
    }

    /**
     * Adds the JavaScript extension by it's resourceName
     * 
     * @param resourceName
     *            the full path to resource
     */
    public void requireResource(String resourceName) {
        JavaScript extension = JavaScript.fromResource(resourceName);
        if (!selenium.containsScript(extension)) {
            selenium.addScript(extension);
        }
    }

    /**
     * Adds the JavaScript extensions by defining list of resource names.
     * 
     * @param resourceNames
     *            the list of full paths to resources
     */
    public void requireResources(List<String> resourceNames) {
        for (String resourceName : resourceNames) {
            requireResource(resourceName);
        }
    }
}
