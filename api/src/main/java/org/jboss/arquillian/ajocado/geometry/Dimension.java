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
package org.jboss.arquillian.ajocado.geometry;

import org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable;

/**
 * Dimensions of object rendered by browser.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class Dimension implements SeleniumRepresentable {
    private final int width;
    private final int height;

    /**
     * Creates new dimension with given width and height
     * 
     * @param width
     * @param height
     */
    public Dimension(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Returns the width
     * 
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns the height
     * 
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Converts dimension to offset
     * 
     * @return the dimension converted to offset
     */
    public Offset toOffset() {
        return new Offset(width, height);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable#inSeleniumRepresentation()
     */
    public String inSeleniumRepresentation() {
        return width + "," + height;
    }
}
