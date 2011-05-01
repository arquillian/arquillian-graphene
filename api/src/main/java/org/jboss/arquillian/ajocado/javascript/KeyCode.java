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
package org.jboss.arquillian.ajocado.javascript;

import org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable;

/**
 * <p>
 * Encapsulates key codes as they are interpreted by JavaScript.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class KeyCode implements SeleniumRepresentable {

    public static final KeyCode BACKSPACE = new KeyCode("8");
    public static final KeyCode TAB = new KeyCode("9");
    public static final KeyCode ENTER = new KeyCode("13");
    public static final KeyCode SHIFT = new KeyCode("16");
    public static final KeyCode CTRL = new KeyCode("17");
    public static final KeyCode ALT = new KeyCode("18");
    public static final KeyCode PAUSE_BREAK = new KeyCode("19");
    public static final KeyCode CAPS_LOCK = new KeyCode("20");
    public static final KeyCode ESCAPE = new KeyCode("27");
    public static final KeyCode PAGE_UP = new KeyCode("33");
    public static final KeyCode PAGE_DOWN = new KeyCode("34");
    public static final KeyCode END = new KeyCode("35");
    public static final KeyCode HOME = new KeyCode("36");
    public static final KeyCode LEFT_ARROW = new KeyCode("37");
    public static final KeyCode UP_ARROW = new KeyCode("38");
    public static final KeyCode RIGHT_ARROW = new KeyCode("39");
    public static final KeyCode DOWN_ARROW = new KeyCode("40");
    public static final KeyCode INSERT = new KeyCode("45");
    public static final KeyCode DELETE = new KeyCode("46");
    public static final KeyCode NUM_0 = new KeyCode("48");
    public static final KeyCode NUM_1 = new KeyCode("49");
    public static final KeyCode NUM_2 = new KeyCode("50");
    public static final KeyCode NUM_3 = new KeyCode("51");
    public static final KeyCode NUM_4 = new KeyCode("52");
    public static final KeyCode NUM_5 = new KeyCode("53");
    public static final KeyCode NUM_6 = new KeyCode("54");
    public static final KeyCode NUM_7 = new KeyCode("55");
    public static final KeyCode NUM_8 = new KeyCode("56");
    public static final KeyCode NUM_9 = new KeyCode("57");
    public static final KeyCode A = new KeyCode("65");
    public static final KeyCode B = new KeyCode("66");
    public static final KeyCode C = new KeyCode("67");
    public static final KeyCode D = new KeyCode("68");
    public static final KeyCode E = new KeyCode("69");
    public static final KeyCode F = new KeyCode("70");
    public static final KeyCode G = new KeyCode("71");
    public static final KeyCode H = new KeyCode("72");
    public static final KeyCode I = new KeyCode("73");
    public static final KeyCode J = new KeyCode("74");
    public static final KeyCode K = new KeyCode("75");
    public static final KeyCode L = new KeyCode("76");
    public static final KeyCode M = new KeyCode("77");
    public static final KeyCode N = new KeyCode("78");
    public static final KeyCode O = new KeyCode("79");
    public static final KeyCode P = new KeyCode("80");
    public static final KeyCode Q = new KeyCode("81");
    public static final KeyCode R = new KeyCode("82");
    public static final KeyCode S = new KeyCode("83");
    public static final KeyCode T = new KeyCode("84");
    public static final KeyCode U = new KeyCode("85");
    public static final KeyCode V = new KeyCode("86");
    public static final KeyCode W = new KeyCode("87");
    public static final KeyCode X = new KeyCode("88");
    public static final KeyCode Y = new KeyCode("89");
    public static final KeyCode Z = new KeyCode("90");
    public static final KeyCode LEFT_WINDOW_KEY = new KeyCode("91");
    public static final KeyCode RIGHT_WINDOW_KEY = new KeyCode("92");
    public static final KeyCode SELECT_KEY = new KeyCode("93");
    public static final KeyCode NUMPAD_0 = new KeyCode("96");
    public static final KeyCode NUMPAD_1 = new KeyCode("97");
    public static final KeyCode NUMPAD_2 = new KeyCode("98");
    public static final KeyCode NUMPAD_3 = new KeyCode("99");
    public static final KeyCode NUMPAD_4 = new KeyCode("100");
    public static final KeyCode NUMPAD_5 = new KeyCode("101");
    public static final KeyCode NUMPAD_6 = new KeyCode("102");
    public static final KeyCode NUMPAD_7 = new KeyCode("103");
    public static final KeyCode NUMPAD_8 = new KeyCode("104");
    public static final KeyCode NUMPAD_9 = new KeyCode("105");
    public static final KeyCode MULTIPLY = new KeyCode("106");
    public static final KeyCode ADD = new KeyCode("107");
    public static final KeyCode SUBTRACT = new KeyCode("109");
    public static final KeyCode DECIMAL_POINT = new KeyCode("110");
    public static final KeyCode DIVIDE = new KeyCode("111");
    public static final KeyCode F1 = new KeyCode("112");
    public static final KeyCode F2 = new KeyCode("113");
    public static final KeyCode F3 = new KeyCode("114");
    public static final KeyCode F4 = new KeyCode("115");
    public static final KeyCode F5 = new KeyCode("116");
    public static final KeyCode F6 = new KeyCode("117");
    public static final KeyCode F7 = new KeyCode("118");
    public static final KeyCode F8 = new KeyCode("119");
    public static final KeyCode F9 = new KeyCode("120");
    public static final KeyCode F10 = new KeyCode("121");
    public static final KeyCode F11 = new KeyCode("122");
    public static final KeyCode F12 = new KeyCode("123");
    public static final KeyCode NUM_LOCK = new KeyCode("144");
    public static final KeyCode SCROLL_LOCK = new KeyCode("145");
    public static final KeyCode SEMI_COLON = new KeyCode("186");
    public static final KeyCode EQUAL_SIGN = new KeyCode("187");
    public static final KeyCode COMMA = new KeyCode("188");
    public static final KeyCode DASH = new KeyCode("189");
    public static final KeyCode PERIOD = new KeyCode("190");
    public static final KeyCode FORWARD_SLASH = new KeyCode("191");
    public static final KeyCode GRAVE_ACCENT = new KeyCode("192");
    public static final KeyCode OPEN_BRACKET = new KeyCode("219");
    public static final KeyCode BACK_SLASH = new KeyCode("220");
    public static final KeyCode CLOSE_BRAKET = new KeyCode("221");
    public static final KeyCode SINGLE_QUOTE = new KeyCode("222");

    private final String code;

    /**
     * Constructs new key code by providing it's JavaScript key code representation
     * 
     * @param code
     */
    public KeyCode(String code) {
        this.code = code;
    }

    /**
     * Returns the code of JavaScript key code representation
     * 
     * @return the code of JavaScript key code representation
     */
    public String getCode() {
        return code;
    }

    public String inSeleniumRepresentation() {
        return "\\" + code;
    }
}
