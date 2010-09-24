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
package org.jboss.test.selenium.framework;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.List;
import java.util.Set;

import org.jboss.test.selenium.cookie.Cookie;
import org.jboss.test.selenium.cookie.CreateCookieOptions;
import org.jboss.test.selenium.cookie.DeleteCookieOptions;
import org.jboss.test.selenium.dom.Event;
import org.jboss.test.selenium.encapsulated.FrameLocator;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.encapsulated.LogLevel;
import org.jboss.test.selenium.encapsulated.NetworkTraffic;
import org.jboss.test.selenium.encapsulated.NetworkTrafficType;
import org.jboss.test.selenium.encapsulated.XpathLibrary;
import org.jboss.test.selenium.geometry.Dimension;
import org.jboss.test.selenium.geometry.Offset;
import org.jboss.test.selenium.geometry.Point;
import org.jboss.test.selenium.locator.AttributeLocator;
import org.jboss.test.selenium.locator.ElementLocationStrategy;
import org.jboss.test.selenium.locator.ElementLocator;
import org.jboss.test.selenium.locator.IdLocator;
import org.jboss.test.selenium.locator.IterableLocator;
import org.jboss.test.selenium.locator.option.OptionLocator;

/**
 * The type-safe wrapper for Selenium API.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface TypedSelenium {

    /** Launches the browser with a new Selenium session */
    void start();

    /** Ends the test session, killing the browser */
    void stop();

    /**
     * Clicks on a link, button, checkbox or radio button. If the click action causes a new page to load (like a link
     * usually does), call waitForPageToLoad.
     * 
     * @param locator
     *            an element locator
     */
    void click(ElementLocator<?> locator);

    /**
     * Double clicks on a link, button, checkbox or radio button. If the double click action causes a new page to load
     * (like a link usually does), call waitForPageToLoad.
     * 
     * @param locator
     *            an element locator
     */
    void doubleClick(ElementLocator<?> locator);

    /**
     * Simulates opening the context menu for the specified element (as might happen if the user "right-clicked" on the
     * element).
     * 
     * @param locator
     *            an element locator
     */
    void contextMenu(ElementLocator<?> locator);

    /**
     * Clicks on a link, button, checkbox or radio button. If the click action causes a new page to load (like a link
     * usually does), call waitForPageToLoad.
     * 
     * @param locator
     *            an element locator
     * @param coords
     *            specifies the point (x,y position) of the mouse event relative to the element returned by the locator.
     */
    void clickAt(ElementLocator<?> locator, Point coords);

    /**
     * Doubleclicks on a link, button, checkbox or radio button. If the action causes a new page to load (like a link
     * usually does), call waitForPageToLoad.
     * 
     * @param locator
     *            an element locator
     * @param coordString
     *            specifies the point (x,y position) of the mouse event relative to the element returned by the locator.
     */
    void doubleClickAt(ElementLocator<?> locator, Point coords);

    /**
     * Simulates opening the context menu for the specified element (as might happen if the user "right-clicked" on the
     * element).
     * 
     * @param locator
     *            an element locator
     * @param coordString
     *            specifies the point (x,y position) of the mouse event relative to the element returned by the locator.
     */
    void contextMenuAt(ElementLocator<?> locator, Point coords);

    /**
     * Explicitly simulate an event, to trigger the corresponding handler.
     * 
     * @param locator
     *            an element locator
     * @param eventName
     *            the event name, e.g. "focus" or "blur"
     */
    void fireEvent(ElementLocator<?> locator, Event event);

    /**
     * Move the focus to the specified element; for example, if the element is an input field, move the cursor to that
     * field.
     * 
     * @param locator
     *            an element locator
     */
    void focus(ElementLocator<?> locator);

    /**
     * Simulates a user pressing and releasing a key.
     * 
     * @param locator
     *            an element locator
     * @param keySequence
     *            Either be a string("\" followed by the numeric keycode of the key to be pressed, normally the ASCII
     *            value of that key), or a single character. For example: " w", "\119".
     */
    void keyPress(ElementLocator<?> locator, String keySequence);

    /**
     * Press the shift key and hold it down until doShiftUp() is called or a new page is loaded.
     */
    void shiftKeyDown();

    /**
     * Release the shift key.
     */
    void shiftKeyUp();

    /**
     * Press the meta key and hold it down until doMetaUp() is called or a new page is loaded.
     */
    void metaKeyDown();

    /**
     * Release the meta key.
     */
    void metaKeyUp();

    /**
     * Press the alt key and hold it down until doAltUp() is called or a new page is loaded.
     */
    void altKeyDown();

    /**
     * Release the alt key.
     */
    void altKeyUp();

    /**
     * Press the control key and hold it down until doControlUp() is called or a new page is loaded.
     */
    void controlKeyDown();

    /**
     * Release the control key.
     */
    void controlKeyUp();

    /**
     * Simulates a user pressing a key (without releasing it yet).
     * 
     * @param locator
     *            an element locator
     * @param keySequence
     *            Either be a string("\" followed by the numeric keycode of the key to be pressed, normally the ASCII
     *            value of that key), or a single character. For example: " w", "\119".
     */
    void keyDown(ElementLocator<?> locator, String keySequence);

    /**
     * Simulates a user releasing a key.
     * 
     * @param locator
     *            an element locator
     * @param keySequence
     *            Either be a string("\" followed by the numeric keycode of the key to be pressed, normally the ASCII
     *            value of that key), or a single character. For example: " w", "\119".
     */
    void keyUp(ElementLocator<?> locator, String keySequence);

    /**
     * Simulates a user hovering a mouse over the specified element.
     * 
     * @param locator
     *            an element locator
     */
    void mouseOver(ElementLocator<?> locator);

    /**
     * Simulates a user moving the mouse pointer away from the specified element.
     * 
     * @param locator
     *            an element locator
     */
    void mouseOut(ElementLocator<?> locator);

    /**
     * Simulates a user pressing the left mouse button (without releasing it yet) on the specified element.
     * 
     * @param locator
     *            an element locator
     */
    void mouseDown(ElementLocator<?> locator);

    /**
     * Simulates a user pressing the right mouse button (without releasing it yet) on the specified element.
     * 
     * @param locator
     *            an element locator
     */
    void mouseDownRight(ElementLocator<?> locator);

    /**
     * Simulates a user pressing the left mouse button (without releasing it yet) at the specified location.
     * 
     * @param locator
     *            an element locator
     * @param coordString
     *            specifies the x,y position (i.e. - 10,20) of the mouse event relative to the element returned by the
     *            locator.
     */
    void mouseDownAt(ElementLocator<?> locator, Point coords);

    /**
     * Simulates a user pressing the right mouse button (without releasing it yet) at the specified location.
     * 
     * @param locator
     *            an element locator
     * @param coordString
     *            specifies the point (x,y position) of the mouse event relative to the element returned by the locator.
     */
    void mouseDownRightAt(ElementLocator<?> locator, Point coords);

    /**
     * Simulates the event that occurs when the user releases the mouse button (i.e., stops holding the button down) on
     * the specified element.
     * 
     * @param locator
     *            an element locator
     */
    void mouseUp(ElementLocator<?> locator);

    /**
     * Simulates the event that occurs when the user releases the right mouse button (i.e., stops holding the button
     * down) on the specified element.
     * 
     * @param locator
     *            an element locator
     */
    void mouseUpRight(ElementLocator<?> locator);

    /**
     * Simulates the event that occurs when the user releases the mouse button (i.e., stops holding the button down) at
     * the specified location.
     * 
     * @param locator
     *            an element locator
     * @param coordString
     *            specifies the point (x,y position) of the mouse event relative to the element returned by the locator.
     */
    void mouseUpAt(ElementLocator<?> locator, Point coords);

    /**
     * Simulates the event that occurs when the user releases the right mouse button (i.e., stops holding the button
     * down) at the specified location.
     * 
     * @param locator
     *            an element locator
     * @param coordString
     *            specifies the point (x,y position) of the mouse event relative to the element returned by the locator.
     */
    void mouseUpRightAt(ElementLocator<?> locator, Point coords);

    /**
     * Simulates a user pressing the mouse button (without releasing it yet) on the specified element.
     * 
     * @param locator
     *            an element locator
     */
    void mouseMove(ElementLocator<?> locator);

    /**
     * Simulates a user pressing the mouse button (without releasing it yet) on the specified element.
     * 
     * @param locator
     *            an element locator
     * @param coordString
     *            specifies the point (x,y position) of the mouse event relative to the element returned by the locator.
     */
    void mouseMoveAt(ElementLocator<?> locator, Point coords);

    /**
     * Sets the value of an input field, as though you typed it in.
     * 
     * <p>
     * Can also be used to set the value of combo boxes, check boxes, etc. In these cases, value should be the value of
     * the option selected, not the visible text.
     * </p>
     * 
     * @param locator
     *            an element locator
     * @param value
     *            the value to type
     */
    void type(ElementLocator<?> locator, String value);

    /**
     * Simulates keystroke events on the specified element, as though you typed the value key-by-key.
     * 
     * <p>
     * This is a convenience method for calling keyDown, keyUp, keyPress for every character in the specified string;
     * this is useful for dynamic UI widgets (like auto-completing combo boxes) that require explicit key events.
     * </p>
     * <p>
     * Unlike the simple "type" command, which forces the specified value into the page directly, this command may or
     * may not have any visible effect, even in cases where typing keys would normally have a visible effect. For
     * example, if you use "typeKeys" on a form element, you may or may not see the results of what you typed in the
     * field.
     * </p>
     * <p>
     * In some cases, you may need to use the simple "type" command to set the value of the field and then the
     * "typeKeys" command to send the keystroke events corresponding to what you just typed.
     * </p>
     * 
     * @param locator
     *            an element locator
     * @param value
     *            the value to type
     */
    void typeKeys(ElementLocator<?> locator, String value);

    /**
     * Set execution speed (i.e., set the millisecond length of a delay which will follow each selenium operation). By
     * default, there is no such delay, i.e., the delay is 0 milliseconds.
     * 
     * @param value
     *            the number of milliseconds to pause after operation
     */
    void setSpeed(long speedInMilis);

    /**
     * Get execution speed (i.e., get the millisecond length of the delay following each selenium operation). By
     * default, there is no such delay, i.e., the delay is 0 milliseconds.
     * 
     * See also setSpeed.
     * 
     * @return the execution speed in milliseconds.
     */
    long getSpeed();

    /**
     * Check a toggle-button (checkbox/radio)
     * 
     * @param locator
     *            an element locator
     */
    void check(ElementLocator<?> locator);

    /**
     * Uncheck a toggle-button (checkbox/radio)
     * 
     * @param locator
     *            an element locator
     */
    void uncheck(ElementLocator<?> locator);

    /**
     * Select an option from a drop-down using an option locator.
     * 
     * <p>
     * Option locators provide different ways of specifying options of an HTML Select element (e.g. for selecting a
     * specific option, or for asserting that the selected option satisfies a specification). There are several forms of
     * Select Option Locator.
     * </p>
     * <ul>
     * <li><strong>label</strong>=<em>labelPattern</em>: matches options based on their labels, i.e. the visible text.
     * (This is the default.)
     * <ul class="first last simple">
     * <li>label=regexp:^[Oo]ther</li>
     * </ul>
     * </li>
     * <li><strong>value</strong>=<em>valuePattern</em>: matches options based on their values.
     * <ul class="first last simple">
     * <li>value=other</li>
     * </ul>
     * </li>
     * <li><strong>id</strong>=<em>id</em>:
     * 
     * matches options based on their ids.
     * <ul class="first last simple">
     * <li>id=option1</li>
     * </ul>
     * </li>
     * <li><strong>index</strong>=<em>index</em>: matches an option based on its index (offset from zero).
     * <ul class="first last simple">
     * <li>index=2</li>
     * </ul>
     * </li>
     * </ul>
     * <p>
     * If no option locator prefix is provided, the default behaviour is to match on <strong>label</strong>.
     * </p>
     * 
     * @param selectLocator
     *            an element locator identifying a drop-down menu
     * @param optionLocator
     *            an option locator (a label by default)
     */
    void select(ElementLocator<?> selectLocator, OptionLocator<?> optionLocator);

    /**
     * Add a selection to the set of selected options in a multi-select element using an option locator.
     * 
     * @see #doSelect for details of option locators
     * @param locator
     *            an element locator identifying a multi-select box
     * @param optionLocator
     *            an option locator (a label by default)
     */
    void addSelection(ElementLocator<?> locator, OptionLocator<?> optionLocator);

    /**
     * Remove a selection from the set of selected options in a multi-select element using an option locator.
     * 
     * @see #doSelect for details of option locators
     * @param locator
     *            an element locator identifying a multi-select box
     * @param optionLocator
     *            an option locator (a label by default)
     */
    void removeSelection(ElementLocator<?> locator, OptionLocator<?> optionLocator);

    /**
     * Unselects all of the selected options in a multi-select element.
     * 
     * @param locator
     *            an element locator identifying a multi-select box
     */
    void removeAllSelections(ElementLocator<?> locator);

    /**
     * Submit the specified form. This is particularly useful for forms without submit buttons, e.g. single-input
     * "Search" forms.
     * 
     * @param formLocator
     *            an element locator for the form you want to submit
     */
    void submit(ElementLocator<?> formLocator);

    /**
     * Opens an URL in the test frame. This accepts both relative and absolute URLs.
     * 
     * The "open" command waits for the page to load before proceeding, ie. the "AndWait" suffix is implicit.
     * 
     * <em>Note</em>: The URL must be on the same domain as the runner HTML due to security restrictions in the browser
     * (Same Origin Policy). If you need to open an URL on another domain, use the Selenium Server to start a new
     * browser session on that domain.
     * 
     * @param url
     *            the URL to open; may be relative or absolute
     */
    void open(URL url);

    /**
     * Selects the main window. Functionally equivalent to using <code>selectWindow()</code> and specifying no value for
     * <code>windowID</code>.
     */
    void deselectPopUp();

    /**
     * <p>
     * Selects a frame within the current window. (You may invoke this command multiple times to select nested frames.)
     * To select the parent frame, use {@link FrameLocator#PARENT}; to select the top frame, use
     * {@link FrameLocator#TOP}. You can also select a frame by its 0-based index number (construct own
     * {@link FrameLocator} using notation described in {@link com.thoughtworks.selenium.Selenium#selectFrame(String)}).
     * </p>
     * 
     * <p>
     * You may also use a DOM expression to identify the frame you want directly, like this:
     * <code>dom=frames["main"].frames["subframe"]</code> (construct own {@link FrameLocator} using notation described
     * in {@link com.thoughtworks.selenium.Selenium#selectFrame(String)}).
     * </p>
     * 
     * @param frameLocator
     *            an frame locator identifying a frame or iframe
     */
    void selectFrame(FrameLocator frameLocator);

    /**
     * <p>
     * By default, Selenium's overridden window.confirm() function will return true, as if the user had manually clicked
     * OK; after running this command, the next call to confirm() will return false, as if the user had clicked Cancel.
     * Selenium will then resume using the default behavior for future confirmations, automatically returning true (OK)
     * unless/until you explicitly call this command for each confirmation.
     * </p>
     * <p>
     * Take note - every time a confirmation comes up, you must consume it with a corresponding getConfirmation, or else
     * the next selenium operation will fail.
     * </p>
     */
    void chooseCancelOnNextConfirmation();

    /**
     * <p>
     * Undo the effect of calling chooseCancelOnNextConfirmation. Note that Selenium's overridden window.confirm()
     * function will normally automatically return true, as if the user had manually clicked OK, so you shouldn't need
     * to use this command unless for some reason you need to change your mind prior to the next confirmation. After any
     * confirmation, Selenium will resume using the default behavior for future confirmations, automatically returning
     * true (OK) unless/until you explicitly call chooseCancelOnNextConfirmation for each confirmation.
     * </p>
     * <p>
     * Take note - every time a confirmation comes up, you must consume it with a corresponding getConfirmation, or else
     * the next selenium operation will fail.
     * </p>
     */
    void chooseOkOnNextConfirmation();

    /**
     * Instructs Selenium to return the specified answer string in response to the next JavaScript prompt
     * [window.prompt()].
     * 
     * @param answer
     *            the answer to give in response to the prompt pop-up
     */
    void answerOnNextPrompt(String answer);

    /**
     * Simulates the user clicking the "back" button on their browser.
     */
    void goBack();

    /**
     * Simulates the user clicking the "Refresh" button on their browser.
     */
    void refresh();

    /**
     * Simulates the user clicking the "close" button in the titlebar of a popup window or tab.
     */
    void close();

    /**
     * Has an alert occurred?
     * 
     * <p>
     * This function never throws an exception
     * </p>
     * 
     * @return true if there is an alert
     */
    boolean isAlertPresent();

    /**
     * Has a prompt occurred?
     * 
     * <p>
     * This function never throws an exception
     * </p>
     * 
     * @return true if there is a pending prompt
     */
    boolean isPromptPresent();

    /**
     * Has confirm() been called?
     * 
     * <p>
     * This function never throws an exception
     * </p>
     * 
     * @return true if there is a pending confirmation
     */
    boolean isConfirmationPresent();

    /**
     * Retrieves the message of a JavaScript alert generated during the previous action, or fail if there were no
     * alerts.
     * 
     * <p>
     * Getting an alert has the same effect as manually clicking OK. If an alert is generated but you do not consume it
     * with getAlert, the next Selenium action will fail.
     * </p>
     * <p>
     * Under Selenium, JavaScript alerts will NOT pop up a visible alert dialog.
     * </p>
     * <p>
     * Selenium does NOT support JavaScript alerts that are generated in a page's onload() event handler. In this case a
     * visible dialog WILL be generated and Selenium will hang until someone manually clicks OK.
     * </p>
     * 
     * @return The message of the most recent JavaScript alert
     */
    String getAlert();

    /**
     * Retrieves the message of a JavaScript confirmation dialog generated during the previous action.
     * 
     * <p>
     * By default, the confirm function will return true, having the same effect as manually clicking OK. This can be
     * changed by prior execution of the chooseCancelOnNextConfirmation command.
     * </p>
     * <p>
     * If an confirmation is generated but you do not consume it with getConfirmation, the next Selenium action will
     * fail.
     * </p>
     * <p>
     * NOTE: under Selenium, JavaScript confirmations will NOT pop up a visible dialog.
     * </p>
     * <p>
     * NOTE: Selenium does NOT support JavaScript confirmations that are generated in a page's onload() event handler.
     * In this case a visible dialog WILL be generated and Selenium will hang until you manually click OK.
     * </p>
     * 
     * @return the message of the most recent JavaScript confirmation dialog
     */
    String getConfirmation();

    /**
     * Retrieves the message of a JavaScript question prompt dialog generated during the previous action.
     * 
     * <p>
     * Successful handling of the prompt requires prior execution of the answerOnNextPrompt command. If a prompt is
     * generated but you do not get/verify it, the next Selenium action will fail.
     * </p>
     * <p>
     * NOTE: under Selenium, JavaScript prompts will NOT pop up a visible dialog.
     * </p>
     * <p>
     * NOTE: Selenium does NOT support JavaScript prompts that are generated in a page's onload() event handler. In this
     * case a visible dialog WILL be generated and Selenium will hang until someone manually clicks OK.
     * </p>
     * 
     * @return the message of the most recent JavaScript question prompt
     */
    String getPrompt();

    /**
     * Gets the absolute URL of the current page.
     * 
     * @return the absolute URL of the current page
     */
    URL getLocation();

    /**
     * Gets the title of the current page.
     * 
     * @return the title of the current page
     */
    String getTitle();

    /**
     * Gets the entire text of the page.
     * 
     * @return the entire text of the page
     */
    String getBodyText();

    /**
     * Gets the (whitespace-trimmed) value of an input field (or anything else with a value parameter). For
     * checkbox/radio elements, the value will be "on" or "off" depending on whether the element is checked or not.
     * 
     * @param locator
     *            an element locator
     * @return the element value, or "on/off" for checkbox/radio elements
     */
    String getValue(ElementLocator<?> locator);

    /**
     * Gets the text of an element. This works for any element that contains text. This command uses either the
     * textContent (Mozilla-like browsers) or the innerText (IE-like browsers) of the element, which is the rendered
     * text shown to the user.
     * 
     * @param locator
     *            an element locator
     * @return the text of the element
     */
    String getText(ElementLocator<?> locator);

    /**
     * Briefly changes the backgroundColor of the specified element yellow. Useful for debugging.
     * 
     * @param locator
     *            an element locator
     */
    void highlight(ElementLocator<?> locator);

    /**
     * Gets the result of evaluating the specified JavaScript snippet. The snippet may have multiple lines, but only the
     * result of the last line will be returned.
     * 
     * <p>
     * Note that, by default, the snippet will run in the context of the "selenium" object itself, so <code>this</code>
     * will refer to the Selenium object. Use <code>window</code> to refer to the window of your application, e.g.
     * <code>window.document.getElementById('foo')</code>
     * </p>
     * <p>
     * If you need to use a locator to refer to a single element in your application page, you can use
     * <code>this.browserbot.findElement("id=foo")</code> where "id=foo" is your locator.
     * </p>
     * 
     * @param script
     *            the JavaScript snippet to run
     * @return the results of evaluating the snippet
     */
    String getEval(JavaScript script);

    /**
     * Gets whether a toggle-button (checkbox/radio) is checked. Fails if the specified element doesn't exist or isn't a
     * toggle-button.
     * 
     * @param locator
     *            an element locator pointing to a checkbox or radio button
     * @return true if the checkbox is checked, false otherwise
     */
    boolean isChecked(ElementLocator<?> locator);

    /**
     * Gets all option labels (visible text) for selected options in the specified select or multi-select element.
     * 
     * @param selectLocator
     *            an element locator identifying a drop-down menu
     * @return an array of all selected option labels in the specified select drop-down
     */
    List<String> getSelectedLabels(ElementLocator<?> selectLocator);

    /**
     * Gets option label (visible text) for selected option in the specified select element.
     * 
     * @param selectLocator
     *            an element locator identifying a drop-down menu
     * @return the selected option label in the specified select drop-down
     */
    String getSelectedLabel(ElementLocator<?> selectLocator);

    /**
     * Gets all option values (value attributes) for selected options in the specified select or multi-select element.
     * 
     * @param selectLocator
     *            an element locator identifying a drop-down menu
     * @return an array of all selected option values in the specified select drop-down
     */
    List<String> getSelectedValues(ElementLocator<?> selectLocator);

    /**
     * Gets option value (value attribute) for selected option in the specified select element.
     * 
     * @param selectLocator
     *            an element locator identifying a drop-down menu
     * @return the selected option value in the specified select drop-down
     */
    String getSelectedValue(ElementLocator<?> selectLocator);

    /**
     * Gets all option indexes (option number, starting at 0) for selected options in the specified select or
     * multi-select element.
     * 
     * @param selectLocator
     *            an element locator identifying a drop-down menu
     * @return an array of all selected option indexes in the specified select drop-down
     */
    List<Integer> getSelectedIndexes(ElementLocator<?> selectLocator);

    /**
     * Gets option index (option number, starting at 0) for selected option in the specified select element.
     * 
     * @param selectLocator
     *            an element locator identifying a drop-down menu
     * @return the selected option index in the specified select drop-down
     */
    int getSelectedIndex(ElementLocator<?> selectLocator);

    /**
     * Gets all option element IDs for selected options in the specified select or multi-select element.
     * 
     * @param selectLocator
     *            an element locator identifying a drop-down menu
     * @return an array of all selected option IDs in the specified select drop-down
     */
    List<String> getSelectedIds(ElementLocator<?> selectLocator);

    /**
     * Gets option element ID for selected option in the specified select element.
     * 
     * @param selectLocator
     *            an element locator identifying a drop-down menu
     * @return the selected option ID in the specified select drop-down
     */
    String getSelectedId(ElementLocator<?> selectLocator);

    /**
     * Determines whether some option in a drop-down menu is selected.
     * 
     * @param selectLocator
     *            an element locator identifying a drop-down menu
     * @return true if some option has been selected, false otherwise
     */
    boolean isSomethingSelected(ElementLocator<?> selectLocator);

    /**
     * Gets all option labels in the specified select drop-down.
     * 
     * @param selectLocator
     *            an element locator identifying a drop-down menu
     * @return an array of all option labels in the specified select drop-down
     */
    List<String> getSelectOptions(ElementLocator<?> selectLocator);

    /**
     * Gets the value of an element attribute. The value of the attribute may differ across browsers (this is the case
     * for the "style" attribute, for example).
     * 
     * @param attributeLocator
     *            attribute locator
     * @return the value of the specified attribute
     */
    String getAttribute(AttributeLocator<?> attributeLocator);

    /**
     * Verifies that the specified text pattern appears somewhere on the rendered page shown to the user.
     * 
     * @param text
     *            a pattern to match with the text of the page
     * @return true if the pattern matches the text, false otherwise
     */
    boolean isTextPresent(String text);

    /**
     * Verifies that the specified element is somewhere on the page.
     * 
     * @param locator
     *            an element locator
     * @return true if the element is present, false otherwise
     */
    boolean isElementPresent(ElementLocator<?> locator);

    /**
     * Determines if the specified element is visible. An element can be rendered invisible by setting the CSS
     * "visibility" property to "hidden", or the "display" property to "none", either for the element itself or one if
     * its ancestors. This method will fail if the element is not present.
     * 
     * @param locator
     *            an element locator
     * @return true if the specified element is visible, false otherwise
     */
    boolean isVisible(ElementLocator<?> locator);

    /**
     * Determines whether the specified input element is editable, ie hasn't been disabled. This method will fail if the
     * specified element isn't an input element.
     * 
     * @param locator
     *            an element locator
     * @return true if the input element is editable, false otherwise
     */
    boolean isEditable(ElementLocator<?> locator);

    /**
     * deprecated - use dragAndDrop instead
     * 
     * @param locator
     *            an element locator
     * @param movementsString
     *            offset in pixels from the current location to which the element should be moved
     */
    void dragdrop(ElementLocator<?> locator, Offset movementsString);

    /**
     * Configure the number of pixels between "mousemove" events during dragAndDrop commands (default=10).
     * <p>
     * Setting this value to 0 means that we'll send a "mousemove" event to every single pixel in between the start
     * location and the end location; that can be very slow, and may cause some browsers to force the JavaScript to
     * timeout.
     * </p>
     * <p>
     * If the mouse speed is greater than the distance between the two dragged objects, we'll just send one "mousemove"
     * at the start location and then one final one at the end location.
     * </p>
     * 
     * @param pixels
     *            the number of pixels between "mousemove" events
     */
    void setMouseSpeed(int pixels);

    /**
     * Returns the number of pixels between "mousemove" events during dragAndDrop commands (default=10).
     * 
     * @return the number of pixels between "mousemove" events during dragAndDrop commands (default=10)
     */
    int getMouseSpeed();

    /**
     * Drags an element a certain distance and then drops it
     * 
     * @param locator
     *            an element locator
     * @param movementsString
     *            offset in pixels from the current location to which the element should be moved, e.g., "+70,-300"
     */
    void dragAndDrop(ElementLocator<?> locator, Offset movementsString);

    /**
     * Drags an element and drops it on another element
     * 
     * @param locatorOfObjectToBeDragged
     *            an element to be dragged
     * @param locatorOfDragDestinationObject
     *            an element whose location (i.e., whose center-most pixel) will be the point where
     *            locatorOfObjectToBeDragged is dropped
     */
    void dragAndDropToObject(ElementLocator<?> locatorOfObjectToBeDragged,
        ElementLocator<?> locatorOfDragDestinationObject);

    /**
     * Gives focus to the currently selected window
     */
    void windowFocus();

    /**
     * Resize currently selected window to take up the entire screen
     */
    void windowMaximize();

    /**
     * Returns the entire HTML source between the opening and closing "html" tags.
     * 
     * @return the entire HTML source
     */
    String getHtmlSource();

    /**
     * Moves the text cursor to the specified position in the given input element or textarea. This method will fail if
     * the specified element isn't an input element or textarea.
     * 
     * @param locator
     *            an element locator pointing to an input element or textarea
     * @param position
     *            the numerical position of the cursor in the field; position should be 0 to move the position to the
     *            beginning of the field. You can also set the cursor to -1 to move it to the end of the field.
     */
    void setCursorPosition(ElementLocator<?> locator, int position);

    /**
     * Get the relative index of an element to its parent (starting from 0). The comment node and empty text node will
     * be ignored.
     * 
     * @param locator
     *            an element locator pointing to an element
     * @return of relative index of the element to its parent (starting from 0)
     */
    int getElementIndex(ElementLocator<?> locator);

    /**
     * Check if these two elements have same parent and are ordered siblings in the DOM. Two same elements will not be
     * considered ordered.
     * 
     * @param locator1
     *            an element locator pointing to the first element
     * @param locator2
     *            an element locator pointing to the second element
     * @return true if element1 is the previous sibling of element2, false otherwise
     */
    boolean isOrdered(ElementLocator<?> locator1, ElementLocator<?> locator2);

    /**
     * Retrieves the horizontal position of an element
     * 
     * @param locator
     *            an element locator pointing to an element
     * @return of pixels from the edge of the frame.
     */
    int getElementPositionLeft(ElementLocator<?> locator);

    /**
     * Retrieves the vertical position of an element
     * 
     * @param locator
     *            an element locator pointing to an element
     * @return of pixels from the edge of the frame.
     */
    int getElementPositionTop(ElementLocator<?> locator);

    /**
     * Retrieves the position of an element
     * 
     * @param locator
     * @return the position in the current frame
     */
    Point getElementPosition(ElementLocator<?> locator);

    /**
     * Retrieves the width of an element
     * 
     * @param locator
     *            an element locator pointing to an element
     * @return width of an element in pixels
     */
    int getElementWidth(ElementLocator<?> locator);

    /**
     * Retrieves the height of an element
     * 
     * @param locator
     *            an element locator pointing to an element
     * @return height of an element in pixels
     */
    int getElementHeight(ElementLocator<?> locator);

    /**
     * Retrievers the element dimensions (width, height)
     * 
     * @param locator
     *            an element locator pointing to an element
     * @return dimensions of an element in pixels
     */
    Dimension getElementDimension(ElementLocator<?> locator);

    /**
     * Retrieves the text cursor position in the given input element or textarea; beware, this may not work perfectly on
     * all browsers.
     * 
     * <p>
     * Specifically, if the cursor/selection has been cleared by JavaScript, this command will tend to return the
     * position of the last location of the cursor, even though the cursor is now gone from the page. This is filed as
     * <a href="http://jira.openqa.org/browse/SEL-243">SEL-243</a>.
     * </p>
     * This method will fail if the specified element isn't an input element or textarea, or there is no cursor in the
     * element.
     * 
     * @param locator
     *            an element locator pointing to an input element or textarea
     * @return the numerical position of the cursor in the field
     */
    int getCursorPosition(ElementLocator<?> locator);

    /**
     * Returns the number of elements that match the specified locator.
     * 
     * @param locator
     * 
     * @return the number of nodes that match the specified locator
     */
    int getCount(IterableLocator<?> locator);

    /**
     * Temporarily sets the "id" attribute of the specified element, so you can locate it in the future using its ID
     * rather than a slow/complicated XPath. This ID will disappear once the page is reloaded.
     * 
     * @param locator
     *            an element locator pointing to an element
     * @param identifier
     *            a string to be used as the ID of the specified element
     */
    IdLocator assignId(ElementLocator<?> locator, String identifier);

    /**
     * Specifies whether Selenium should use the native in-browser implementation of XPath (if any native version is
     * available); if you pass "false" to this function, we will always use our pure-JavaScript xpath library. Using the
     * pure-JS xpath library can improve the consistency of xpath element locators between different browser vendors,
     * but the pure-JS version is much slower than the native implementations.
     * 
     * @param allow
     *            boolean, true means we'll prefer to use native XPath; false means we'll only use JS XPath
     */
    void allowNativeXpath(boolean allow);

    /**
     * Specifies whether Selenium will ignore xpath attributes that have no value, i.e. are the empty string, when using
     * the non-native xpath evaluation engine. You'd want to do this for performance reasons in IE. However, this could
     * break certain xpaths, for example an xpath that looks for an attribute whose value is NOT the empty string.
     * 
     * The hope is that such xpaths are relatively rare, but the user should have the option of using them. Note that
     * this only influences xpath evaluation when using the ajaxslt engine (i.e. not "javascript-xpath").
     * 
     * @param ignore
     *            boolean, true means we'll ignore attributes without value at the expense of xpath "correctness"; false
     *            means we'll sacrifice speed for correctness.
     */
    void ignoreAttributesWithoutValue(boolean ignore);

    /**
     * Runs the specified JavaScript snippet repeatedly until it evaluates to "true". The snippet may have multiple
     * lines, but only the result of the last line will be considered.
     * 
     * <p>
     * Note that, by default, the snippet will be run in the runner's test window, not in the window of your
     * application. To get the window of your application, you can use the JavaScript snippet
     * <code>selenium.browserbot.getCurrentWindow()</code>, and then run your JavaScript in there
     * </p>
     * 
     * <p>
     * Wait default timeout specified in
     * {@link org.jboss.test.selenium.SystemProperties#getSeleniumTimeout
     * (org.jboss.test.selenium.SystemProperties.SeleniumTimeoutType)}
     * of type {@link SystemProperties.SeleniumTimeoutType#DEFAULT}.
     * </p>
     * 
     * @param script
     *            the JavaScript snippet to run
     */
    void waitForCondition(JavaScript script);

    /**
     * Runs the specified JavaScript snippet repeatedly until it evaluates to "true". The snippet may have multiple
     * lines, but only the result of the last line will be considered.
     * 
     * <p>
     * Note that, by default, the snippet will be run in the runner's test window, not in the window of your
     * application. To get the window of your application, you can use the JavaScript snippet
     * <code>selenium.browserbot.getCurrentWindow()</code>, and then run your JavaScript in there
     * </p>
     * 
     * @param script
     *            the JavaScript snippet to run
     * @param timeout
     *            a timeout in milliseconds, after which this command will return with an error
     */
    void waitForCondition(JavaScript script, long timeout);

    /**
     * Specifies the amount of time that Selenium will wait for actions to complete.
     * 
     * <p>
     * Actions that require waiting include "open" and the "waitFor*" actions.
     * </p>
     * The default timeout is 30 seconds.
     * 
     * @param timeout
     *            a timeout in milliseconds, after which the action will return with an error
     */
    void setTimeout(long timeout);

    /**
     * Waits for a new page to load.
     * 
     * <p>
     * You can use this command instead of the "AndWait" suffixes, "clickAndWait", "selectAndWait", "typeAndWait" etc.
     * (which are only available in the JS API).
     * </p>
     * <p>
     * Selenium constantly keeps track of new pages loading, and sets a "newPageLoaded" flag when it first notices a
     * page load. Running any other Selenium command after turns the flag to false. Hence, if you want to wait for a
     * page to load, you must wait immediately after a Selenium command that caused a page-load.
     * </p>
     * <p>
     * Wait default timeout specified in
     * {@link org.jboss.test.selenium.SystemProperties#getSeleniumTimeout
     * (org.jboss.test.selenium.SystemProperties.SeleniumTimeoutType)}
     * of type {@link org.jboss.test.selenium.SystemProperties.SeleniumTimeoutType#DEFAULT}.
     * </p>
     */
    void waitForPageToLoad();

    /**
     * Waits for a new page to load.
     * 
     * <p>
     * You can use this command instead of the "AndWait" suffixes, "clickAndWait", "selectAndWait", "typeAndWait" etc.
     * (which are only available in the JS API).
     * </p>
     * <p>
     * Selenium constantly keeps track of new pages loading, and sets a "newPageLoaded" flag when it first notices a
     * page load. Running any other Selenium command after turns the flag to false. Hence, if you want to wait for a
     * page to load, you must wait immediately after a Selenium command that caused a page-load.
     * </p>
     * 
     * @param timeout
     *            a timeout in milliseconds, after which this command will return with an error
     */
    void waitForPageToLoad(long timeout);

    /**
     * Waits for a new frame to load.
     * 
     * <p>
     * Selenium constantly keeps track of new pages and frames loading, and sets a "newPageLoaded" flag when it first
     * notices a page load.
     * </p>
     * 
     * <p>
     * See waitForPageToLoad for more information.
     * </p>
     * 
     * <p>
     * Wait default timeout specified in
     * {@link org.jboss.test.selenium.SystemProperties#getSeleniumTimeout
     * (org.jboss.test.selenium.SystemProperties.SeleniumTimeoutType)}
     * of type {@link org.jboss.test.selenium.SystemProperties.SeleniumTimeoutType#DEFAULT}.
     * </p>
     * 
     * @param frameAddress
     *            FrameAddress from the server side
     */
    void waitForFrameToLoad(URL frameAddress);
    
    /**
     * Waits for a new frame to load.
     * 
     * <p>
     * Selenium constantly keeps track of new pages and frames loading, and sets a "newPageLoaded" flag when it first
     * notices a page load.
     * </p>
     * 
     * See waitForPageToLoad for more information.
     * 
     * @param frameAddress
     *            FrameAddress from the server side
     * @param timeout
     *            a timeout in milliseconds, after which this command will return with an error
     */
    void waitForFrameToLoad(URL frameAddress, long timeout);

    /**
     * <p>Return all cookies of the current page under test.</p>
     * 
     * <p><b>Currently unsupported</b></p>
     * 
     * @return all cookies of the current page under test
     */
    Set<Cookie> getAllCookies();

    /**
     * Returns the value of the cookie with the specified name, or throws an error if the cookie is not present.
     * 
     * @param cookieName
     *            the name of the cookie
     * @return the value of the cookie
     */
    Cookie getCookieByName(String cookieName);

    /**
     * Returns true if a cookie with the specified name is present, or false otherwise.
     * 
     * @param cookieName
     *            the name of the cookie
     * @return true if a cookie with the specified name is present, or false otherwise.
     */
    boolean isCookiePresent(String cookieName);

    /**
     * Create a new cookie whose path and domain are same with those of current page under test.
     * 
     * @param cookie
     *            the cookie to be created
     */
    void createCookie(Cookie cookie);
    
    /**
     * Create a new cookie whose path and domain are same with those of current page under test, unless you specified a
     * path for this cookie explicitly in options.
     * 
     * @param cookie
     *            the cookie to be created
     * @param options
     *            options for the cookie. Currently supported options include 'path', 'max_age' and 'domain'. the
     *            optionsString's format is "path=/path/, max_age=60, domain=.foo.com". The order of options are
     *            irrelevant, the unit of the value of 'max_age' is second. Note that specifying a domain that isn't a
     *            subset of the current domain will usually fail.
     */
    void createCookie(Cookie cookie, CreateCookieOptions options);

    /**
     * Delete a named cookie with specified options. Be careful; to delete a cookie, you need to delete it using
     * the exact same path and domain that were used to create the cookie. If the path is wrong, or the domain is wrong,
     * the cookie simply won't be deleted. Also note that specifying a domain that isn't a subset of the current domain
     * will usually fail.
     * 
     * Since there's no way to discover at runtime the original path and domain of a given cookie, we've added an option
     * called 'recurse' to try all sub-domains of the current domain with all paths that are a subset of the current
     * path. Beware; this option can be slow. In big-O notation, it operates in O(n*m) time, where n is the number of
     * dots in the domain name and m is the number of slashes in the path.
     * 
     * @param cookieName
     *            the name of the cookie to be deleted
     * @param options
     *            options for the cookie. Currently supported options include 'path', 'domain' and 'recurse.' The
     *            optionsString's format is "path=/path/, domain=.foo.com, recurse=true". The order of options are
     *            irrelevant. Note that specifying a domain that isn't a subset of the current domain will usually fail.
     */
    void deleteCookie(String cookieName, DeleteCookieOptions options);

    /**
     * Calls deleteCookie with recurse=true on all cookies visible to the current page. As noted on the documentation
     * for deleteCookie, recurse=true can be much slower than simply deleting the cookies using a known domain/path.
     */
    void deleteAllVisibleCookies();

    /**
     * Sets the threshold for browser-side logging messages; log messages beneath this threshold will be discarded.
     * Valid logLevel strings are: "debug", "info", "warn", "error" or "off". To see the browser logs, you need to
     * either show the log window in GUI mode, or enable browser-side logging in Selenium RC.
     * 
     * @param logLevel
     *            one of the following: "debug", "info", "warn", "error" or "off"
     */
    void setBrowserLogLevel(LogLevel logLevel);

    /**
     * Creates a new "script" tag in the body of the current test window, and adds the specified text into the body of
     * the command. Scripts run in this way can often be debugged more easily than scripts executed using Selenium's
     * "getEval" command. Beware that JS exceptions thrown in these script tags aren't managed by Selenium, so you
     * should probably wrap your script in try/catch blocks if there is any chance that the script will throw an
     * exception.
     * 
     * @param script
     *            the JavaScript snippet to run
     */
    void runScript(JavaScript script);

    /**
     * Defines a new function for Selenium to locate elements on the page. For example, if you define the strategy
     * "foo", and someone runs click("foo=blah"), we'll run your function, passing you the string "blah", and click on
     * the element that your function returns, or throw an "Element not found" error if your function returns null.
     * 
     * We'll pass three arguments to your function:
     * <ul>
     * <li>locator: the string the user passed in</li>
     * <li>inWindow: the currently selected window</li>
     * <li>inDocument: the currently selected document</li>
     * </ul>
     * The function must return null if the element can't be found.
     * 
     * @param strategyName
     *            the name of the strategy to define; this should use only letters [a-zA-Z] with no spaces or other
     *            punctuation.
     * @param functionDefinition
     *            a string defining the body of a function in JavaScript. For example:
     *            <code>return inDocument.getElementById(locator);</code>
     */
    void addLocationStrategy(ElementLocationStrategy strategyName, JavaScript functionDefinition);

    /**
     * Loads script content into a new script tag in the Selenium document. This differs from the runScript command in
     * that runScript adds the script tag to the document of the AUT, not the Selenium document. The following entities
     * in the script content are replaced by the characters they represent:
     * 
     * &lt; &gt; &amp;
     * 
     * The corresponding remove command is removeScript.
     * 
     * @param javaScript
     *            the JavaScript script to add
     */
    void addScript(JavaScript javaScript);

    /**
     * Checks whenever the script is already added.
     * 
     * Check is based on JavaScript's id (see {@link JavaScript#getIdentification()}.
     * 
     * @param javaScript
     *            JavaScript we want to check if it is added
     * @return true if the javaScript is already added; false otherwise
     */
    boolean containsScript(JavaScript javaScript);

    /**
     * Removes a script tag from the Selenium document identified by the JavaScript's id (see
     * {@link JavaScript#getIdentification()}. Does nothing if the referenced tag doesn't exist.
     * 
     * @param javaScript
     *            JavaScript we want to remove
     */
    void removeScript(JavaScript javaScript);

    /**
     * Allows choice of one of the available libraries.
     * 
     * @param xpathLibrary
     *            name of the desired library Only the following three can be chosen:
     *            <ul>
     *            <li>"ajaxslt" - Google's library</li>
     *            <li>"javascript-xpath" - Cybozu Labs' faster library</li>
     *            <li>"default" - The default library. Currently the default library is "ajaxslt" .</li>
     *            </ul>
     *            If libraryName isn't one of these three, then no change will be made.
     */
    void useXpathLibrary(XpathLibrary xpathLibrary);

    /**
     * Writes a message to the status bar and adds a note to the browser-side log.
     * 
     * @param context
     *            the message to be sent to the browser
     */
    void logToBrowser(String context);

    /**
     * Kills the running Selenium Server and all browser sessions. After you run this command, you will no longer be
     * able to send commands to the server; you can't remotely start the server once it has been stopped. Normally you
     * should prefer to run the "stop" command, which terminates the current browser session, rather than shutting down
     * the entire server.
     */
    void shutDownSeleniumServer();

    /**
     * Retrieve the last messages logged on a specific remote control. Useful for error reports, especially when running
     * multiple remote controls in a distributed environment. The maximum number of log messages that can be retrieve is
     * configured on remote control startup.
     * 
     * @return The last N log messages as a multi-line string.
     */
    String retrieveLastRemoteControlLogs();

    /**
     * Simulates a user pressing a key (without releasing it yet) by sending a native operating system keystroke. This
     * function uses the java.awt.Robot class to send a keystroke; this more accurately simulates typing a key on the
     * keyboard. It does not honor settings from the shiftKeyDown, controlKeyDown, altKeyDown and metaKeyDown commands,
     * and does not target any particular HTML element. To send a keystroke to a particular element, focus on the
     * element first before running this command.
     * 
     * @param keycode
     *            an integer keycode number corresponding to a java.awt.event.KeyEvent; note that Java keycodes are NOT
     *            the same thing as JavaScript keycodes!
     */
    void keyDownNative(String keycode);

    /**
     * Simulates a user releasing a key by sending a native operating system keystroke. This function uses the
     * java.awt.Robot class to send a keystroke; this more accurately simulates typing a key on the keyboard. It does
     * not honor settings from the shiftKeyDown, controlKeyDown, altKeyDown and metaKeyDown commands, and does not
     * target any particular HTML element. To send a keystroke to a particular element, focus on the element first
     * before running this command.
     * 
     * @param keycode
     *            an integer keycode number corresponding to a java.awt.event.KeyEvent; note that Java keycodes are NOT
     *            the same thing as JavaScript keycodes!
     */
    void keyUpNative(String keycode);

    /**
     * Simulates a user pressing and releasing a key by sending a native operating system keystroke. This function uses
     * the java.awt.Robot class to send a keystroke; this more accurately simulates typing a key on the keyboard. It
     * does not honor settings from the shiftKeyDown, controlKeyDown, altKeyDown and metaKeyDown commands, and does not
     * target any particular HTML element. To send a keystroke to a particular element, focus on the element first
     * before running this command.
     * 
     * @param keycode
     *            an integer keycode number corresponding to a java.awt.event.KeyEvent; note that Java keycodes are NOT
     *            the same thing as JavaScript keycodes!
     */
    void keyPressNative(String keycode);
    
    /**
     * Capture a PNG screenshot. It then returns the file as a base 64 encoded string.
     * 
     * @return The BufferedImage
     */
    BufferedImage captureScreenshot();
    
    /**
     * Downloads a screenshot of the browser current window canvas to a based 64 encoded PNG file. The <em>entire</em>
     * windows canvas is captured, including parts rendered outside of the current view port.
     * 
     * Currently this only works in Mozilla and when running in chrome mode.
     * 
     * @return The BufferedImage
     */
    BufferedImage captureEntirePageScreenshot();
    
    /**
     * Returns the network traffic seen by the browser, including headers, AJAX requests, status codes, and timings.
     * When this function is called, the traffic log is cleared, so the returned content is only the traffic seen since
     * the last call.
     * 
     * @param type
     *            The type of data to return the network traffic as. Valid values are: json, xml, or plain.
     * @return A string representation in the defined type of the network traffic seen by the browser.
     */
    NetworkTraffic captureNetworkTraffic(NetworkTrafficType type);

}