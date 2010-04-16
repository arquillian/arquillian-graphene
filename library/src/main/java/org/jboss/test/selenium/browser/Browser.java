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
import static org.jboss.test.selenium.utils.text.LocatorFormat.format;

public class Browser {
    BrowserMode browserMode;
    File executable;
    Pattern pattern = Pattern.compile("^\\*([^\\s]+)(?:$|\\s+(.*))$");

    public Browser(String stringRepresentation) {
        Matcher matcher = pattern.matcher(stringRepresentation);
        if (!matcher.find()) {
            throw new IllegalArgumentException(format(
                "given browser's stringRepresentation '{0}' doesn't match pattern", stringRepresentation));
        }

        browserMode = BrowserMode.parseMode(matcher.group(1));

        if (matcher.group(2) != null) {
            executable = new File(matcher.group(2));
        }
    }

    public Browser(BrowserMode browserMode) {
        this.browserMode = browserMode;
    }

    public Browser(BrowserMode browserMode, File executableFile) {
        this.browserMode = browserMode;
        this.executable = executableFile;
    }
    
    public BrowserMode getMode() {
        return this.browserMode;
    }
    
    public BrowserType getType() {
        return this.browserMode.getBrowserType();
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(browserMode.toString());
        if (executable != null) {
            builder.append("").append(executable.toString());
        }
        return builder.toString();
    }
}
