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
package org.jboss.test.selenium.browser;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;

/**
 * <p>
 * Encapsulates execution properties of selected browser.
 * </p>
 * 
 * <p>
 * Consists from browser mode (incl. it's brand type) and associated executable file
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 * @see {@link #Browser(String)}
 */
public class Browser {

    /** The group in {@link #pattern} for associated executable. */
    private static final int GROUP_EXECUTABLE = 2;

    /** The group in {@link #pattern} for browser's mode. */
    private static final int GROUP_MODE = 1;

    /** The browser mode. */
    BrowserMode browserMode;

    /** The associated executable for given browser instance. */
    File executable;

    /**
     * Pattern for parsing browserMode and executable file from 
     * @see #Browser(String)
     */
    Pattern pattern = Pattern.compile("^\\*([^\\s]+)(?:$|\\s+(.*))$");

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
        Matcher matcher = pattern.matcher(stringRepresentation);
        if (!matcher.find()) {
            throw new IllegalArgumentException(format(
                "given browser's stringRepresentation '{0}' doesn't match pattern", stringRepresentation));
        }

        browserMode = BrowserMode.parseMode(matcher.group(GROUP_MODE));

        if (matcher.group(GROUP_EXECUTABLE) != null) {
            executable = new File(matcher.group(GROUP_EXECUTABLE));
        }
    }

    /**
     * Instantiates a new browser by given browserMode
     *
     * @param browserMode the browser mode
     */
    public Browser(BrowserMode browserMode) {
        this.browserMode = browserMode;
    }

    /**
     * Instantiates a new browser by given browserMode and executableFile.
     *
     * @param browserMode the browser mode
     * @param executableFile the executable file
     */
    public Browser(BrowserMode browserMode, File executableFile) {
        this.browserMode = browserMode;
        this.executable = executableFile;
    }
    
    /**
     * Gets the browser's mode (see {@link Browser}).
     *
     * @return the mode
     */
    public BrowserMode getMode() {
        return this.browserMode;
    }
    
    /**
     * <p>Gets the type of browser.</p>
     * 
     * <p>Shortcut for {@link BrowserMode#getBrowserType()}.</p>
     *
     * @return the type
     */
    public BrowserType getType() {
        return this.browserMode.getBrowserType();
    }
    
    /**
     * <p>Gets a string representation of browser.</p>
     * 
     * @see Browser#Browser(String)
     * @return the string representation of browser
     */
    public String getAsString() {
        StringBuilder builder = new StringBuilder(browserMode.getMode());
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
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE).append("mode", browserMode).append("type",
            browserMode.getBrowserType()).toString();
    }
}
