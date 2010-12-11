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
package org.jboss.ajocado.css;

import org.apache.commons.lang.Validate;

/**
 * <p>
 * Encapsulates extendable enumeration CSS properties.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class CssProperty {

    public static final CssProperty BACKGROUND = new CssProperty("background");
    public static final CssProperty BACKGROUND_ATTACHMENT = new CssProperty("background-attachment");
    public static final CssProperty BACKGROUND_COLOR = new CssProperty("background-color");
    public static final CssProperty BACKGROUND_IMAGE = new CssProperty("background-image");
    public static final CssProperty BACKGROUND_POSITION = new CssProperty("background-position");
    public static final CssProperty BACKGROUND_REPEAT = new CssProperty("background-repeat");
    public static final CssProperty BORDER = new CssProperty("border");
    public static final CssProperty BORDER_BOTTOM = new CssProperty("border-bottom");
    public static final CssProperty BORDER_BOTTOM_WIDTH = new CssProperty("border-bottom-width");
    public static final CssProperty BORDER_COLOR = new CssProperty("border-color");
    public static final CssProperty BORDER_LEFT = new CssProperty("border-left");
    public static final CssProperty BORDER_LEFT_WIDTH = new CssProperty("border-left-width");
    public static final CssProperty BORDER_RIGHT = new CssProperty("border-right");
    public static final CssProperty BORDER_RIGHT_WIDTH = new CssProperty("border-right-width");
    public static final CssProperty BORDER_STYLE = new CssProperty("border-style");
    public static final CssProperty BORDER_TOP = new CssProperty("border-top");
    public static final CssProperty BORDER_TOP_WIDTH = new CssProperty("border-top-width");
    public static final CssProperty BORDER_WIDTH = new CssProperty("border-width");
    public static final CssProperty CLEAR = new CssProperty("clear");
    public static final CssProperty COLOR = new CssProperty("color");
    public static final CssProperty DISPLAY = new CssProperty("display");
    public static final CssProperty FLOAT = new CssProperty("float");
    public static final CssProperty FONT = new CssProperty("font");
    public static final CssProperty FONT_FAMILY = new CssProperty("font-family");
    public static final CssProperty FONT_SIZE = new CssProperty("font-size");
    public static final CssProperty FONT_STYLE = new CssProperty("font-style");
    public static final CssProperty FONT_VARIANT = new CssProperty("font-variant");
    public static final CssProperty FONT_WEIGHT = new CssProperty("font-weight");
    public static final CssProperty HEIGHT = new CssProperty("height");
    public static final CssProperty LETTER_SPACING = new CssProperty("letter-spacing");
    public static final CssProperty LINE_HEIGHT = new CssProperty("line-height");
    public static final CssProperty LIST_STYLE = new CssProperty("list-style");
    public static final CssProperty LIST_STYLE_IMAGE = new CssProperty("list-style-image");
    public static final CssProperty LIST_STYLE_POSITION = new CssProperty("list-style-position");
    public static final CssProperty LIST_STYLE_TYPE = new CssProperty("list-style-type");
    public static final CssProperty MARGIN = new CssProperty("margin");
    public static final CssProperty MARGIN_BOTTOM = new CssProperty("margin-bottom");
    public static final CssProperty MARGIN_LEFT = new CssProperty("margin-left");
    public static final CssProperty MARGIN_RIGHT = new CssProperty("margin-right");
    public static final CssProperty MARGIN_TOP = new CssProperty("margin-top");
    public static final CssProperty PADDING = new CssProperty("padding");
    public static final CssProperty PADDING_BOTTOM = new CssProperty("padding-bottom");
    public static final CssProperty PADDING_LEFT = new CssProperty("padding-left");
    public static final CssProperty PADDING_RIGHT = new CssProperty("padding-right");
    public static final CssProperty PADDING_TOP = new CssProperty("padding-top");
    public static final CssProperty TEXT_ALIGN = new CssProperty("text-align");
    public static final CssProperty TEXT_DECORATION = new CssProperty("text-decoration");
    public static final CssProperty TEXT_INDENT = new CssProperty("text-indent");
    public static final CssProperty TEXT_TRANSFORM = new CssProperty("text-transform");
    public static final CssProperty VERTICAL_ALIGN = new CssProperty("vertical-align");
    public static final CssProperty WORD_SPACING = new CssProperty("word-spacing");
    public static final CssProperty WHITE_SPACE = new CssProperty("white-space");
    public static final CssProperty WIDTH = new CssProperty("width");

    static final CssProperty BORDER_SPACING = new CssProperty("border-spacing");

    private String propertyName;

    public CssProperty(String propertyName) {
        Validate.notNull(propertyName);
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
