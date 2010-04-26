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
package org.jboss.test.selenium.locator.type;

/**
 * Strategy for locating element on the page.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface LocationStrategy {

    /** Strategy for locating by CSS selectors. */
    LocationStrategy CSS = new CssStrategy();

    /** Strategy for locating by given JavaScript expression. */
    LocationStrategy DOM = new DomStrategy();

    /** Strategy for locating elements by given id or by name as a fallback */
    LocationStrategy IDENTIFIER = new IdentifierStrategy();

    /** Strategy for locating elements by given id attribute. */
    LocationStrategy ID = new IdStrategy();

    /** Strategy for locating elements using JQuery Selector syntax. */
    LocationStrategy JQUERY = new JQueryStrategy();

    /** Strategy for locating elements by text of the link (anchor) */
    LocationStrategy LINK = new LinkStrategy();

    /** Strategy for locating elements by given name attribute. */
    LocationStrategy NAME = new NameStrategy();

    /** Strategy for locating elements by given xpath xpression. */
    LocationStrategy XPATH = new XpathStrategy();

    /**
     * <p>
     * Gets the strategy name used to express location strategy in Selenium API.
     * </p>
     * 
     * <p>
     * E.g. <tt>&lt;strategyName&gt;=&lt;elementLocator&gt;</tt>
     * </p>
     * 
     * @return the strategy name
     */
    String getStrategyName();
}