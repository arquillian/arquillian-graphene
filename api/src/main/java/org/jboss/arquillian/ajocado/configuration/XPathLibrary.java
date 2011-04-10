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
package org.jboss.arquillian.ajocado.configuration;

import org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable;

/**
 * Encapsulates the current implementations of XPath libraries supported by Selenium.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class XPathLibrary implements SeleniumRepresentable {

    /** Default library, currently Google's library, see {@link #AJAXSLT} */
    public static final XPathLibrary DEFAULT = new XPathLibrary("default");

    /** Google's library */
    public static final XPathLibrary AJAXSLT = new XPathLibrary("ajaxslt");

    /** Cybozu Labs' faster library */
    public static final XPathLibrary JAVASCRIPT_XPATH = new XPathLibrary("javascript-xpath");

    /** The xpath library name. */
    private String xpathLibraryName;

    /**
     * New named XPath library
     * 
     * @param xpathLibraryName
     *            the XPath library name
     */
    public XPathLibrary(String xpathLibraryName) {
        this.xpathLibraryName = xpathLibraryName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable#inSeleniumRepresentation()
     */
    public String inSeleniumRepresentation() {
        return xpathLibraryName;
    }

    @Override
    public String toString() {
        return xpathLibraryName;
    }
}
