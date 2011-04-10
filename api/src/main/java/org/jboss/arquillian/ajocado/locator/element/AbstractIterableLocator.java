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
package org.jboss.arquillian.ajocado.locator.element;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;

/**
 * Abstract implementation of locator which can iterate over it's descendant.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 * @param <T>
 *            type what we want to iterate over - this type will be returned by method provided by this abtract class
 */
public abstract class AbstractIterableLocator<T extends IterableLocator<T>> extends AbstractElementLocator<T> implements
    IterableLocator<T> {

    private AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    /**
     * Constructs locator for given string representation
     * 
     * @param locator
     *            the string representation of locator
     */
    public AbstractIterableLocator(String locator) {
        super(locator);
    }

    @Override
    public Iterator<T> iterator() {
        return new ElementIterator();
    }

    @Override
    public int size() {
        return selenium.getCount(this);
    }

    /**
     * The iterator over elements given by IterableLocator.
     */
    public class ElementIterator implements Iterator<T> {

        /** The index of current element in iteration. */
        private int index;

        /** The cache for the count of elements found by given elementLocator. */
        private int count;

        /**
         * <p>
         * Instantiates a new element iterator.
         * </p>
         * 
         * <p>
         * Set iteration index to first element and then try to found actual count for given elements.
         * </p>
         */
        public ElementIterator() {
            index = 1;
            recount();
        }

        /**
         * Recounts the actual count of elements by given elementLocator.
         */
        private void recount() {
            count = size();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Iterator#hasNext()
         */
        public boolean hasNext() {
            return index <= count;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Iterator#next()
         */
        public T next() {
            if (hasNext()) {
                return get(index++);
            } else {
                throw new NoSuchElementException();
            }
        }

        /**
         * <b>Unsupported</b> for this Iterator.
         * 
         * @throws UnsupportedOperationException
         *             in every call
         * 
         */
        public void remove() {
            throw new UnsupportedOperationException(this.getClass().getCanonicalName() + " doesn't support remove()");
        }
    }
}
