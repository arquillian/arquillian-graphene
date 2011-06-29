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
package org.jboss.arquillian.ajocado.browser;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable;

/**
 * <p>
 * Encapsulates execution properties of selected browser.
 * </p>
 * 
 * <p>
 * Consists from browser mode (including its type) and associated executable file
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class Browser implements SeleniumRepresentable {

    /**
     * Pattern for parsing browserMode and executable file from
     * 
     * @see #Browser(String)
     */
    private static final Pattern PATTERN = Pattern.compile("^\\*([^\\s]+)(?:$|\\s+(.*))$");

    /** The group in {@link #PATTERN} for browser's mode. */
    private static final int GROUP_MODE = 1;

    /** The group in {@link #PATTERN} for associated executable. */
    private static final int GROUP_EXECUTABLE = 2;

    /** The browser mode. */
    private BrowserMode browserMode;

    /** The associated executable for given browser instance. */
    private File executable;

    /**
     * <p>
     * Instantiates a new browser from string representation defined by Selenium framework
     * </p>
     * 
     * <p>
     * e.g. "*pifirefox /some/path/firefox-bin"
     * </p>
     * 
     * @param stringRepresentation
     *            the string representation
     */
    public Browser(String stringRepresentation) {
        Matcher matcher = PATTERN.matcher(stringRepresentation);
        if (!matcher.find()) {
            throw new IllegalArgumentException("given browser's stringRepresentation '" + stringRepresentation
                + "' doesn't match pattern");
        }

        browserMode = BrowserMode.parseMode(matcher.group(GROUP_MODE));

        if (matcher.group(GROUP_EXECUTABLE) != null) {
            executable = new File(matcher.group(GROUP_EXECUTABLE));
        }
    }

    /**
     * Instantiates a new browser by given browserMode
     * 
     * @param browserMode
     *            the browser mode
     */
    public Browser(BrowserMode browserMode) {
        this.browserMode = browserMode;
    }

    /**
     * Instantiates a new browser by given browserMode and executableFile.
     * 
     * @param browserMode
     *            the browser mode
     * @param executableFile
     *            the executable file
     */
    public Browser(BrowserMode browserMode, File executableFile) {
        this.browserMode = browserMode;
        this.executable = executableFile;
    }

    /**
     * Gets the browser's mode (see {@link BrowserMode}).
     * 
     * @return the mode
     */
    public BrowserMode getMode() {
        return browserMode;
    }

    /**
     * <p>
     * Gets the type of browser.
     * </p>
     * 
     * <p>
     * Shortcut for {@link BrowserMode#getType()}.
     * </p>
     * 
     * @return the type
     */
    public BrowserType getType() {
        if (browserMode == null) {
            return null;
        }
        return browserMode.getType();
    }

    /**
     * <p>
     * Gets a string representation of browser.
     * </p>
     * 
     * @see Browser#Browser(String)
     * @return the string representation of browser
     */
    @Override
    public String inSeleniumRepresentation() {
        StringBuilder builder = new StringBuilder(browserMode.inSeleniumRepresentation());
        if (executable != null) {
            builder.append(" ").append(executable.toString());
        }
        return builder.toString();
    }

    /**
     * Returns a string representation of the object in human-readable format.
     * 
     * @return the human-readable string representation of this object
     */
    @Override
    public String toString() {
        return "Browser [browserMode=" + browserMode + ", executable=" + executable + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((browserMode == null) ? 0 : browserMode.hashCode());
        result = prime * result + ((executable == null) ? 0 : executable.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Browser other = (Browser) obj;
        if (browserMode != other.browserMode) {
            return false;
        }
        if (executable == null) {
            if (other.executable != null) {
                return false;
            }
        } else if (!executable.equals(other.executable)) {
            return false;
        }
        return true;
    }
}
