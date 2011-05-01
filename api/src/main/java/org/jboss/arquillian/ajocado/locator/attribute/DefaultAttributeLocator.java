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
package org.jboss.arquillian.ajocado.locator.attribute;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.ajocado.locator.AbstractLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocationStrategy;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;

/**
 * Default implementation of locator for element's attributes.
 * 
 * @param <E>
 *            the type of associated element locator, which can be obtained from this attribute locator
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class DefaultAttributeLocator<E extends ElementLocator<E>> extends AbstractLocator<AttributeLocator<E>>
    implements AttributeLocator<E> {

    /** The underlying elementLocator. */
    ElementLocator<E> elementLocator;

    /** The attribute. */
    Attribute attribute;

    /**
     * Instantiates a attribute locator using given elementLocator and the specific attribute.
     * 
     * @param elementLocator
     *            the element locator
     * @param attribute
     *            the attribute
     */
    public DefaultAttributeLocator(ElementLocator<E> elementLocator, Attribute attribute) {
        super("not-used");
        if (attribute == null) {
            throw new IllegalArgumentException("attribute can't be null");
        }
        this.elementLocator = elementLocator;
        this.attribute = attribute;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.Locator#getRawLocator()
     */
    public String getRawLocator() {
        return SimplifiedFormat.format("{0}@{1}", elementLocator.getRawLocator(), attribute.getAttributeName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.AttributeLocator#getLocationStrategy()
     */
    public ElementLocationStrategy getLocationStrategy() {
        return elementLocator.getLocationStrategy();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.locator.AttributeLocator#getAssociatedElement()
     */
    public ElementLocator<E> getAssociatedElement() {
        return elementLocator;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    @Override
    public AttributeLocator<E> format(Object... args) {
        ElementLocator<E> derivedElementLocator = elementLocator.format(args);
        return new DefaultAttributeLocator<E>(derivedElementLocator, attribute);
    }
}
