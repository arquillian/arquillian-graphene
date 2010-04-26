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
package org.jboss.test.selenium.locator;

/**
 * Attribute of page element.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class Attribute {

    /** The STYLE attribute. */
    public static final Attribute STYLE = new Attribute("style");

    /** The CLASS attribute. */
    public static final Attribute CLASS = new Attribute("class");

    /** The SRC attribute. */
    public static final Attribute SRC = new Attribute("src");

    /** The HREF attribute. */
    public static final Attribute HREF = new Attribute("href");

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
