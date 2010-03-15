package org.jboss.test.selenium.framework;

import org.jboss.test.selenium.geometry.Point;
import org.jboss.test.selenium.locator.ElementLocator;

public class ExtendedTypedSelenium extends DefaultTypedSelenium {
	
	ExtendedSelenium getExtendedSelenium() {
		if (selenium instanceof ExtendedSelenium) {
			return (ExtendedSelenium) selenium;
		} else {
			throw new UnsupportedOperationException("Assigned Selenium isn't instance of ExtendedSelenium");
		}
	}
	
	/**
	 * Get current style value of element given by locator.
	 * 
	 * Use CSS style notation instead of JavaScript's camel notation!
	 * 
	 * This methods of getting current style value haven't absolute browser
	 * compatibility.
	 * 
	 * E.g.: use property "background-color" instead of "backgroundColor"
	 * 
	 * @param locator
	 *            of element from what we want to get current style value
	 * @param property
	 *            CSS style property what we can to recognize
	 * @return current value of property if its element exists and has this
	 *         property value set; null value otherwise
	 * @throws IllegalStateException
	 *             if is caught unrecognized throwable
	 */
	public String getStyle(ElementLocator elementLocator, String property) {
		return getExtendedSelenium().getStyle(elementLocator.getAsString(), property);
	}

	/**
	 * Aligns screen to top (resp. bottom) of element given by locator.
	 * 
	 * @param locator
	 *            of element which should be screen aligned to
	 * @param alignToTop
	 *            should be top border of screen aligned to top border of
	 *            element
	 */
	public void scrollIntoView(ElementLocator elementLocator, boolean alignToTop) {
		getExtendedSelenium().scrollIntoView(elementLocator.getAsString(), String.valueOf(alignToTop));
	}

	/**
	 * Simulates a user hovering a mouse over the specified element at specific
	 * coordinates relative to element.
	 * 
	 * @param locator
	 *            element's locator
	 * @param coordString
	 *            specifies the x,y position (i.e. - 10,20) of the mouse event
	 *            relative to the element returned by the locator.
	 */
	public void mouseOverAt(ElementLocator elementLocator, Point point) {
		getExtendedSelenium().mouseOverAt(elementLocator.getAsString(), point.getCoords());
	}

	/**
	 * Returns whether the element is displayed on the page.
	 * 
	 * @param locator
	 *            element locator
	 * @return if style contains "display: none;" returns false, else returns
	 *         true
	 */
	public boolean isDisplayed(ElementLocator elementLocator) {
		return getExtendedSelenium().isDisplayed(elementLocator.getAsString());
	}

	/**
	 * Checks if element given by locator is member of CSS class given by
	 * className.
	 * 
	 * @param className
	 *            name of CSS class
	 * @param locator
	 *            element's locator
	 * @return true if element given by locator is member of CSS class given by
	 *         className
	 */
	public boolean belongsClass(ElementLocator elementLocator, String className) {
		return getExtendedSelenium().belongsClass(elementLocator.getAsString(), className);
	}

}