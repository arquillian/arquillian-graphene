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
package org.jboss.ajocado.framework.internal;

import java.util.List;
import static org.apache.commons.lang.StringEscapeUtils.escapeJavaScript;

import org.jboss.ajocado.encapsulated.JavaScript;
import org.jboss.ajocado.framework.AjaxSelenium;
import org.jboss.ajocado.framework.AjaxSeleniumProxy;
import org.jboss.ajocado.framework.SeleniumExtensions;

import static org.jboss.ajocado.encapsulated.JavaScript.*;

/**
 * Defines the methods for loading the Selenium JS extensions to the Selenium Test Runner window.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class SeleniumExtensionsImpl implements SeleniumExtensions {

    /**
     * The associated AjaxSelenium object
     */
    AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();

    /*
     * JavaScript definitions for this object
     */
    final JavaScript getScriptWithResourceName = fromResource("javascript/get-script-with-resourcename.js");
    final JavaScript containsScriptWithResourceName = getScriptWithResourceName.append(" != null");
    final JavaScript getIdForScriptWithResourceName = getScriptWithResourceName.append(".getAttribute('id')");
    final JavaScript setResourceNameForId = js("document.getElementById('{0}').setAttribute('resourceName', '{1}')");
    final JavaScript removeScript = js("selenium.doRemoveScript('{0}')");

    /**
     * <p>
     * Loads the JavaScript extension by it's resourceName.
     * </p>
     * 
     * <p>
     * If the JavaScript with given resourceName are already loaded, it will not be loaded again.
     * </p>
     * 
     * <p>
     * If the JavaScript is already loaded but it's source has another checksum, it will be reloaded.
     * </p>
     * 
     * @param resourceName
     *            the full path to resource
     */
    public void requireResource(String resourceName) {
        if (!containsScript(resourceName)) {
            loadScript(resourceName);
        } else {
            refreshScript(resourceName);
        }
    }

    private boolean containsScript(String resourceName) {
        return Boolean.valueOf(selenium.getEval(containsScriptWithResourceName
            .parametrize(escapeJavaScript(resourceName))));
    }

    private void loadScript(String resourceName) {
        JavaScript extension = fromResource(resourceName);
        String identification = extension.getIdentification();
        String escapedResourceName = escapeJavaScript(resourceName);
        selenium.addScript(extension);
        selenium.getEval(setResourceNameForId.parametrize(identification, escapedResourceName));
    }

    private void refreshScript(String resourceName) {
        JavaScript extension = fromResource(resourceName);
        String identification = extension.getIdentification();
        String escapedResourceName = escapeJavaScript(resourceName);

        String scriptId = selenium.getEval(getIdForScriptWithResourceName.parametrize(escapedResourceName));

        if (!scriptId.equals(identification)) {
            System.out.println("# Reloading extension: " + resourceName);
            selenium.getEval(removeScript.parametrize(scriptId));
            loadScript(resourceName);
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

    /**
     * The SeleniumExtensions specifies new custom handlers, but the registration in commandFactory are triggered before
     * the loading of extensions. That is reason why we must explicitly register it before the test after each start of
     * selenium.
     */
    public void registerCustomHandlers() {
        final JavaScript registerCustomHandlers = js("currentTest.commandFactory.registerAll(selenium)");
        selenium.getEval(registerCustomHandlers);
    }
}
