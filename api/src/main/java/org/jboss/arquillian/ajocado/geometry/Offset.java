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
package org.jboss.arquillian.ajocado.geometry;

import org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable;

/**
 * Offset for movement on browser-rendered canvas.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class Offset implements SeleniumRepresentable {

    private int x;
    private int y;

    /**
     * Creates the offset with specified x and y movements
     * 
     * @param x
     *            the x movement
     * @param y
     *            the y movement
     */
    public Offset(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x movement
     * 
     * @return the x movement
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y movement
     * 
     * @return the y movement
     */
    public int getY() {
        return y;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable#inSeleniumRepresentation()
     */
    public String inSeleniumRepresentation() {
        return x + "," + y;
    }
}
