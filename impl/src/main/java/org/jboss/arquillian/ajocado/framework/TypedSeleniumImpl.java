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
package org.jboss.arquillian.ajocado.framework;

import static org.jboss.arquillian.ajocado.format.SimplifiedFormat.format;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.jboss.arquillian.ajocado.configuration.XPathLibrary;
import org.jboss.arquillian.ajocado.cookie.Cookie;
import org.jboss.arquillian.ajocado.cookie.CookieCreateOptions;
import org.jboss.arquillian.ajocado.cookie.CookieDeleteOptions;
import org.jboss.arquillian.ajocado.cookie.CookieOptions;
import org.jboss.arquillian.ajocado.dom.Attribute;
import org.jboss.arquillian.ajocado.dom.Event;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration.TimeoutType;
import org.jboss.arquillian.ajocado.framework.internal.UnsupportedTypedSelenium;
import org.jboss.arquillian.ajocado.geometry.Dimension;
import org.jboss.arquillian.ajocado.geometry.Offset;
import org.jboss.arquillian.ajocado.geometry.Point;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.javascript.KeyCode;
import org.jboss.arquillian.ajocado.locator.IdLocator;
import org.jboss.arquillian.ajocado.locator.attribute.AttributeLocator;
import org.jboss.arquillian.ajocado.locator.element.ElementLocationStrategy;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.locator.element.IterableLocator;
import org.jboss.arquillian.ajocado.locator.frame.FrameLocator;
import org.jboss.arquillian.ajocado.locator.option.OptionLocator;
import org.jboss.arquillian.ajocado.locator.window.WindowLocator;
import org.jboss.arquillian.ajocado.locator.window.WindowNameLocator;
import org.jboss.arquillian.ajocado.log.LogLevel;
import org.jboss.arquillian.ajocado.network.NetworkTraffic;
import org.jboss.arquillian.ajocado.network.NetworkTrafficType;
import org.jboss.arquillian.ajocado.request.RequestHeader;
import org.jboss.arquillian.ajocado.utils.array.ArrayTransform;

import com.thoughtworks.selenium.Selenium;

