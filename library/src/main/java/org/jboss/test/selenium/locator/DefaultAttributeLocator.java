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

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.locator.type.LocationStrategy;
import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;

/**
 * Default implementation of locator for element's attributes.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class DefaultAttributeLocator extends AbstractLocator implements AttributeLocator {

    /** The underlying elementLocator. */
    ElementLocator elementLocator;
    
    /** The attribute. */
    Attribute attribute;
    
    /**
     * Instantiates a attribute locator using given elementLocator and the specific attribute.
     *
     * @param elementLocator the element locator
     * @param attribute the attribute
     */
    public DefaultAttributeLocator(ElementLocator elementLocator, Attribute attribute) {
    	super("not-used");
        Validate.notNull(attribute);
        this.elementLocator = elementLocator;
        this.attribute = attribute;
    }
    
    /*
     * (non-Javadoc)
     * @see org.jboss.test.selenium.locator.Locator#getRawLocator()
     */
    public String getRawLocator() {
    	return format("{0}@{1}", elementLocator.getRawLocator(), attribute.getAttributeName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.locator.AttributeLocator#getLocationStrategy()
     */
    public LocationStrategy getLocationStrategy() {
        return elementLocator.getLocationStrategy();
    }

    
    /* (non-Javadoc)
     * @see org.jboss.test.selenium.locator.AttributeLocator#getAssociatedElement()
     */
    public ElementLocator getAssociatedElement() {
        return elementLocator;
    }

    public Attribute getAttribute() {
        return attribute;
    }
}
