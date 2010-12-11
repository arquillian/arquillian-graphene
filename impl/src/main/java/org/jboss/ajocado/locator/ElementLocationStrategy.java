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
package org.jboss.ajocado.locator;

/**
 * Strategy for locating element on the page.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class ElementLocationStrategy extends AbstractLocationStrategy {

    /** Strategy for locating by CSS selectors. */
    public static final ElementLocationStrategy CSS = new ElementLocationStrategy("css");

    /** Strategy for locating by given JavaScript expression. */
    public static final ElementLocationStrategy DOM = new ElementLocationStrategy("dom");

    /** Strategy for locating elements by given id or by name as a fallback */
    public static final ElementLocationStrategy IDENTIFIER = new ElementLocationStrategy("identifier");

    /** Strategy for locating elements by given id attribute. */
    public static final ElementLocationStrategy ID = new ElementLocationStrategy("id");

    /** Strategy for locating elements using JQuery Selector syntax. */
    public static final ElementLocationStrategy JQUERY = new ElementLocationStrategy("jquery");

    /** Strategy for locating elements by text of the link (anchor) */
    public static final ElementLocationStrategy LINK = new ElementLocationStrategy("link");

    /** Strategy for locating elements by given name attribute. */
    public static final ElementLocationStrategy NAME = new ElementLocationStrategy("name");

    /** Strategy for locating elements by given xpath xpression. */
    public static final ElementLocationStrategy XPATH = new ElementLocationStrategy("xpath");

    public ElementLocationStrategy(String strategyName) {
        super(strategyName);
    }

}