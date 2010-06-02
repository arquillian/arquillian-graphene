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
package org.jboss.test.selenium.utils.text;

import static org.apache.commons.lang.StringEscapeUtils.*;

/**
 * <p>Formats using simplified MessageFormat syntax:</p>
 * 
 * <p>In opposite to MessageFormat, it isn't necessary doubling single quotes (').</p>
 *
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class SimplifiedFormat {
    private SimplifiedFormat() {
    }

    /**
     * Uses a MessageFormat.format() to prepare given format string and use it to format result with given arguments.
     * 
     * @param format
     *            string used in MessageFormat.format()
     * @param args
     *            used to formatting given format string
     * @return string formatted using given arguments
     */
    public static String format(String message, Object... args) {
        String result = message;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i].toString();
            arg = escapeJava(arg);
            result = result.replaceFirst("\\{\\}", arg);
            result = result.replaceAll("\\{" + i + "\\}", arg);
        }
        return result;
    }
}
