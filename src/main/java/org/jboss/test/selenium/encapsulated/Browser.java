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
package org.jboss.test.selenium.encapsulated;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static org.jboss.test.selenium.utils.text.LocatorFormat.format;

public class Browser {
    Type browserType;
    File file;
    Pattern pattern = Pattern.compile("^\\*([^\\s]+)(?:$|\\s+(.*))$");

    public Browser(String stringRepresentation) {
        Matcher matcher = pattern.matcher(stringRepresentation);
        if (!matcher.find()) {
            throw new IllegalArgumentException(format(
                "given browser's stringRepresentation '{0}' doesn't match pattern", stringRepresentation));
        }

        browserType = new Type(matcher.group(1));

        if (matcher.group(2) != null) {
            file = new File(matcher.group(2));
        }
    }

    public Browser(Type browserType) {
        this.browserType = browserType;
    }

    public Browser(Type browserType, File file) {
        this.browserType = browserType;
        this.file = file;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(browserType.toString());
        if (file != null) {
            builder.append("").append(file.toString());
        }
        return builder.toString();
    }

    public static class Type {
        public final static Type FIREFOX = new Type("firefox");
        public final static Type IE = new Type("iexplorer");

        private String type;

        public Type(String type) {
            this.type = type;
        }

        public String toString() {
            return "*" + type;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((type == null) ? 0 : type.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            Type other = (Type) obj;
            if (type == null) {
                if (other.type != null)
                    return false;
            } else if (!type.equals(other.type))
                return false;
            return true;
        }
    }
}
