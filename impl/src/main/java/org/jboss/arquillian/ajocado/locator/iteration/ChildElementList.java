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
package org.jboss.arquillian.ajocado.locator.iteration;

import org.jboss.arquillian.ajocado.locator.IterableLocator;

/**
 * <p>
 * Able to iterate over direct childs of given locator.
 * </p>
 * 
 * <p>
 * In opposite to {@link ElementOcurrenceList}, it returns only direct descendants of given expression.
 * </p>
 * 
 * @param <T>
 *            the IterableLocator implementation
 * @see AbstractElementList
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class ChildElementList<T extends IterableLocator<T>> extends AbstractElementList<T> {

    /**
     * Instantiates a new child element list.
     *
     * @param iterableLocator the iterable locator
     */
    public ChildElementList(T iterableLocator) {
        super(iterableLocator);
    }

    /* (non-Javadoc)
     * @see org.jboss.arquillian.ajocado.locator.iteration.AbstractElementList#abstractNthElement(int)
     */
    @Override
    T abstractNthElement(int index) {
        return (T) iterableLocator.getNthChildElement(index);
    }
}
