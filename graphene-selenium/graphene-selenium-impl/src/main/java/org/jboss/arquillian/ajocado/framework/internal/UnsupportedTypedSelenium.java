/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.ajocado.framework.internal;

import java.io.File;
import java.net.URL;
import java.util.List;

import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;

/**
 * Unsupported methods from Selenium API didn't exposed to TypedSelenium
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface UnsupportedTypedSelenium {

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
    void attachFile(ElementLocator<?> fieldLocator, File fileLocator);

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
    void attachFile(ElementLocator<?> fieldLocator, URL fileLocator);

    /**
     * Captures a PNG screenshot to the specified file.
     *
     * @param filename
     *            the absolute path to the file to be written, e.g. "c:\blah\screenshot.png"
     */
    void captureScreenshot(File filename);

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
     */
    void captureEntirePageScreenshot(File filename);

    /**
     * Returns the IDs of all buttons on the page.
     *
     * <p>
     * If a given button has no ID, it will appear as "" in this array.
     * </p>
     *
     * @return the IDs of all buttons on the page
     */
    List<ElementLocator<?>> getAllButtons();

    /**
     * Returns the IDs of all links on the page.
     *
     * <p>
     * If a given link has no ID, it will appear as "" in this array.
     * </p>
     *
     * @return the IDs of all links on the page
     */
    List<ElementLocator<?>> getAllLinks();

    /**
     * Returns the IDs of all input fields on the page.
     *
     * <p>
     * If a given field has no ID, it will appear as "" in this array.
     * </p>
     *
     * @return the IDs of all field on the page
     */
    List<ElementLocator<?>> getAllFields();

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
     * @param attribute
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
    boolean getWhetherThisFrameMatchFrameExpression(String currentFrameString, String target);

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
    boolean getWhetherThisWindowMatchWindowExpression(String currentWindowString, String target);

    /**
     * Sets the per-session extension Javascript
     *
     * @param extensionJs
     */
    void setExtensionJs(JavaScript extensionJs);
}
