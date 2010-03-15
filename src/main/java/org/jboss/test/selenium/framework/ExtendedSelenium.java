package org.jboss.test.selenium.framework;

import org.apache.commons.lang.Validate;

import com.thoughtworks.selenium.CommandProcessor;
import com.thoughtworks.selenium.DefaultSelenium;

import static java.text.MessageFormat.format;

public class ExtendedSelenium extends DefaultSelenium {

	public ExtendedSelenium(CommandProcessor commandProcessor) {
		super(commandProcessor);
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
    public String getStyle(String locator, String property) {
        return commandProcessor.doCommand("getStyle", new String[] { locator, property, });
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
	public void scrollIntoView(String locator, String alignToTop) {
		commandProcessor.doCommand("scrollIntoView", new String[] { locator, alignToTop, });
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
    public void mouseOverAt(String locator, String coordString) {
		commandProcessor.doCommand("doMouseOverAt", new String[] { locator, coordString });
    }
    
    /**
	 * Returns whether the element is displayed on the page.
	 * 
	 * @param locator
	 *            element locator
	 * @return if style contains "display: none;" returns false, else returns
	 *         true
	 */
    public boolean isDisplayed(String locator) {
        try {
            return !getStyle(locator, "display").contains("none");
        } catch (Exception e) {
            // there is no attribute "style"
            return true;
        }
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
    public boolean belongsClass(String locator, String className) {
        Validate.notNull(className);
        Validate.notNull(locator);

        String classLocator = format("{0}@class", locator);
        String classNames = getAttribute(classLocator);

        String regex = format("(?:^|.*\\s){0}(?:$|\\s.*)", className);
        return classNames.matches(regex);
    }
}
