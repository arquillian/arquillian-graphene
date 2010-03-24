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

public class LocatorFactory {

    public static CssLocator css(String cssSelector) {
        return new CssLocator(cssSelector);
    }

    public static DomLocator dom(String javascriptExpression) {
        return new DomLocator(javascriptExpression);
    }

    public static IdLocator id(String id) {
        return new IdLocator(id);
    }

    public static LinkLocator link(String linkText) {
        return new LinkLocator(linkText);
    }

    public static JQueryLocator jq(String jquerySelector) {
        return new JQueryLocator(jquerySelector);
    }

    public static NameLocator name(String name) {
        return new NameLocator(name);
    }

    public static XpathLocator xp(String xpath) {
        return new XpathLocator(xpath);
    }
}
