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
package org.jboss.test.selenium.framework;

import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.waiting.Wait;

/**
 * Defines methods for installing JavaScript page extension to the target page.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class JavaScriptPageExtension {

    /** The JavaScript for extending of the page. */
    final JavaScript pageExtension = JavaScript.fromResource("page-extensions.js");

    /** Evaluates if the body is loaded */
    final JavaScript isBodyLoaded = new JavaScript("(selenium.browserbot.getCurrentWindow() != null) "
        + " && (selenium.browserbot.getCurrentWindow().document != null) "
        + " && (selenium.browserbot.getCurrentWindow().document.body != null)");

    /** Evalutes if the RichFacesSelenium object is undefined on the page */
    final JavaScript isRFSUndefined = new JavaScript("getRFS() === undefined");

    /** The associated selenium object. */
    AjaxSelenium selenium;

    /**
     * Instantiates a new page extensions and associate it with given selenium object.
     * 
     * @param selenium
     *            the selenium object what we want to associate extension to
     */
    public JavaScriptPageExtension(AjaxSelenium selenium) {
        this.selenium = selenium;
    }

    /**
     * Install.
     */
    public void install() {
        if (!isInstalled()) {
            waitForBodyLoaded();
            installPageExtension();
        }
    }

    /**
     * Checks if RichFacesSelenium is already installed
     * 
     * @return true, if is installed
     */
    public boolean isInstalled() {
        return !Boolean.valueOf(selenium.getEval(isRFSUndefined));
    }

    /**
     * Install the page extensions
     */
    void installPageExtension() {
        selenium.runScript(pageExtension);
    }

    /**
     * Wait for body to be loaded.
     */
    void waitForBodyLoaded() {
        selenium.waitForCondition(isBodyLoaded, Wait.DEFAULT_TIMEOUT);
    }
}
