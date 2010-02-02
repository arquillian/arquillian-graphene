/*******************************************************************************
 * JBoss, Home of Professional Open Source
 * Copyright 2009, Red Hat, Inc. and individual contributors
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
 *******************************************************************************/

package org.jboss.test.selenium;

import static com.thoughtworks.selenium.SeleneseTestCase.seleniumEquals;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.waiting.Condition;
import org.jboss.test.selenium.waiting.Retrieve;
import org.jboss.test.selenium.waiting.Wait;
import org.testng.Assert;

import com.thoughtworks.selenium.Selenium;
import com.thoughtworks.selenium.SeleniumException;

/**
 * Class that serves as basis for all Selenium-based tests.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:ppitonak@redhat.com">Pavol Pitonak</a>
 * @version $Revision$
 */
public abstract class AbstractSeleniumTestCase {

    protected Properties locatorsProperties;
    protected Properties messagesProperties;
    protected Selenium selenium;
    /**
     * Generic timeout in miliseconds used in every selenium function
     * waitForPageToLoad()
     */
    public static String PAGE_LOAD = "180000";
    /** Generic timeout in miliseconds used for AJAX timeouts */
    public static long AJAX_LOAD = 3000;
    /** Element timeout in seconds used in waitForElement functions */
    public static int ELEM_TIMEOUT = 220;

    public AbstractSeleniumTestCase() {
        locatorsProperties = getLocatorsProperties();
        messagesProperties = getMessagesProperties();
    }

    /**
     * This method initializes properties with messages.
     * 
     * Should be overridden to properly initialize properties.
     * 
     * @return initialized properties
     */
    protected Properties getMessagesProperties() {
        return new Properties();
    }

    /**
     * This method initializes properties with locators.
     * 
     * Should be overridden to properly initialize properties.
     * 
     * @return initialized properties
     */
    protected Properties getLocatorsProperties() {
        return new Properties();
    }

    /**
     * Loads properties from specified resource file
     * 
     * @param resource
     *            where in classpath the file is located
     * @return loaded properties
     * @throws IOException
     *             if an error occurred when reading resource file
     */
    protected static Properties getProperties(String resource) throws IOException {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        InputStream is = cl.getResourceAsStream(resource);

        Properties props = new Properties();

        if (is == null) {
            is = AbstractSeleniumTestCase.class.getResourceAsStream(resource);
        }

        if (is != null) {
            props.load(is);
        }

        return props;
    }

