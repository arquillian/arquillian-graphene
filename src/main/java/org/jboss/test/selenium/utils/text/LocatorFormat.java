package org.jboss.test.selenium.utils.text;

import java.text.MessageFormat;

public class LocatorFormat {
	/**
     * Uses a MessageFormat.format() to prepare given format string and use it
     * to format result with given arguments.
     * 
     * @param format
     *            string used in MessageFormat.format()
     * @param args
     *            used to formatting given format string
     * @return string formatted using given arguments
     */
    public static String format(String format, Object... args) {
        String message = preformat(format);
        return MessageFormat.format(message, args);
    }

    /**
     * Prepares a message to use in Message.format()
     * 
     * @param message
     *            prepared to use in Message.format()
     * @return message prepared to use in Message.format()
     */
    private static String preformat(String message) {
        return message.replace("'", "''").replace("\\''", "'");
    }
}