/**
 * Wrapper implementation for Selenium object's API to make it type-safe.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TypedSeleniumImpl implements TypedSelenium, UnsupportedTypedSelenium {

    Selenium selenium;

    AjocadoConfiguration configuration = AjocadoConfigurationContext.getProxy();

    private ArrayTransform<String, Integer> transformArrayOfStringToInteger = new ArrayTransform<String, Integer>(
        Integer.class) {
        public Integer transformation(String source) {
            return Integer.valueOf(source);
        }
    };

    public void addLocationStrategy(ElementLocationStrategy locationStrategy, JavaScript strategyDefinition) {
        selenium.addLocationStrategy(locationStrategy.getStrategyName(), strategyDefinition.toString());
    }

    public void addScript(JavaScript javaScript) {
        selenium.addScript(javaScript.getAsString(), javaScript.getIdentification());
    }

    public void addSelection(ElementLocator<?> elementLocator, OptionLocator<?> optionLocator) {
        selenium.addSelection(elementLocator.inSeleniumRepresentation(), optionLocator.inSeleniumRepresentation());
    }

    public void allowNativeXpath(boolean allow) {
        selenium.allowNativeXpath(String.valueOf(allow));
    }

    public void altKeyDown() {
        selenium.altKeyDown();
    }

    public void altKeyUp() {
        selenium.altKeyUp();
    }

    public void answerOnNextPrompt(String answer) {
        selenium.answerOnNextPrompt(answer);
    }

    public IdLocator assignId(ElementLocator<?> elementLocator, String identifier) {
        selenium.assignId(elementLocator.inSeleniumRepresentation(), identifier);
        return new IdLocator(identifier);
    }

    public void attachFile(ElementLocator<?> fieldLocator, File fileLocator) {
        throw new UnsupportedOperationException();
    }

    public void attachFile(ElementLocator<?> fieldLocator, URL fileLocator) {
        throw new UnsupportedOperationException();
    }

    public void captureEntirePageScreenshot(File filename) {
        throw new UnsupportedOperationException();
    }

    public BufferedImage captureEntirePageScreenshot() {
        return decodeBase64Screenshot(selenium.captureEntirePageScreenshotToString(""));
    }

    public NetworkTraffic captureNetworkTraffic(NetworkTrafficType type) {
        String traffic = selenium.captureNetworkTraffic(type.getType());
        return new NetworkTraffic(type, traffic);
    }

    public void captureScreenshot(File filename) {
        throw new UnsupportedOperationException();
    }

    public BufferedImage captureScreenshot() {
        return decodeBase64Screenshot(selenium.captureScreenshotToString());
    }

    public void check(ElementLocator<?> elementLocator) {
        selenium.check(elementLocator.inSeleniumRepresentation());
    }

    public void chooseCancelOnNextConfirmation() {
        selenium.chooseCancelOnNextConfirmation();
    }

    public void chooseOkOnNextConfirmation() {
        selenium.chooseOkOnNextConfirmation();
    }

    public void click(ElementLocator<?> elementLocator) {
        selenium.click(elementLocator.inSeleniumRepresentation());
    }

    public void clickAt(ElementLocator<?> elementLocator, Point point) {
        selenium.clickAt(elementLocator.inSeleniumRepresentation(), point.inSeleniumRepresentation());
    }

    public void close() {
        selenium.close();
    }

    public boolean containsScript(JavaScript javaScript) {
        final String identification = javaScript.getIdentification();
        String evaluated = selenium.getEval(format("document.getElementById('{0}') ? true : false", identification));
        return Boolean.valueOf(evaluated);
    }

    public void contextMenu(ElementLocator<?> elementLocator) {
        selenium.contextMenu(elementLocator.inSeleniumRepresentation());
    }

    public void contextMenuAt(ElementLocator<?> elementLocator, Point point) {
        selenium.contextMenuAt(elementLocator.inSeleniumRepresentation(), point.inSeleniumRepresentation());
    }

    public void controlKeyDown() {
        selenium.controlKeyDown();
    }

    public void controlKeyUp() {
        selenium.controlKeyUp();
    }

    public void deleteAllVisibleCookies() {
        selenium.deleteAllVisibleCookies();
    }

    public void deselectPopUp() {
        selenium.deselectPopUp();
    }

    public void doubleClick(ElementLocator<?> elementLocator) {
        selenium.doubleClick(elementLocator.inSeleniumRepresentation());
    }

    public void doubleClickAt(ElementLocator<?> elementLocator, Point point) {
        selenium.doubleClickAt(elementLocator.inSeleniumRepresentation(), point.inSeleniumRepresentation());
    }

    public void dragAndDrop(ElementLocator<?> elementLocator, Offset offset) {
        selenium.dragAndDrop(elementLocator.inSeleniumRepresentation(), offset.inSeleniumRepresentation());
    }

    public void dragAndDropToObject(ElementLocator<?> elementLocatorOfObjectToBeDragged,
        ElementLocator<?> elementLocatorOfDragDestinationObject) {
        selenium.dragAndDropToObject(elementLocatorOfDragDestinationObject.inSeleniumRepresentation(),
            elementLocatorOfObjectToBeDragged.inSeleniumRepresentation());
    }

    public void fireEvent(ElementLocator<?> elementLocator, Event event) {
        selenium.fireEvent(elementLocator.inSeleniumRepresentation(), event.getEventName());
    }

    public void focus(ElementLocator<?> elementLocator) {
        selenium.focus(elementLocator.inSeleniumRepresentation());
    }

    public String getAlert() {
        return selenium.getAlert();
    }

    public List<ElementLocator<?>> getAllButtons() {
        throw new UnsupportedOperationException();
    }

    public List<ElementLocator<?>> getAllFields() {
        throw new UnsupportedOperationException();
    }

    public List<ElementLocator<?>> getAllLinks() {
        throw new UnsupportedOperationException();
    }

    public List<WindowNameLocator> getAllWindowIds() {
        throw new UnsupportedOperationException();
    }

    public List<String> getAllWindowNames() {
        throw new UnsupportedOperationException();
    }

    public List<String> getAllWindowTitles() {
        throw new UnsupportedOperationException();
    }

    public String getAttribute(AttributeLocator<?> attributeLocator) {
        return selenium.getAttribute(attributeLocator.inSeleniumRepresentation());
    }

    public String getAttribute(ElementLocator<?> elementLocator, Attribute attribute) {
        return getAttribute(elementLocator.getAttribute(attribute));
    }

    public List<String> getAttributeFromAllWindows(Attribute attribute) {
        throw new UnsupportedOperationException();
    }

    public String getBodyText() {
        return selenium.getBodyText();
    }

    public String getConfirmation() {
        return selenium.getConfirmation();
    }

    public int getCount(IterableLocator<?> locator) {
        if (locator.getLocationStrategy() != ElementLocationStrategy.XPATH) {
            throw new UnsupportedOperationException("Only XPath locators are supported for counting");
        }
        return selenium.getXpathCount(locator.getRawLocator()).intValue();
    }

    public int getCursorPosition(ElementLocator<?> elementLocator) {
        return selenium.getCursorPosition(elementLocator.inSeleniumRepresentation()).intValue();
    }

    public Dimension getElementDimension(ElementLocator<?> elementLocator) {
        return new Dimension(getElementWidth(elementLocator), getElementHeight(elementLocator));
    }

    public int getElementHeight(ElementLocator<?> elementLocator) {
        return selenium.getElementHeight(elementLocator.inSeleniumRepresentation()).intValue();
    }

    public int getElementIndex(ElementLocator<?> elementLocator) {
        return selenium.getElementIndex(elementLocator.inSeleniumRepresentation()).intValue();
    }

    public Point getElementPosition(ElementLocator<?> elementLocator) {
        return new Point(getElementPositionLeft(elementLocator), getElementPositionTop(elementLocator));
    }

    public int getElementPositionLeft(ElementLocator<?> elementLocator) {
        return selenium.getElementPositionLeft(elementLocator.inSeleniumRepresentation()).intValue();
    }

    public int getElementPositionTop(ElementLocator<?> elementLocator) {
        return selenium.getElementPositionTop(elementLocator.inSeleniumRepresentation()).intValue();
    }

    public int getElementWidth(ElementLocator<?> elementLocator) {
        return selenium.getElementWidth(elementLocator.inSeleniumRepresentation()).intValue();

    }

    public String getEval(JavaScript script) {
        return selenium.getEval(script.toString());
    }

    public JavaScript getExpression(JavaScript expression) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public String getHtmlSource() {
        return selenium.getHtmlSource();
    }

    public URL getLocation() {
        try {
            return new URL(selenium.getLocation());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getMouseSpeed() {
        return selenium.getMouseSpeed().intValue();
    }

    public String getPrompt() {
        return selenium.getPrompt();
    }

    public List<String> getSelectOptions(ElementLocator<?> selectLocator) {
        return Arrays.asList(selenium.getSelectOptions(selectLocator.inSeleniumRepresentation()));
    }

    public String getSelectedId(ElementLocator<?> selectLocator) {
        return selenium.getSelectedId(selectLocator.inSeleniumRepresentation());
    }

    public List<String> getSelectedIds(ElementLocator<?> selectLocator) {
        return Arrays.asList(selenium.getSelectedIds(selectLocator.inSeleniumRepresentation()));
    }

    public int getSelectedIndex(ElementLocator<?> selectLocator) {
        return Integer.valueOf(selenium.getSelectedIndex(selectLocator.inSeleniumRepresentation()));
    }

    public List<Integer> getSelectedIndexes(ElementLocator<?> selectLocator) {
        return Arrays.asList(transformArrayOfStringToInteger.transform(selenium.getSelectedIndexes(selectLocator
            .inSeleniumRepresentation())));
    }

    public String getSelectedLabel(ElementLocator<?> selectLocator) {
        return selenium.getSelectedLabel(selectLocator.inSeleniumRepresentation());
    }

    public List<String> getSelectedLabels(ElementLocator<?> selectLocator) {
        return Arrays.asList(selenium.getSelectedLabels(selectLocator.inSeleniumRepresentation()));
    }

    public String getSelectedValue(ElementLocator<?> selectLocator) {
        return selenium.getSelectedValue(selenium.getSelectedValue(selectLocator.inSeleniumRepresentation()));
    }

    public List<String> getSelectedValues(ElementLocator<?> selectLocator) {
        return Arrays.asList(selenium.getSelectedValues(selectLocator.inSeleniumRepresentation()));
    }

    public long getSpeed() {
        return Long.valueOf(selenium.getSpeed());
    }

    public String getText(ElementLocator<?> elementLocator) {
        return selenium.getText(elementLocator.inSeleniumRepresentation());
    }

    public String getTitle() {
        return selenium.getTitle();
    }

    public String getValue(ElementLocator<?> elementLocator) {
        return selenium.getValue(elementLocator.inSeleniumRepresentation());
    }

    public boolean getWhetherThisFrameMatchFrameExpression(String currentFrameString, String target) {
        return selenium.getWhetherThisFrameMatchFrameExpression(currentFrameString, target);
    }

    public boolean getWhetherThisWindowMatchWindowExpression(String currentWindowString, String target) {
        return selenium.getWhetherThisWindowMatchWindowExpression(currentWindowString, target);
    }

    public void goBack() {
        selenium.goBack();
    }

    public void highlight(ElementLocator<?> elementLocator) {
        selenium.highlight(elementLocator.inSeleniumRepresentation());
    }

    public void ignoreAttributesWithoutValue(boolean ignore) {
        selenium.ignoreAttributesWithoutValue(String.valueOf(ignore));
    }

    public boolean isAlertPresent() {
        return Boolean.valueOf(selenium.isAlertPresent());
    }

    public boolean isChecked(ElementLocator<?> elementLocator) {
        return Boolean.valueOf(selenium.isChecked(elementLocator.inSeleniumRepresentation()));
    }

    public boolean isConfirmationPresent() {
        return Boolean.valueOf(selenium.isConfirmationPresent());
    }

    public boolean isEditable(ElementLocator<?> elementLocator) {
        return Boolean.valueOf(selenium.isEditable(elementLocator.inSeleniumRepresentation()));
    }

    public boolean isElementPresent(ElementLocator<?> elementLocator) {
        return selenium.isElementPresent(elementLocator.inSeleniumRepresentation());
    }

    public boolean isOrdered(ElementLocator<?> elementLocator1, ElementLocator<?> elementLocator2) {
        return selenium.isOrdered(elementLocator1.inSeleniumRepresentation(),
            elementLocator2.inSeleniumRepresentation());
    }

    public boolean isPromptPresent() {
        return selenium.isPromptPresent();
    }

    public boolean isSomethingSelected(ElementLocator<?> selectLocator) {
        return selenium.isSomethingSelected(selectLocator.inSeleniumRepresentation());
    }

    public boolean isTextPresent(String text) {
        return selenium.isTextPresent(text);
    }

    public boolean isVisible(ElementLocator<?> elementLocator) {
        return selenium.isVisible(elementLocator.inSeleniumRepresentation());
    }
    
    public void keyDown(ElementLocator<?> elementLocator, char character) {
        selenium.keyPress(elementLocator.inSeleniumRepresentation(), String.valueOf(character));
    }
    
    public void keyDown(ElementLocator<?> elementLocator, KeyCode keyCode) {
        selenium.keyPress(elementLocator.inSeleniumRepresentation(), keyCode.inSeleniumRepresentation());
    }

    public void keyDownNative(String keycode) {
        selenium.keyDownNative(keycode);
    }

    public void keyPress(ElementLocator<?> elementLocator, char character) {
        selenium.keyPress(elementLocator.inSeleniumRepresentation(), String.valueOf(character));
    }
    
    public void keyPress(ElementLocator<?> elementLocator, KeyCode keyCode) {
        selenium.keyPress(elementLocator.inSeleniumRepresentation(), keyCode.inSeleniumRepresentation());
    }

    public void keyPressNative(String keycode) {
        selenium.keyPressNative(keycode);
    }
    
    public void keyUp(ElementLocator<?> elementLocator, char character) {
        selenium.keyPress(elementLocator.inSeleniumRepresentation(), String.valueOf(character));
    }
    
    public void keyUp(ElementLocator<?> elementLocator, KeyCode keyCode) {
        selenium.keyPress(elementLocator.inSeleniumRepresentation(), keyCode.inSeleniumRepresentation());
    }

    public void keyUpNative(String keycode) {
        selenium.keyUpNative(keycode);
    }

    public void logToBrowser(String textToLog) {
        selenium.setContext(textToLog);
    }

    public void metaKeyDown() {
        selenium.metaKeyDown();
    }

    public void metaKeyUp() {
        selenium.metaKeyUp();
    }

    public void mouseDown(ElementLocator<?> elementLocator) {
        selenium.mouseDown(elementLocator.inSeleniumRepresentation());
    }

    public void mouseDownAt(ElementLocator<?> elementLocator, Point point) {
        selenium.mouseDownAt(elementLocator.inSeleniumRepresentation(), point.inSeleniumRepresentation());
    }

    public void mouseDownRight(ElementLocator<?> elementLocator) {
        selenium.mouseDownRight(elementLocator.inSeleniumRepresentation());
    }

    public void mouseDownRightAt(ElementLocator<?> elementLocator, Point point) {
        selenium.mouseDownRightAt(elementLocator.inSeleniumRepresentation(), point.inSeleniumRepresentation());
    }

    public void mouseMove(ElementLocator<?> elementLocator) {
        selenium.mouseMove(elementLocator.inSeleniumRepresentation());
    }

    public void mouseMoveAt(ElementLocator<?> elementLocator, Point point) {
        selenium.mouseMoveAt(elementLocator.inSeleniumRepresentation(), point.inSeleniumRepresentation());
    }

    public void mouseOut(ElementLocator<?> elementLocator) {
        selenium.mouseOut(elementLocator.inSeleniumRepresentation());
    }

    public void mouseOver(ElementLocator<?> elementLocator) {
        selenium.mouseOver(elementLocator.inSeleniumRepresentation());
    }

    public void mouseUp(ElementLocator<?> elementLocator) {
        selenium.mouseUp(elementLocator.inSeleniumRepresentation());
    }

    public void mouseUpAt(ElementLocator<?> elementLocator, Point point) {
        selenium.mouseUpAt(elementLocator.inSeleniumRepresentation(), point.inSeleniumRepresentation());
    }

    public void mouseUpRight(ElementLocator<?> elementLocator) {
        selenium.mouseUpRight(elementLocator.inSeleniumRepresentation());
    }

    public void mouseUpRightAt(ElementLocator<?> elementLocator, Point point) {
        selenium.mouseUpRightAt(elementLocator.inSeleniumRepresentation(), point.inSeleniumRepresentation());
    }

    public void open(URL url) {
        selenium.open(url.toString());
    }

    public void openWindow(URL url, WindowNameLocator windowName) {
        selenium.openWindow(url.toString(), windowName.getRawLocator());
    }

    public void refresh() {
        selenium.refresh();
    }

    public void removeAllSelections(ElementLocator<?> elementLocator) {
        selenium.removeAllSelections(elementLocator.inSeleniumRepresentation());
    }

    public void removeScript(JavaScript javaScript) {
        selenium.removeScript(javaScript.getIdentification());
    }

    public void removeSelection(ElementLocator<?> elementLocator, OptionLocator<?> optionLocator) {
        selenium.removeSelection(elementLocator.inSeleniumRepresentation(), optionLocator.inSeleniumRepresentation());
    }

    public String retrieveLastRemoteControlLogs() {
        return selenium.retrieveLastRemoteControlLogs();
    }

    public void runScript(JavaScript script) {
        selenium.runScript(script.getAsString());
    }

    public void select(ElementLocator<?> selectLocator, OptionLocator<?> optionLocator) {
        selenium.select(selectLocator.inSeleniumRepresentation(), optionLocator.inSeleniumRepresentation());
    }

    public void selectFrame(FrameLocator<?> frameLocator) {
        selenium.selectFrame(frameLocator.inSeleniumRepresentation());
    }

    public void selectPopUp(WindowLocator<?> windowLocator) {
        selenium.selectPopUp(windowLocator.getRawLocator());
    }

    public void selectWindow(WindowLocator<?> windowLocator) {
        selenium.selectWindow(windowLocator.inSeleniumRepresentation());
    }

    public void setBrowserLogLevel(LogLevel logLevel) {
        selenium.setBrowserLogLevel(logLevel.getLogLevelName());
    }

    public void setCursorPosition(ElementLocator<?> elementLocator, int position) {
        selenium.setCursorPosition(elementLocator.inSeleniumRepresentation(), String.valueOf(position));
    }

    public void setExtensionJs(JavaScript extensionJs) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public void setMouseSpeed(int pixels) {
        selenium.setMouseSpeed(String.valueOf(pixels));
    }

    public void setSpeed(long speedInMilis) {
        selenium.setSpeed(String.valueOf(speedInMilis));
    }

    public void setTimeout(long timeoutInMilis) {
        selenium.setTimeout(String.valueOf(timeoutInMilis));
    }

    public void shiftKeyDown() {
        selenium.shiftKeyDown();
    }

    public void shiftKeyUp() {
        selenium.shiftKeyUp();
    }

    public void shutDownSeleniumServer() {
        selenium.shutDownSeleniumServer();
    }

    public void start() {
        selenium.start();
    }

    public void stop() {
        selenium.stop();
    }

    public void submit(ElementLocator<?> formLocator) {
        selenium.submit(formLocator.inSeleniumRepresentation());
    }

    public void type(ElementLocator<?> elementLocator, String value) {
        selenium.type(elementLocator.inSeleniumRepresentation(), value);
    }

    public void typeKeys(ElementLocator<?> elementLocator, String value) {
        selenium.type(elementLocator.inSeleniumRepresentation(), value);
    }

    public void uncheck(ElementLocator<?> elementLocator) {
        selenium.uncheck(elementLocator.inSeleniumRepresentation());
    }

    public void useXpathLibrary(XPathLibrary xPathLibrary) {
        selenium.useXpathLibrary(xPathLibrary.inSeleniumRepresentation());
    }

    public void waitForCondition(JavaScript script) {
        String timeout = String.valueOf(configuration.getTimeout(TimeoutType.DEFAULT));
        selenium.waitForCondition(script.getAsString(), timeout);
    }

    public void waitForCondition(JavaScript script, long timeout) {
        selenium.waitForCondition(script.getAsString(), String.valueOf(timeout));
    }

    public void waitForFrameToLoad(URL frameURL) {
        String timeout = String.valueOf(configuration.getTimeout(TimeoutType.DEFAULT));
        selenium.waitForFrameToLoad(frameURL.toString(), timeout);
    }

    public void waitForFrameToLoad(URL frameURL, long timeout) {
        selenium.waitForFrameToLoad(frameURL.toString(), String.valueOf(timeout));
    }

    public void waitForPageToLoad() {
        String timeout = String.valueOf(configuration.getTimeout(TimeoutType.DEFAULT));
        selenium.waitForPageToLoad(timeout);
    }

    public void waitForPageToLoad(long timeout) {
        selenium.waitForPageToLoad(String.valueOf(timeout));
    }

    public void waitForPopUp(WindowNameLocator windowNameLocator, long timeoutInMilis) {
        selenium.waitForPopUp(windowNameLocator.getRawLocator(), Long.toString(timeoutInMilis));
    }

    public void windowFocus() {
        selenium.windowFocus();
    }

    public void windowMaximize() {
        selenium.windowMaximize();
    }

    private BufferedImage decodeBase64Screenshot(String screenshotInBase64) {
        byte[] screenshotPng = Base64.decodeBase64(screenshotInBase64);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(screenshotPng);
        BufferedImage result;
        try {
            result = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public Set<Cookie> getAllCookies() {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public Cookie getCookieByName(String cookieName) {
        String value = selenium.getCookieByName(cookieName);
        return Cookie.createCookie(cookieName, value);
    }

    @Override
    public boolean isCookiePresent(String cookieName) {
        return selenium.isCookiePresent(cookieName);
    }

    @Override
    public CookieCreateOptions createCookie(Cookie cookie) {
        CookieCreateOptions options = CookieOptions.forCreation();
        this.createCookie(cookie, options);
        return options;
    }

    @Override
    public void createCookie(Cookie cookie, CookieCreateOptions options) {
        selenium.createCookie(cookie.inSeleniumRepresentation(), options.inSeleniumRepresentation());
    }

    @Override
    public void deleteCookie(String cookieName, CookieDeleteOptions options) {
        selenium.deleteCookie(cookieName, options.inSeleniumRepresentation());
    }

    @Override
    public void addCustomRequestHeader(RequestHeader header) {
        selenium.addCustomRequestHeader(header.getName(), header.getValue());
    }
}