    /**
     * Loads properties from specified resource file in context of specified
     * class' package.
     * 
     * @param tClass
     *            named resource will be searched in context of this class
     * @param name
     *            name of resource contained in current class' package
     * @return loaded properties
     * @throws IllegalStateException
     *             if an error occurred when reading resource file
     */
    protected <T> Properties getNamedPropertiesForClass(Class<T> tClass, String name) throws IllegalStateException {
        String propFile = tClass.getPackage().getName();
        propFile = propFile.replace('.', '/');
        propFile = String.format("%s/%s.properties", propFile, name);

        try {
            return getProperties(propFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * From given properties class gets property using "property" key or if
     * value with given key doesn't exist, returns substitution'
     * 
     * @param properties
     *            loaded properties
     * @param property
     *            key that will be found in properties
     * @param subst
     *            substitution which will be used in the case that property with
     *            given key doesn't exist
     * @throws IllegalStateException
     *             when property wasn't found and substitution isn't set
     * @return property value for given key or substitution if it doesn't exist
     */
    private String getProperty(Properties properties, String property, String subst) {
        if (properties == null || properties.getProperty(property) == null) {
            if (StringUtils.isEmpty(subst)) {
                throw new IllegalStateException("property '" + property + "' wasn't found and substitution isn't set");
            } else {
                return subst;
            }
        } else {
            return properties.getProperty(property);
        }
    }

    /**
     * Gets the property from messagesProperties. Use getMsg(String, String)
     * instead of this deprecated method.
     * 
     * @param prop
     *            the name of the property
     * @param subst
     *            the value which is returned in the case the property isn't set
     * @throws IllegalStateException
     *             when property wasn't found and substitution isn't set
     * @return the property
     * @see org.jboss.test.selenium.AbstractSeleniumTestCase#getMsg(String,
     *      String) getMsg(String, String)
     * @see org.jboss.test.selenium.AbstractSeleniumTestCase#getMessagesProperties()
     *      getMessagesProperties()
     */
    @Deprecated
    public String getMess(String prop, String subst) {
        return getMsg(prop, subst);
    }

    /**
     * Gets the property from messagesProperties. Use getMess(String) instead of
     * this deprecated method.
     * 
     * @param prop
     *            the name of the property
     * @throws IllegalStateException
     *             when property wasn't found
     * @return the property
     * @see org.jboss.test.selenium.AbstractSeleniumTestCase#getMsg(String)
     *      getMsg(String)
     * @see org.jboss.test.selenium.AbstractSeleniumTestCase#getMessagesProperties()
     *      getMessagesProperties()
     */
    @Deprecated
    public String getMess(String prop) {
        return getMsg(prop, null);
    }

    /**
     * Gets the property from messagesProperties.
     * 
     * @param prop
     *            the name of the property
     * @param subst
     *            the value which is returned in the case the property isn't set
     * @throws IllegalStateException
     *             when property wasn't found and substitution isn't set
     * @return the property
     * @see org.jboss.test.selenium.AbstractSeleniumTestCase#getMessagesProperties()
     *      getMessagesProperties()
     */
    public String getMsg(String prop, String subst) {
        return getProperty(messagesProperties, prop, subst);
    }

    /**
     * Gets the property from messagesProperties.
     * 
     * @param prop
     *            the name of the property
     * @throws IllegalStateException
     *             when property wasn't found
     * @return the property
     * @see org.jboss.test.selenium.AbstractSeleniumTestCase#getMessagesProperties()
     *      getMessagesProperties()
     */
    public String getMsg(String prop) {
        return getMsg(prop, null);
    }

    /**
     * Gets the property from messagesProperties and use it to format Message
     * with given arguments
     * 
     * @param prop
     *            the name of the property with message format.
     * @param args
     *            an array of atributes to be formatted and substituted to prop
     * @throws IllegalStateException
     *             when property wasn't found
     * @return the property
     */
    public String formatMsg(String prop, Object... args) {
        return format(getMsg(prop, null), args);
    }

    /**
     * Gets the property from locatorsProperties
     * 
     * @param prop
     *            the name of the property
     * @param subst
     *            the value which is returned in the case the property isn't set
     * @throws IllegalStateException
     *             when property wasn't found and substitution isn't set
     * @return the property
     * @see org.jboss.test.selenium.AbstractSeleniumTestCase#getLocatorsProperties()
     *      getLocatorsProperties()
     */
    public String getLoc(String prop, String subst) {
        return getProperty(locatorsProperties, prop, subst);
    }

    /**
     * Gets the property from locatorsProperties
     * 
     * @param prop
     *            the name of the property
     * @throws IllegalStateException
     *             when property wasn't found
     * @return the property
     * @see org.jboss.test.selenium.AbstractSeleniumTestCase#getLocatorsProperties()
     *      getLocatorsProperties()
     */
    public String getLoc(String prop) {
        return getLoc(prop, null);
    }

    /**
     * Gets the property from messagesProperties and use it to format Message
     * with given arguments
     * 
     * @param prop
     *            the name of the property with message format.
     * @param args
     *            an array of atributes to be formatted and substituted to prop
     * @throws IllegalStateException
     *             when property wasn't found
     * @return the property
     * @see org.jboss.test.selenium.AbstractSeleniumTestCase#getLocatorsProperties()
     *      getLocatorsProperties()
     */
    public String formatLoc(String prop, Object... args) {
        return format(getLoc(prop, null), args);
    }

    /**
     * Uses a MessageFormat.format() to prepare given format string and use it
     * to format result with given arguments.
     * 
     * @param format
     *            string used in MessageFormat.format()
     * @param args
     *            used to formatting given format string
     * @return string formatted using given arguments
     */
    public static String format(String format, Object... args) {
        String message = preformat(format);
        return MessageFormat.format(message, args);
    }

    /**
     * Prepares a message to use in Message.format()
     * 
     * @param message
     *            prepared to use in Message.format()
     * @return message prepared to use in Message.format()
     */
    private static String preformat(String message) {
        return message.replace("'", "''").replace("\\''", "'");
    }

    /**
     * Click on an element only in the case element is present on the screen.
     * 
     * @param id
     *            element locator
     */
    public void clickIfVisible(String id) {

        if (selenium.isElementPresent(id)) {
            selenium.click(id);
            selenium.waitForPageToLoad(PAGE_LOAD);
        }
    }

    /**
     * Finding the correct row in a table due to input string and columns. The
     * function uses selenium javascript extension findTableRow. It tries to
     * look up the input string in the chosen column in selected table. After
     * the first occurrence of input string, it returns the number of row where
     * it was found.
     * 
     * @param tableLocation
     *            the table locator
     * @param searchName
     *            the string which the function searches for
     * @param searchCol
     *            the column in which the function searches for
     * @return the number of row with the first occurrence of searchName string.
     */
    public int findTableRow(String tableLocation, String searchName, int searchCol) {
        return Integer.valueOf(
                selenium.getEval("selenium.findTableRow(\"" + tableLocation + "\",'" + searchName + "'," + searchCol
                        + ")")).intValue();
    }

    /**
     * Counts table rows for selected table.
     * 
     * @param tableLocation
     *            the table locator
     * @return the number of rows of the table
     */
    public int countTableRows(String tableLocation) {
        return Integer.valueOf(selenium.getEval("selenium.countTableRows(\"" + tableLocation + "\")")).intValue();
    }

    // TODO refactor
    /**
     * Selects a value only if the value is present in the select. If the value
     * is not present, selection fails.
     * 
     * @param locator
     *            the locator of the select tag
     * @param value
     *            the value which should be selected
     */
    public void safeSelect(String locator, String value) {
        waitForElement(locator);

        for (int second = 0;; second++) {
            if (second >= ELEM_TIMEOUT) {
                Assert.fail("Element " + locator + " not found.");
            }
            try {
                String[] opts = selenium.getSelectOptions(locator);
                boolean isAvailable = false;

                for (String opt : opts) {
                    if (opt.equals(value)) {
                        isAvailable = true;
                    }
                }

                if (isAvailable) {
                    break;
                }
            } catch (Exception e) {
            }
            waitFor(1000);
        }

        selenium.select(locator, "label=" + value);
    }

    /**
     * Click and wait. Substitution for two selenium commands click and
     * waitForPageToLoad.
     * 
     * @param locator
     *            the locator of the element to be clicked on
     */
    public void clickAndWait(String locator) {
        waitForElement(locator);
        selenium.click(locator);
        selenium.waitForPageToLoad(PAGE_LOAD);
    }

    /**
     * Open and wait. Substitution for two selenium commands open and
     * waitForPageToLoad.
     * 
     * @param locator
     *            the address to be opened
     */
    public void openAndWait(String locator) {
        selenium.open(locator);
        selenium.waitForPageToLoad(PAGE_LOAD);
    }

    /**
     * Select if not selected. If the value is already selected, the method does
     * nothing.
     * 
     * @param selector
     *            the selector locator
     * @param label
     *            the label to be selected
     */
    public void selectIfNotSelected(String selector, String label) {
        waitForElement(selector);
        if (!selenium.getSelectedLabel(selector).equals(label)) {
            selenium.select(selector, "label=" + label);
            selenium.waitForPageToLoad(PAGE_LOAD);
        }
    }

    /**
     * Asserts the text order on the page. The messages which order is to be
     * determined are inputed in a comma separated way. i.e.
     * "string1;string2;string3". The method asserts that they appear on the
     * page in the same order as typed in the input string. It returns true if
     * the order is the same or false otherwise.
     * 
     * @param text
     *            input text in comma separated format which order is to be
     *            asserted
     * 
     * @return true if the order is the same or false otherwise
     */
    public boolean assertTextOrder(String text) {
        return Boolean.valueOf(selenium.getEval("selenium.assertTextOrder(\"" + text + "\")"));
    }

    /**
     * Waits for specified time in ms. Used mostly in AJAX based tests.
     * 
     * @param time
     *            the time (in ms) to be waited for.
     */
    public void waitFor(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Waits for element to appear on the screen. Used mostly in AJAX based
     * tests.
     * 
     * @param locator
     *            the locator of the element to be waited for
     */
    public void waitForElement(String locator) {
        waitForElement(locator, 1000);
    }
    
    /**
     * Waits for element to appear on the screen. Used mostly in AJAX based
     * tests.
     * 
     * @param locator
     *            the locator of the element to be waited for
     * @param step interval between two pollings
     */
    public void waitForElement(final String locator, int step) {
        Wait.failWith("Element \"" + locator + "\" not found.").interval(step).timeout(ELEM_TIMEOUT).until(new Condition() {
            public boolean isTrue() {
                return selenium.isElementPresent(locator);
            }
        });
    }

    /**
     * Waits for element to appear on the screen. Used mostly in AJAX based
     * tests.
     * 
     * @param locatorElem
     *            the element locator
     * @param timeToWait
     *            the time (in ms) to wait until timeout is reached
     */
    public void waitForElement(String locatorElem, long timeToWait) {
        for (int i = 0;; i++) {
            if (i * 500 >= timeToWait) {
                Assert.fail("Element " + locatorElem + " not found.");
            }
            try {
                if (selenium.isElementPresent(locatorElem)) {
                    break;
                }
            } catch (Exception e) {
            }
            waitFor(500);
        }
    }

    /**
     * Waits for element to appear on the screen. Used mostly in AJAX based
     * tests.
     * 
     * @param locatorElem
     *            the element locator
     * @param locatorLink
     *            the link locator - not used
     * @param timeToWait
     *            the time (in ms) to wait until timeout is reached
     * @param n
     *            the n
     * @see org.jboss.test.selenium.AbstractSeleniumTestCase#waitForElement(String,
     *      long) waitForElement(String, long)
     */
    @Deprecated
    public void waitForElement(String locatorElem, String locatorLink, long timeToWait, long n) {
        for (int i = 0;; i++) {
            if (i >= n) {
                Assert.fail("Element " + locatorElem + " not found.");
            }
            try {
                if (selenium.isElementPresent(locatorElem)) {
                    break;
                }
            } catch (Exception e) {
            }
            waitFor(1000);
        }
    }

    /**
     * Waits for text to appear on the screen. Used mostly in AJAX based tests.
     * 
     * @param text
     *            the text to be waited for
     */
    public void waitForText(String text) {
        for (int second = 0;; second++) {
            if (second >= ELEM_TIMEOUT) {
                Assert.fail("Text '" + text + "' not found.");
            }
            try {
                if (selenium.isTextPresent(text)) {
                    break;
                }
            } catch (Exception e) {
            }
            waitFor(100);
        }
    }

    /**
     * Waits for element specified by locator does contain text specified by
     * pattern
     * 
     * @param locator
     *            text of this locator's element will be tested for equivalence
     *            to pattern text
     * @param pattern
     *            pattern text, which will be tested to equivalence to element's
     *            text given by locator
     * 
     */
    public void waitForTextEquals(final String locator, final String pattern) {
        Wait.until(new Condition() {
            public boolean isTrue() {
                return seleniumEquals(selenium.getText(locator), pattern);
            }
        });
    }

    /**
     * Wait for text of element given by locator changes from lastText to
     * another text.
     * 
     * @param locator
     *            locator of element
     * @param lastText
     *            current text what we are waiting for change
     */
    public void waitForTextChanges(final String locator, final String lastText) {
        waitForTextChangesAndReturn(locator, lastText);
    }

    /**
     * Wait for text of element given by locator changes from lastText to
     * another text and returns this new text.
     * 
     * @param locator
     *            locator of element
     * @param lastText
     *            current text what we are waiting for change
     * @return new value of text
     */
    public String waitForTextChangesAndReturn(final String locator, final String lastText) {
        return Wait.waitForChangeAndReturn(lastText, new Retrieve<String>() {
            public String retrieve() {
                return selenium.getText(locator);
            }
        });
    }

    /**
     * Wait for attribute of element given by attributeLocator changes from
     * attributeValue to another value.
     * 
     * @param attributeLocator
     *            locator of attribute
     * @param attributeValue
     *            current value attribute what we are waiting for change
     */
    public void waitForAttributeChanges(final String attributeLocator, final String attributeValue) {
        waitForAttributeChangesAndReturn(attributeLocator, attributeValue);
    }

    /**
     * Wait for attribute of element given by attributeLocator changes from
     * attributeValue to another value and returns this new value.
     * 
     * @param attributeLocator
     *            locator of attribute
     * @param attributeValue
     *            current value attribute what we are waiting for change
     * @return new value of attribute
     */
    public String waitForAttributeChangesAndReturn(final String attributeLocator, final String attributeValue) {
        return Wait.waitForChangeAndReturn(attributeValue, new Retrieve<String>() {
            public String retrieve() {
                return selenium.getAttribute(attributeLocator);
            }
        });
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
        String evaluate = MessageFormat.format("selenium.getStyle(\"{0}\", \"{1}\")", locator, property);
        String result = null;
        try {
            result = selenium.getEval(evaluate);
        } catch (Exception e) {
            if ("ERROR: Threw an exception: null property value".equals(e.getMessage())) {
                return null;
            }
            throw new SeleniumException(e);
        }
        return result;
    }

    /**
     * Aligns screen to top (resp. bottom) of element given by locator.
     * 
     * TODO should be reimplemented to use of JQuery.scrollTo
     * 
     * @param locator
     *            of element which should be screen aligned to
     * @param alignToTop
     *            should be top border of screen aligned to top border of
     *            element
     */
    public void scrollIntoView(String locator, boolean alignToTop) {
        String evaluate = MessageFormat.format("selenium.scrollIntoView(\"{0}\", {1})", locator, Boolean
                .toString(alignToTop));
        selenium.getEval(evaluate);
    }

	/**
	 * Returns the count of elements for given jQuery selector
	 * 
	 * @param selector
	 *            jQuery selector
	 * @return count of elements matching given selector
	 */
	public int getJQueryCount(String selector) {
		String evaluate = format("selenium.getJQueryCount(\"{0}\")", selector.replaceFirst("^jquery=", ""));
		String result = selenium.getEval(evaluate);
		return Integer.parseInt(result);
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
    public boolean belongsClass(String className, String locator) {
        Validate.notNull(className);
        Validate.notNull(locator);

        String classLocator = format("{0}@class", locator);
        String classNames = getAttributeOrNull(classLocator);

        if (classNames == null) {
            return false;
        }

        String regex = format("(?:^|.*\\s){0}(?:$|\\s.*)", className);
        return classNames.matches(regex);
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
        selenium.getEval(format("selenium.doMouseOverAt(\"{0}\", \"{1}\")", locator, coordString));
    }

    /**
     * Gets the text of an element. This works for any element that contains
     * text. This command uses either the textContent (Mozilla-like browsers) or
     * the innerText (IE-like browsers) of the element, which is the rendered
     * text shown to the user.
     * 
     * If no element with given locator is found, returns null.
     * 
     * @param locator
     *            an element locator
     * @return the text of the element or null if element's wasn't found
     */
    public String getTextOrNull(String locator) {
        try {
            return selenium.getEval(format("selenium.getTextOrNull(\"{0}\")", locator));
        } catch (RuntimeException e) {
            if ("ERROR: Threw an exception: element is not found".equals(e.getMessage())) {
                return null;
            }
            throw e;
        }
    }

    /**
     * Gets the value of an element attribute. The value of the attribute may
     * differ across browsers (this is the case for the "style" attribute, for
     * example).
     * 
     * If no element with given attribute's locator is found, returns null.
     * 
     * @param attributeLocator
     *            an element's attribute locator
     * @return the attribute of the element or null if element's attribute
     *         wasn't found
     */
    public String getAttributeOrNull(String attributeLocator) {
        try {
            return selenium.getAttribute(attributeLocator);
        } catch (Exception e) {
            if (e.getMessage().startsWith("ERROR: Could not find element attribute:")
                    || e.getMessage().matches("^ERROR: Element .* not found$")) {
                return null;
            }
            throw new IllegalStateException("getAttributeOrNull unexpected state - " + e.getMessage(), e);
        }
	}

	/**
	 * Add required script to page once.
	 * 
	 * (This code will refuse adding script to the page duplicitly due to usage
	 * of hash, so you can add it thought you are not sure script is already
	 * added or not - this is usefull for adding libraries directly to the
	 * page).
	 * 
	 * This script will be appended to end of the body tag within the script
	 * tag.
	 * 
	 * @param script what should be added to the page
	 */
	public void addRequiredScript(String script) {
		final String escapedScript = StringEscapeUtils.escapeJavaScript(script);
		final String id = "selenium_script_" + Integer.toString(escapedScript.hashCode());
		if (selenium.isElementPresent(id)) {
			return;
		}
		selenium.getEval(format("selenium.addScriptLocally('{0}', '{1}')", id, escapedScript));
	}

	/**
	 * Remove *jquery= prefix of locators given by jQuery selector
	 * 
	 * @param jqueryLocator locator in jQuery selector's syntax
	 * @return jQuery selector without ^jquery= locator prefix
	 */
	public String removeJQueryPrefix(String jqueryLocator) {
		final String prefix = "jquery=";

		if (jqueryLocator.startsWith(prefix)) {
			return jqueryLocator.replaceFirst(prefix, "");
		}

		throw new IllegalArgumentException(format(
				"Given locator '{0}' isn't valid jQuery locator (doesn't start with '{1}')", jqueryLocator, prefix));
	}
}
