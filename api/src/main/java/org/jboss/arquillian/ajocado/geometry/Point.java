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
 * Point as position rendered on browser canvas.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class Point implements SeleniumRepresentable {
    private int x;
    private int y;

    /**
     * Constructs new point with specified x and y positions
     *
     * @param x
     * @param y
     */
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns x position
     *
     * @return x position
     */
    public int getX() {
        return x;
    }

    /**
     * Returns y position
     *
     * @return y position
     */
    public int getY() {
        return y;
    }

    /**
     * Subtracts the given point from this point
     *
     * @param point
     *            to substract
     * @return the point with this point substract of given point
     */
    public Point substract(Point point) {
        return new Point(this.x - point.x, this.y - point.y);
    }

    /**
     * Adds the given point from this point
     *
     * @param point
     *            to add
     * @return the point with this point add of given point
     */
    public Point add(Point point) {
        return new Point(this.x + point.x, this.y + point.y);
    }

    /**
     * Subtracts the given offset from this point
     *
     * @param offset
     *            to subtract
     * @return the point with this point subtract of given offset
     */
    public Point substract(Offset offset) {
        return new Point(this.x - offset.getX(), this.y - offset.getY());
    }

    /**
     * Adds the given offset from this point
     *
     * @param offset
     *            to add
     * @return the point with this point add of given offset
     */
    public Point add(Offset offset) {
        return new Point(this.x + offset.getX(), this.y + offset.getY());
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable#inSeleniumRepresentation()
     */
    @Override
    public String inSeleniumRepresentation() {
        return x + "," + y;
    }
}
