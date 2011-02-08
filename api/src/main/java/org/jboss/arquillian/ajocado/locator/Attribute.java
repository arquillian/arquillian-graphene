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
package org.jboss.arquillian.ajocado.locator;

/**
 * <p>
 * Attribute of page element.
 * </p>
 * 
 * <p>
 * Contains almost complete enumeration of attributes
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class Attribute {

    /** The Attribute SIZE. */
    public static final Attribute SIZE = new Attribute("size");

    /** The Attribute COLOR. */
    public static final Attribute COLOR = new Attribute("color");

    /** The Attribute CLEAR. */
    public static final Attribute CLEAR = new Attribute("clear");

    /** The Attribute BACKGROUND. */
    public static final Attribute BACKGROUND = new Attribute("background");

    /** The Attribute BGCOLOR. */
    public static final Attribute BGCOLOR = new Attribute("bgcolor");

    /** The Attribute TEXT. */
    public static final Attribute TEXT = new Attribute("text");

    /** The Attribute LINK. */
    public static final Attribute LINK = new Attribute("link");

    /** The Attribute VLINK. */
    public static final Attribute VLINK = new Attribute("vlink");

    /** The Attribute ALINK. */
    public static final Attribute ALINK = new Attribute("alink");

    /** The Attribute WIDTH. */
    public static final Attribute WIDTH = new Attribute("width");

    /** The Attribute HEIGHT. */
    public static final Attribute HEIGHT = new Attribute("height");

    /** The Attribute ALIGN. */
    public static final Attribute ALIGN = new Attribute("align");

    /** The Attribute NAME. */
    public static final Attribute NAME = new Attribute("name");

    /** The Attribute HREF. */
    public static final Attribute HREF = new Attribute("href");

    /** The Attribute REL. */
    public static final Attribute REL = new Attribute("rel");

    /** The Attribute REV. */
    public static final Attribute REV = new Attribute("rev");

    /** The Attribute TITLE. */
    public static final Attribute TITLE = new Attribute("title");

    /** The Attribute TARGET. */
    public static final Attribute TARGET = new Attribute("target");

    /** The Attribute SHAPE. */
    public static final Attribute SHAPE = new Attribute("shape");

    /** The Attribute COORDS. */
    public static final Attribute COORDS = new Attribute("coords");

    /** The Attribute ISMAP. */
    public static final Attribute ISMAP = new Attribute("ismap");

    /** The Attribute NOHREF. */
    public static final Attribute NOHREF = new Attribute("nohref");

    /** The Attribute ALT. */
    public static final Attribute ALT = new Attribute("alt");

    /** The Attribute ID. */
    public static final Attribute ID = new Attribute("id");

    /** The Attribute SRC. */
    public static final Attribute SRC = new Attribute("src");

    /** The Attribute HSPACE. */
    public static final Attribute HSPACE = new Attribute("hspace");

    /** The Attribute VSPACE. */
    public static final Attribute VSPACE = new Attribute("vspace");

    /** The Attribute USEMAP. */
    public static final Attribute USEMAP = new Attribute("usemap");

    /** The Attribute LOWSRC. */
    public static final Attribute LOWSRC = new Attribute("lowsrc");

    /** The Attribute CODEBASE. */
    public static final Attribute CODEBASE = new Attribute("codebase");

    /** The Attribute CODE. */
    public static final Attribute CODE = new Attribute("code");

    /** The Attribute ARCHIVE. */
    public static final Attribute ARCHIVE = new Attribute("archive");

    /** The Attribute VALUE. */
    public static final Attribute VALUE = new Attribute("value");

    /** The Attribute VALUETYPE. */
    public static final Attribute VALUETYPE = new Attribute("valuetype");

    /** The Attribute TYPE. */
    public static final Attribute TYPE = new Attribute("type");

    /** The Attribute CLASS. */
    public static final Attribute CLASS = new Attribute("class");

    /** The Attribute STYLE. */
    public static final Attribute STYLE = new Attribute("style");

    /** The Attribute LANG. */
    public static final Attribute LANG = new Attribute("lang");

    /** The Attribute FACE. */
    public static final Attribute FACE = new Attribute("face");

    /** The Attribute DIR. */
    public static final Attribute DIR = new Attribute("dir");

    /** The Attribute DECLARE. */
    public static final Attribute DECLARE = new Attribute("declare");

    /** The Attribute CLASSID. */
    public static final Attribute CLASSID = new Attribute("classid");

    /** The Attribute DATA. */
    public static final Attribute DATA = new Attribute("data");

    /** The Attribute CODETYPE. */
    public static final Attribute CODETYPE = new Attribute("codetype");

    /** The Attribute STANDBY. */
    public static final Attribute STANDBY = new Attribute("standby");

    /** The Attribute BORDER. */
    public static final Attribute BORDER = new Attribute("border");

    /** The Attribute SHAPES. */
    public static final Attribute SHAPES = new Attribute("shapes");

    /** The Attribute NOSHADE. */
    public static final Attribute NOSHADE = new Attribute("noshade");

    /** The Attribute COMPACT. */
    public static final Attribute COMPACT = new Attribute("compact");

    /** The Attribute START. */
    public static final Attribute START = new Attribute("start");

    /** The Attribute ACTION. */
    public static final Attribute ACTION = new Attribute("action");

    /** The Attribute METHOD. */
    public static final Attribute METHOD = new Attribute("method");

    /** The Attribute ENCTYPE. */
    public static final Attribute ENCTYPE = new Attribute("enctype");

    /** The Attribute CHECKED. */
    public static final Attribute CHECKED = new Attribute("checked");

    /** The Attribute MAXLENGTH. */
    public static final Attribute MAXLENGTH = new Attribute("maxlength");

    /** The Attribute MULTIPLE. */
    public static final Attribute MULTIPLE = new Attribute("multiple");

    /** The Attribute SELECTED. */
    public static final Attribute SELECTED = new Attribute("selected");

    /** The Attribute ROWS. */
    public static final Attribute ROWS = new Attribute("rows");

    /** The Attribute COLS. */
    public static final Attribute COLS = new Attribute("cols");

    /** The Attribute DUMMY. */
    public static final Attribute DUMMY = new Attribute("dummy");

    /** The Attribute CELLSPACING. */
    public static final Attribute CELLSPACING = new Attribute("cellspacing");

    /** The Attribute CELLPADDING. */
    public static final Attribute CELLPADDING = new Attribute("cellpadding");

    /** The Attribute VALIGN. */
    public static final Attribute VALIGN = new Attribute("valign");

    /** The Attribute HALIGN. */
    public static final Attribute HALIGN = new Attribute("halign");

    /** The Attribute NOWRAP. */
    public static final Attribute NOWRAP = new Attribute("nowrap");

    /** The Attribute ROWSPAN. */
    public static final Attribute ROWSPAN = new Attribute("rowspan");

    /** The Attribute COLSPAN. */
    public static final Attribute COLSPAN = new Attribute("colspan");

    /** The Attribute PROMPT. */
    public static final Attribute PROMPT = new Attribute("prompt");

    /** The Attribute HTTPEQUIV. */
    public static final Attribute HTTPEQUIV = new Attribute("http-equiv");

    /** The Attribute CONTENT. */
    public static final Attribute CONTENT = new Attribute("content");

    /** The Attribute LANGUAGE. */
    public static final Attribute LANGUAGE = new Attribute("language");

    /** The Attribute VERSION. */
    public static final Attribute VERSION = new Attribute("version");

    /** The Attribute N. */
    public static final Attribute N = new Attribute("n");

    /** The Attribute FRAMEBORDER. */
    public static final Attribute FRAMEBORDER = new Attribute("frameborder");

    /** The Attribute MARGINWIDTH. */
    public static final Attribute MARGINWIDTH = new Attribute("marginwidth");

    /** The Attribute MARGINHEIGHT. */
    public static final Attribute MARGINHEIGHT = new Attribute("marginheight");

    /** The Attribute SCROLLING. */
    public static final Attribute SCROLLING = new Attribute("scrolling");

    /** The Attribute NORESIZE. */
    public static final Attribute NORESIZE = new Attribute("noresize");

    /** The Attribute ENDTAG. */
    public static final Attribute ENDTAG = new Attribute("endtag");

    /** The Attribute COMMENT. */
    public static final Attribute COMMENT = new Attribute("comment");

    /** The attribute name. */
    private String attributeName;

    /**
     * Instantiates a new attribute from it's name.
     * 
     * @param attributeName
     *            the attribute name
     */
    public Attribute(String attributeName) {
        super();
        this.attributeName = attributeName;
    }

    /**
     * Gets the attribute name.
     * 
     * @return the attribute name
     */
    public String getAttributeName() {
        return attributeName;
    }

}
