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
package org.jboss.arquillian.ajocado.log;

import org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable;

/**
 * Logging level of Selenium command processor.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class LogLevel implements SeleniumRepresentable {

    /** DEBUG log level */
    public static final LogLevel DEBUG = new LogLevel("debug");

    /** INFO log level */
    public static final LogLevel INFO = new LogLevel("info");

    /** WARN log level */
    public static final LogLevel WARN = new LogLevel("warn");

    /** ERROR log level */
    public static final LogLevel ERROR = new LogLevel("error");

    /** Logging OFF */
    public static final LogLevel OFF = new LogLevel("off");

    /** The log level name. */
    String logLevelName;

    /**
     * Instantiates a new log level.
     * 
     * @param logLevelName
     *            the log level name
     */
    public LogLevel(String logLevelName) {
        this.logLevelName = logLevelName;
    }

    /**
     * Gets the log level name.
     * 
     * @return the log level name
     */
    public String getLogLevelName() {
        return logLevelName;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable#inSeleniumRepresentation()
     */
    @Override
    public String inSeleniumRepresentation() {
        return getLogLevelName();
    }

}
