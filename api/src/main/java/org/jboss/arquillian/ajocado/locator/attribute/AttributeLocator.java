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
package org.jboss.arquillian.ajocado.locator.attribute;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.locator.Locator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;

/**
 * Specific locator for attributes.
 * 
 * @param <E>
 *            the type of associated element locator, which can be obtained from this attribute locator
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface AttributeLocator<E extends ElementLocator<E>> extends Locator<AttributeLocator<E>> {
    /**
     * Returns the underlying element what associates attribute given by this locator to
     * 
     * @return the underlying element what associates attribute given by this locator to
     */
    ElementLocator<E> getAssociatedElement();

    /**
     * Returns the type of attribute which this locator points to.
     * 
     * @return the type of attribute which this locator points to.
     */
    Attribute getAttribute();

    public AttributeLocator<E> format(Object... args);
}