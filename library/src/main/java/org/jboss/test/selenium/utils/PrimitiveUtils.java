package org.jboss.test.selenium.utils;

public final class PrimitiveUtils {

    private PrimitiveUtils() {
    }

    public static long asLong(String string) {
        return Long.valueOf(string);
    }
    
    public static int asInt(String string) {
        return Integer.valueOf(string);
    }
    
    public static boolean asBoolean(String string) {
        return Boolean.valueOf(string);
    }
}
