package org.jboss.test.selenium.locator.iteration;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jboss.test.selenium.framework.TypedSelenium;
import org.jboss.test.selenium.locator.IterableLocator;

public abstract class AbstractElementList<T extends IterableLocator> implements Iterable<T> {
	
	TypedSelenium typedSelenium;
	IterableLocator iterableLocator;
	
	public AbstractElementList(TypedSelenium typedSelenium, T iterableLocator) {
		super();
		this.typedSelenium = typedSelenium;
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
			count = typedSelenium.getCount(iterableLocator);
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
