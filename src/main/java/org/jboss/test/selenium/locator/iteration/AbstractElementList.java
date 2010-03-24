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
package org.jboss.test.selenium.locator.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.internal.Contextual;
import org.jboss.test.selenium.locator.IterableLocator;

public abstract class AbstractElementList<T extends IterableLocator<T>> implements Contextual, Iterable<T> {
	
	T iterableLocator;
	
	public AbstractElementList(T iterableLocator) {
		super();
		this.iterableLocator = iterableLocator;
	}

	public Iterator<T> iterator() {
		return new ElementIterator();
	}
	
	public class ElementIterator implements Iterator<T> {
		
		private int index;
		private int count;
		
		public ElementIterator() {
			index = 0;
			recount();
		}
		
		private final void recount() {
			count = AjaxSelenium.getCurrentContext(AbstractElementList.this, iterableLocator).getCount(iterableLocator);
		}

		public boolean hasNext() {
			return index <= count;
		}

		public T next() {
			if (hasNext()) {
				return abstractNthElement(index++);
			} else {
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			throw new UnsupportedOperationException(this.getClass().getCanonicalName() + " doesn't support remove");
		}
	}
	
	abstract T abstractNthElement(int index);
}
