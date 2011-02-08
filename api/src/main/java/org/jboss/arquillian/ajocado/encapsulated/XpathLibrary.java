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
package org.jboss.arquillian.ajocado.encapsulated;

/**
 * Encapsulates the current implementations of XPath libraries supported by Selenium.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class XpathLibrary {

    /** Default library, currently Google's library, see {@link #AJAXSLT} */
    public static final XpathLibrary DEFAULT = new XpathLibrary("default");

    /** Google's library */
    public static final XpathLibrary AJAXSLT = new XpathLibrary("ajaxslt");

    /** Cybozu Labs' faster library */
    public static final XpathLibrary JAVASCRIPT_XPATH = new XpathLibrary("javascript-xpath");

    /** The xpath library name. */
    String xpathLibraryName;

    /**
     * Instantiates a new xpath library.
     * 
     * @param xpathLibraryName
     *            the xpath library name
     */
    public XpathLibrary(String xpathLibraryName) {
        this.xpathLibraryName = xpathLibraryName;
    }

    /**
     * Gets the xpath library name.
     * 
     * @return the xpath library name
     */
    public String getXpathLibraryName() {
        return xpathLibraryName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getXpathLibraryName();
    }

}
