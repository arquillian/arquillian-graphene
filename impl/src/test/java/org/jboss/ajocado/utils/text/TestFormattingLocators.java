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
package org.jboss.ajocado.utils.text;

import org.jboss.ajocado.locator.Attribute;
import org.jboss.ajocado.locator.AttributeLocator;
import org.jboss.ajocado.locator.JQueryLocator;
import org.testng.annotations.Test;

import static org.jboss.ajocado.locator.LocatorFactory.*;
import static org.testng.Assert.*;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TestFormattingLocators {

    @Test
    public void testBackReference() {
        assertEquals(jq("input[id$=myId]").getAsString(), "jquery=input[id$=myId]");
    }
    
    @Test
    public void testSimpleFormatting() {
        JQueryLocator locator = jq("x{0}z");
        locator = locator.format("y");
        assertEquals(locator.getAsString(), "jquery=xyz");
    }
    
    @Test
    public void testDoubleFormatting() {
        JQueryLocator locator = jq("a{0}c{1}e");
        locator = locator.format("b");
        locator = locator.format("d");
        assertEquals(locator.getAsString(), "jquery=abcde");
    }
    
    @Test
    public void testComplexFormatting() {
        JQueryLocator locator = jq("a{1}b{3}c{}{4}d{}e{}f");
        locator = locator.format(0, 1);
        locator = locator.format(2, 3);
        locator = locator.format(4);
        assertEquals(locator.getAsString(), "jquery=a1b3c04d1e2f");
    }
    
    @Test
    public void testAttributeFormatting() {
        JQueryLocator locator = jq("x{0}z");
        AttributeLocator<?> attributeLocator = locator.getAttribute(Attribute.CLASS);
        attributeLocator = attributeLocator.format("y");
        assertEquals(attributeLocator.getAttribute(), Attribute.CLASS);
        assertEquals(attributeLocator.getAssociatedElement().getAsString(), "jquery=xyz");
        assertEquals(attributeLocator.getAsString(), "jquery=xyz@class");
    }
}
