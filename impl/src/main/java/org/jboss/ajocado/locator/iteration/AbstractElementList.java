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
package org.jboss.ajocado.locator.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jboss.ajocado.framework.AjaxSelenium;
import org.jboss.ajocado.framework.AjaxSeleniumProxy;
import org.jboss.ajocado.locator.IterableLocator;

/**
 * <p>
 * Abstract class able to iterate over <tt>IterableLocator&lt;T&gt;</tt>.
 * </p>
 * 
 * <p>
 * Encapsulates the functionality around the iterating over <tt>IterableLocator</tt>s.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 * @param <T>
 *            the IterableLocator implementation
 * @see ChildElementList
 * @see ElementOcurrenceList
 */
public abstract class AbstractElementList<T extends IterableLocator<T>> implements Iterable<T> {

    /**
     * Proxy to local selenium instance
     */
    protected AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();
    
    /** The iterable locator. */
    T iterableLocator;

    /**
     * Instantiates a new abstract element list for given iterableLocator.
     * 
     * @param iterableLocator
     *            the iterable locator
     */
    public AbstractElementList(T iterableLocator) {
        super();
        this.iterableLocator = iterableLocator;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Iterable#iterator()
     */
    public Iterator<T> iterator() {
        return new ElementIterator();
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
            count = selenium.getCount(iterableLocator);
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
                return abstractNthElement(index++);
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

    /**
     * <p>
     * The point of extension.
     * </p>
     * 
     * <p>
     * Returns the <i>N</i>-th element (= on given index) in element list given by iterableLocator.
     * 
     * @param index
     *            the index of element to get
     * @return the IterableLocator instance on given index in element list
     */
    abstract T abstractNthElement(int index);
}
