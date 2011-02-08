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
package org.jboss.arquillian.ajocado.encapsulated;

/**
 * <p>
 * The Encapsulation of element locator.
 * </p>
 * 
 * <p>
 * Implements the relative addressing {@link FrameLocator#TOP} and {@link FrameLocator#PARENT} out-of-the-box.
 * 
 * <p>
 * You can also implement own frame locators, using 'dom=&lt;dom&gt;' locator or by indexing the frame using
 * 'index=&lt;index&gt;' (see {@link com.thoughtworks.selenium.Selenium.Selenium#selectFrame(String)}).
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class FrameLocator {

    /**
     * The relative frame addressing of top frame.
     */
    public static final FrameLocator TOP = new FrameLocator("relative=top");
    /**
     * The relative frame addressing of parent frame.
     */
    public static final FrameLocator PARENT = new FrameLocator("relative=parent");

    private String locator;

    public FrameLocator(String locator) {
        this.locator = locator;
    }

    public String getAsString() {
        return locator;
    }
}
