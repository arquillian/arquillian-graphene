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
package org.jboss.test.selenium.framework.internal;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.List;

import org.jboss.test.selenium.encapsulated.Cookie;
import org.jboss.test.selenium.encapsulated.CookieParameters;
import org.jboss.test.selenium.encapsulated.Frame;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.encapsulated.Kwargs;
import org.jboss.test.selenium.encapsulated.NetworkTraffic;
import org.jboss.test.selenium.encapsulated.NetworkTrafficType;
import org.jboss.test.selenium.encapsulated.Window;
import org.jboss.test.selenium.encapsulated.WindowId;
import org.jboss.test.selenium.locator.Attribute;
import org.jboss.test.selenium.locator.ElementLocator;

/**
 * Unsupported methods from Selenium API didn't exposed to TypedSelenium
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface UnsupportedTypedSelenium {
    /**
     * Tells the Selenium server to add the specificed key and value as a custom outgoing request header. This only
     * works if the browser is configured to use the built in Selenium proxy.
     * 
     * @param key
     *            the header name.
     * @param value
     *            the header value.
     */
    void addCustomRequestHeader(String key, String value);

    /**
     * Sets a file input (upload) field to the file listed in fileLocator
     * 
     * @param fieldLocator
     *            an element locator
     * @param fileLocator
     *            a URL pointing to the specified file. Before the file can be set in the input field (fieldLocator),
     *            Selenium RC may need to transfer the file to the local machine before attaching the file in a web page
     *            form. This is common in selenium grid configurations where the RC server driving the browser is not
     *            the same machine that started the test. Supported Browsers: Firefox ("*chrome") only.
     */
    void attachFile(ElementLocator fieldLocator, File fileLocator);

    /**
     * Sets a file input (upload) field to the file listed in fileLocator
     * 
     * @param fieldLocator
     *            an element locator
     * @param fileLocator
     *            a URL pointing to the specified file. Before the file can be set in the input field (fieldLocator),
     *            Selenium RC may need to transfer the file to the local machine before attaching the file in a web page
     *            form. This is common in selenium grid configurations where the RC server driving the browser is not
     *            the same machine that started the test. Supported Browsers: Firefox ("*chrome") only.
     */
    @Deprecated
    void attachFile(ElementLocator fieldLocator, URL fileLocator);

    /**
     * Captures a PNG screenshot to the specified file.
     * 
     * @param filename
     *            the absolute path to the file to be written, e.g. "c:\blah\screenshot.png"
     */
    void captureScreenshot(File filename);

    /**
     * Capture a PNG screenshot. It then returns the file as a base 64 encoded string.
     * 
     * @return The BufferedImage
     */
    BufferedImage captureScreenshot();

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

    /**
     * Downloads a screenshot of the browser current window canvas to a based 64 encoded PNG file. The <em>entire</em>
     * windows canvas is captured, including parts rendered outside of the current view port.
     * 
     * Currently this only works in Mozilla and when running in chrome mode.
     * 
     * @param kwargs
     *            A kwargs string that modifies the way the screenshot is captured. Example: "background=#CCFFDD". This
     *            may be useful to set for capturing screenshots of less-than-ideal layouts, for example where absolute
     *            positioning causes the calculation of the canvas dimension to fail and a black background is exposed
     *            (possibly obscuring black text).
     * @return The BufferedImage
     */
    BufferedImage captureEntirePageScreenshot(Kwargs kwargs);

    /**
     * Saves the entire contents of the current window canvas to a PNG file. Contrast this with the captureScreenshot
     * command, which captures the contents of the OS viewport (i.e. whatever is currently being displayed on the
     * monitor), and is implemented in the RC only. Currently this only works in Firefox when running in chrome mode,
     * and in IE non-HTA using the EXPERIMENTAL "Snapsie" utility. The Firefox implementation is mostly borrowed from
     * the Screengrab! Firefox extension. Please see http://www.screengrab.org and http://snapsie.sourceforge.net/ for
     * details.
     * 
     * @param filename
     *            the path to the file to persist the screenshot as. No filename extension will be appended by default.
     *            Directories will not be created if they do not exist, and an exception will be thrown, possibly by
     *            native code.
     * @param kwargs
     *            a kwargs string that modifies the way the screenshot is captured. Example: "background=#CCFFDD" .
     *            Currently valid options:
     *            <dl>
     *            <dt>background</dt>
     *            <dd>the background CSS for the HTML document. This may be useful to set for capturing screenshots of
     *            less-than-ideal layouts, for example where absolute positioning causes the calculation of the canvas
     *            dimension to fail and a black background is exposed (possibly obscuring black text).</dd>
     *            </dl>
     */
    void captureEntirePageScreenshot(File filename, Kwargs kwargs);

    /**
     * Return all cookies of the current page under test.
     * 
     * @return all cookies of the current page under test
     */
    List<Cookie> getCookie();

    /**
     * Returns the value of the cookie with the specified name, or throws an error if the cookie is not present.
     * 
     * @param name
     *            the name of the cookie
     * @return the value of the cookie
     */
    Cookie getCookieByName(Cookie name);

    /**
     * Returns true if a cookie with the specified name is present, or false otherwise.
     * 
     * @param name
     *            the name of the cookie
     * @return true if a cookie with the specified name is present, or false otherwise.
     */
    boolean isCookiePresent(Cookie name);

    /**
     * Create a new cookie whose path and domain are same with those of current page under test, unless you specified a
     * path for this cookie explicitly.
     * 
     * @param nameValuePair
     *            name and value of the cookie in a format "name=value"
     * @param optionsString
     *            options for the cookie. Currently supported options include 'path', 'max_age' and 'domain'. the
     *            optionsString's format is "path=/path/, max_age=60, domain=.foo.com". The order of options are
     *            irrelevant, the unit of the value of 'max_age' is second. Note that specifying a domain that isn't a
     *            subset of the current domain will usually fail.
     */
    void createCookie(Cookie cookie, CookieParameters parameters);

    /**
     * Delete a named cookie with specified path and domain. Be careful; to delete a cookie, you need to delete it using
     * the exact same path and domain that were used to create the cookie. If the path is wrong, or the domain is wrong,
     * the cookie simply won't be deleted. Also note that specifying a domain that isn't a subset of the current domain
     * will usually fail.
     * 
     * Since there's no way to discover at runtime the original path and domain of a given cookie, we've added an option
     * called 'recurse' to try all sub-domains of the current domain with all paths that are a subset of the current
     * path. Beware; this option can be slow. In big-O notation, it operates in O(n*m) time, where n is the number of
     * dots in the domain name and m is the number of slashes in the path.
     * 
     * @param name
     *            the name of the cookie to be deleted
     * @param optionsString
     *            options for the cookie. Currently supported options include 'path', 'domain' and 'recurse.' The
     *            optionsString's format is "path=/path/, domain=.foo.com, recurse=true". The order of options are
     *            irrelevant. Note that specifying a domain that isn't a subset of the current domain will usually fail.
     */
    void deleteCookie(Cookie cookie, CookieParameters parameters);

    /**
     * Returns the IDs of all buttons on the page.
     * 
     * <p>
     * If a given button has no ID, it will appear as "" in this array.
     * </p>
     * 
     * @return the IDs of all buttons on the page
     */
    List<ElementLocator> getAllButtons();

    /**
     * Returns the IDs of all links on the page.
     * 
     * <p>
     * If a given link has no ID, it will appear as "" in this array.
     * </p>
     * 
     * @return the IDs of all links on the page
     */
    List<ElementLocator> getAllLinks();

    /**
     * Returns the IDs of all input fields on the page.
     * 
     * <p>
     * If a given field has no ID, it will appear as "" in this array.
     * </p>
     * 
     * @return the IDs of all field on the page
     */
    List<ElementLocator> getAllFields();

    /**
     * Returns the IDs of all windows that the browser knows about.
     * 
     * @return the IDs of all windows that the browser knows about.
     */
    List<WindowId> getAllWindowIds();

    /**
     * Returns the names of all windows that the browser knows about.
     * 
     * @return the names of all windows that the browser knows about.
     */
    List<String> getAllWindowNames();

    /**
     * Returns the titles of all windows that the browser knows about.
     * 
     * @return the titles of all windows that the browser knows about.
     */
    List<String> getAllWindowTitles();

    /**
     * Returns every instance of some attribute from all known windows.
     * 
     * @param attributeName
     *            name of an attribute on the windows
     * @return the set of values of this attribute from all known windows.
     */
    List<String> getAttributeFromAllWindows(Attribute attribute);

    /**
     * Returns the specified expression.
     * 
     * <p>
     * This is useful because of JavaScript preprocessing. It is used to generate commands like assertExpression and
     * waitForExpression.
     * </p>
     * 
     * @param expression
     *            the value to return
     * @return the value passed in
     */
    JavaScript getExpression(JavaScript expression);

    /**
     * Determine whether current/locator identify the frame containing this running code.
     * 
     * <p>
     * This is useful in proxy injection mode, where this code runs in every browser frame and window, and sometimes the
     * selenium server needs to identify the "current" frame. In this case, when the test calls selectFrame, this
     * routine is called for each frame to figure out which one has been selected. The selected frame will return true,
     * while all others will return false.
     * </p>
     * 
     * @param currentFrameString
     *            starting frame
     * @param target
     *            new frame (which might be relative to the current one)
     * @return true if the new frame is this code's window
     */
    boolean getWhetherThisFrameMatchFrameExpression(Frame currentFrame, Frame targetFrame);

    /**
     * Determine whether currentWindowString plus target identify the window containing this running code.
     * 
     * <p>
     * This is useful in proxy injection mode, where this code runs in every browser frame and window, and sometimes the
     * selenium server needs to identify the "current" window. In this case, when the test calls selectWindow, this
     * routine is called for each window to figure out which one has been selected. The selected window will return
     * true, while all others will return false.
     * </p>
     * 
     * @param currentWindowString
     *            starting window
     * @param target
     *            new window (which might be relative to the current one, e.g., "_parent")
     * @return true if the new window is this code's window
     */
    boolean getWhetherThisWindowMatchWindowExpression(Window currentWindowString, Window target);

    /**
     * Opens a popup window (if a window with that ID isn't already open). After opening the window, you'll need to
     * select it using the selectWindow command.
     * 
     * <p>
     * This command can also be a useful workaround for bug SEL-339. In some cases, Selenium will be unable to intercept
     * a call to window.open (if the call occurs during or before the "onLoad" event, for example). In those cases, you
     * can force Selenium to notice the open window's name by using the Selenium openWindow command, using an empty
     * (blank) url, like this: openWindow("", "myFunnyWindow").
     * </p>
     * 
     * @param url
     *            the URL to open, which can be blank
     * @param windowID
     *            the JavaScript window ID of the window to select
     */
    void openWindow(URL url, WindowId windowID);

    /**
     * Simplifies the process of selecting a popup window (and does not offer functionality beyond what
     * <code>selectWindow()</code> already provides).
     * <ul>
     * <li>If <code>windowID</code> is either not specified, or specified as "null", the first non-top window is
     * selected. The top window is the one that would be selected by <code>selectWindow()</code> without providing a
     * <code>windowID</code> . This should not be used when more than one popup window is in play.</li>
     * <li>Otherwise, the window will be looked up considering <code>windowID</code> as the following in order: 1) the
     * "name" of the window, as specified to <code>window.open()</code>; 2) a javascript variable which is a reference
     * to a window; and 3) the title of the window. This is the same ordered lookup performed by
     * <code>selectWindow</code> .</li>
     * </ul>
     * 
     * @param windowID
     *            an identifier for the popup window, which can take on a number of different meanings
     */
    void selectPopUp(WindowId windowID);

    /**
     * Selects a popup window using a window locator; once a popup window has been selected, all commands go to that
     * window. To select the main window again, use null as the target.
     * 
     * <p>
     * 
     * Window locators provide different ways of specifying the window object: by title, by internal JavaScript "name,"
     * or by JavaScript variable.
     * </p>
     * <ul>
     * <li><strong>title</strong>=<em>My Special Window</em>: Finds the window using the text that appears in the title
     * bar. Be careful; two windows can share the same title. If that happens, this locator will just pick one.</li>
     * <li><strong>name</strong>=<em>myWindow</em>: Finds the window using its internal JavaScript "name" property. This
     * is the second parameter "windowName" passed to the JavaScript method window.open(url, windowName, windowFeatures,
     * replaceFlag) (which Selenium intercepts).</li>
     * <li><strong>var</strong>=<em>variableName</em>: Some pop-up windows are unnamed (anonymous), but are associated
     * with a JavaScript variable name in the current application window, e.g. "window.foo = window.open(url);". In
     * those cases, you can open the window using "var=foo".</li>
     * </ul>
     * <p>
     * If no window locator prefix is provided, we'll try to guess what you mean like this:
     * </p>
     * <p>
     * 1.) if windowID is null, (or the string "null") then it is assumed the user is referring to the original window
     * instantiated by the browser).
     * </p>
     * <p>
     * 2.) if the value of the "windowID" parameter is a JavaScript variable name in the current application window,
     * then it is assumed that this variable contains the return value from a call to the JavaScript window.open()
     * method.
     * </p>
     * <p>
     * 3.) Otherwise, selenium looks in a hash it maintains that maps string names to window "names".
     * </p>
     * <p>
     * 4.) If <em>that</em> fails, we'll try looping over all of the known windows to try to find the appropriate
     * "title". Since "title" is not necessarily unique, this may have unexpected behavior.
     * </p>
     * <p>
     * If you're having trouble figuring out the name of a window that you want to manipulate, look at the Selenium log
     * messages which identify the names of windows created via window.open (and therefore intercepted by Selenium). You
     * will see messages like the following for each window as it is opened:
     * </p>
     * <p>
     * <code>debug: window.open call intercepted; window ID (which you can use with selectWindow()) is
     * "myNewWindow"</code>
     * </p>
     * <p>
     * In some cases, Selenium will be unable to intercept a call to window.open (if the call occurs during or before
     * the "onLoad" event, for example). (This is bug SEL-339.) In those cases, you can force Selenium to notice the
     * open window's name by using the Selenium openWindow command, using an empty (blank) url, like this:
     * openWindow("", "myFunnyWindow").
     * </p>
     * 
     * @param windowID
     *            the JavaScript window ID of the window to select
     */
    void selectWindow(WindowId windowID);

    /** Sets the per-session extension Javascript */
    void setExtensionJs(JavaScript extensionJs);

    /**
     * Waits for a popup window to appear and load up.
     * 
     * @param windowID
     *            the JavaScript window "name" of the window that will appear (not the text of the title bar) If
     *            unspecified, or specified as "null", this command will wait for the first non-top window to appear
     *            (don't rely on this if you are working with multiple popups simultaneously).
     * @param timeout
     *            a timeout in milliseconds, after which the action will return with an error. If this value is not
     *            specified, the default Selenium timeout will be used. See the setTimeout() command.
     */
    void waitForPopUp(WindowId windowId, long timeoutInMilis);
}
