/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.ajocado.framework.internal;

import static org.jboss.arquillian.ajocado.javascript.JavaScript.js;

import java.util.List;

import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.framework.PageExtensions;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.waiting.Wait;

/**
 * Defines methods for installing JavaScript page extension to the target page.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class PageExtensionsImpl implements PageExtensions {

    /** The JavaScript for extending of the page. */
    JavaScript pageExtensions;

    /** Evaluates if the body is loaded */
    final JavaScript isBodyLoaded = js("(selenium.browserbot.getCurrentWindow() != null) "
        + " && (selenium.browserbot.getCurrentWindow().document != null) "
        + " && (selenium.browserbot.getCurrentWindow().document.body != null)");

    /** Evalutes if the Graphene object is undefined on the page */
    final JavaScript isGraphenePageUndefined = js("Graphene === undefined || Graphene.getPage() === undefined");

    /** The associated selenium object. */
    GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();

    /**
     * Install.
     */
    @Override
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
    @Override
    public boolean isInstalled() {
        return !Boolean.valueOf(selenium.getEval(isGraphenePageUndefined));
    }

    /**
     * Install the page extensions
     */
    void installPageExtension() {
        selenium.runScript(pageExtensions);
    }

    /**
     * Wait for body to be loaded.
     */
    void waitForBodyLoaded() {
        selenium.waitForCondition(isBodyLoaded, Wait.DEFAULT_TIMEOUT);
    }

    /**
     * Loads the page JS extensions from resources defined by list of resource names.
     *
     * @param resourceNames
     *            the list of full paths to resources
     */
    @Override
    public void loadFromResources(List<String> resourceNames) {
        JavaScript extensions = null;
        for (String resourceName : resourceNames) {
            JavaScript partial = JavaScript.fromResource(resourceName);
            extensions = (extensions == null) ? partial : extensions.join(partial);
        }
        this.pageExtensions = extensions;
    }
}
