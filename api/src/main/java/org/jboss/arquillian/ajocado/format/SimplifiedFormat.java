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
package org.jboss.arquillian.ajocado.format;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Formats using simplified MessageFormat syntax: {} are used as placeholders in order of arguments; {number} are
 * placeholders with given argument number.
 * </p>
 * 
 * <p>
 * When filling the placeholders by given arguments are compelete, rest of the numbered placeholders are decreased by
 * number of arguments in previous format action.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class SimplifiedFormat {

    private static final int MAXIMUM = 64;
    private static final int SIXTEEN = 16;

    private static final Pattern PATTERN = Pattern.compile("\\{([0-9]+)\\}");

    private SimplifiedFormat() {
    }

    /**
     * Parametrize given string with arguments, using {} or {number} (e.g. {0}, {1}, ...) as placeholders.
     * 
     * @param message
     *            message to format
     * @param args
     *            used to formatting given format string
     * @return string formatted using given arguments
     */
    public static String format(String message, Object... args) {
        String result = message;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i].toString();
            arg = increaseByDelta(arg, args.length);
            arg = replace(arg, "{}", "{-1}");

            result = replaceOnce(result, "{}", arg);
            result = replace(result, "{" + i + "}", arg);
        }

        result = decreaseByDelta(result, args.length);
        result = replace(result, "{-1}", "{}");

        return result;
    }

    private static String decreaseByDelta(String message, int delta) {
        return increaseByDelta(message, 0 - delta);
    }

    private static String increaseByDelta(String message, int delta) {
        String result = message;
        Matcher matcher = PATTERN.matcher(result);
        List<Integer> found = new LinkedList<Integer>();
        while (matcher.find()) {
            int value = Integer.valueOf(matcher.group(1));
            found.add(value);
        }

        Collections.sort(found);

        for (int value : found) {
            int replacement = value + delta;
            result = replaceOnce(result, "{" + value + "}", "{" + replacement + "}");
        }

        return result;
    }

    private static String replace(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, -1);
    }

    private static String replaceOnce(String text, String searchString, String replacement) {
        return replace(text, searchString, replacement, 1);
    }

    private static String replace(String text, String searchString, String replacement, int max) {
        int m = max;
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = increase < 0 ? 0 : increase;
        increase *= m < 0 ? SIXTEEN : (m > MAXIMUM ? MAXIMUM : m);
        StringBuffer buf = new StringBuffer(text.length() + increase);
        while (end != -1) {
            buf.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            if (--m == 0) {
                break;
            }
            end = text.indexOf(searchString, start);
        }
        buf.append(text.substring(start));
        return buf.toString();
    }
}
