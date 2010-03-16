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
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.jboss.test.selenium.dom.Event;
import org.jboss.test.selenium.encapsulated.*;
import org.jboss.test.selenium.geometry.*;
import org.jboss.test.selenium.locator.*;
import org.jboss.test.selenium.locator.type.LocationStrategy;
import org.jboss.test.selenium.utils.array.ArrayTransform;

import com.thoughtworks.selenium.Selenium;

public class DefaultTypedSelenium implements TypedSelenium {

	Selenium selenium;

	public void addCustomRequestHeader(String key, String value) {
		throw new UnsupportedOperationException();
	}

	public void addLocationStrategy(LocationStrategy locationStrategy, JavaScript strategyDefinition) {
		selenium.addLocationStrategy(locationStrategy.getStrategyName(), strategyDefinition.toString());
	}

	public void addScript(JavaScript scriptContent, ScriptTag scriptTagId) {
		throw new UnsupportedOperationException();
	}

	public void addSelection(ElementLocator elementLocator, ElementLocator optionLocator) {
		selenium.addSelection(elementLocator.getAsString(), optionLocator.getAsString());
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

	public IdLocator assignId(ElementLocator elementLocator, String identifier) {
		selenium.assignId(elementLocator.getAsString(), identifier);
		return new IdLocator(identifier);
	}

	public void attachFile(ElementLocator fieldLocator, File fileLocator) {
		throw new UnsupportedOperationException();
	}

	public void attachFile(ElementLocator fieldLocator, URL fileLocator) {
		throw new UnsupportedOperationException();
	}

	public void captureEntirePageScreenshot(File filename, Kwargs kwargs) {
		throw new UnsupportedOperationException();
	}

	public BufferedImage captureEntirePageScreenshotToString(Kwargs kwargs) {
		throw new UnsupportedOperationException();
	}

	public NetworkTraffic captureNetworkTraffic(NetworkTrafficType type) {
		throw new UnsupportedOperationException();
	}

	public void captureScreenshot(File filename) {
		throw new UnsupportedOperationException();
	}

	public BufferedImage captureScreenshotToString() {
		throw new UnsupportedOperationException();
	}

	public void check(ElementLocator elementLocator) {
		selenium.check(elementLocator.getAsString());
	}

	public void chooseCancelOnNextConfirmation() {
		selenium.chooseCancelOnNextConfirmation();
	}

	public void chooseOkOnNextConfirmation() {
		selenium.chooseOkOnNextConfirmation();
	}

	public void click(ElementLocator elementLocator) {
		selenium.click(elementLocator.getAsString());
	}

	public void clickAt(ElementLocator elementLocator, Point point) {
		selenium.clickAt(elementLocator.getAsString(), point.getCoords());
	}

	public void close() {
		selenium.close();
	}

	public void contextMenu(ElementLocator elementLocator) {
		selenium.contextMenu(elementLocator.getAsString());
	}

	public void contextMenuAt(ElementLocator elementLocator, Point point) {
		selenium.contextMenuAt(elementLocator.getAsString(), point.getCoords());
	}

	public void controlKeyDown() {
		selenium.controlKeyDown();
	}

	public void controlKeyUp() {
		selenium.controlKeyUp();
	}

	public void createCookie(Cookie cookie, CookieParameters parameters) {
		throw new UnsupportedOperationException();
	}

	public void deleteAllVisibleCookies() {
		selenium.deleteAllVisibleCookies();
	}

	public void deleteCookie(Cookie cookie, CookieParameters parameters) {
		throw new UnsupportedOperationException();
	}

	public void deselectPopUp() {
		selenium.deselectPopUp();
	}

	public void doubleClick(ElementLocator elementLocator) {
		selenium.doubleClick(elementLocator.getAsString());
	}

	public void doubleClickAt(ElementLocator elementLocator, Point point) {
		selenium.doubleClickAt(elementLocator.getAsString(), point.getCoords());
	}

	public void dragAndDrop(ElementLocator elementLocator, Offset offset) {
		selenium.dragAndDrop(elementLocator.getAsString(), offset.getMovement());
	}

	public void dragAndDropToObject(ElementLocator elementLocatorOfObjectToBeDragged,
			ElementLocator elementLocatorOfDragDestinationObject) {
		selenium.dragAndDropToObject(elementLocatorOfDragDestinationObject.getAsString(),
				elementLocatorOfObjectToBeDragged.getAsString());
	}

	public void dragdrop(ElementLocator elementLocator, Offset offset) {
		selenium.dragdrop(elementLocator.getAsString(), offset.getMovement());
	}

	public void fireEvent(ElementLocator elementLocator, Event event) {
		selenium.fireEvent(elementLocator.getAsString(), event.getEventName());
	}

	public void focus(ElementLocator elementLocator) {
		selenium.focus(elementLocator.getAsString());
	}

	public String getAlert() {
		return selenium.getAlert();
	}

	public List<ElementLocator> getAllButtons() {
		throw new UnsupportedOperationException();
	}

	public List<ElementLocator> getAllFields() {
		throw new UnsupportedOperationException();
	}

	public List<ElementLocator> getAllLinks() {
		throw new UnsupportedOperationException();
	}

	public List<WindowId> getAllWindowIds() {
		throw new UnsupportedOperationException();
	}

	public List<String> getAllWindowNames() {
		throw new UnsupportedOperationException();
	}

	public List<String> getAllWindowTitles() {
		throw new UnsupportedOperationException();
	}

	public String getAttribute(AttributeLocator attributeLocator) {
		return selenium.getAttribute(attributeLocator.getAsString());
	}

	public List<String> getAttributeFromAllWindows(String attributeName) {
		throw new UnsupportedOperationException();
	}

	public String getBodyText() {
		return selenium.getBodyText();
	}

	public String getConfirmation() {
		return selenium.getConfirmation();
	}

	public List<Cookie> getCookie() {
		throw new UnsupportedOperationException();
	}

	public Cookie getCookieByName(Cookie name) {
		throw new UnsupportedOperationException();
	}

	public int getCount(IterableLocator locator) {
		return selenium.getXpathCount(locator.getAsString()).intValue();
	}

	public int getCursorPosition(ElementLocator elementLocator) {
		return selenium.getCursorPosition(elementLocator.getAsString()).intValue();
	}

	public Dimension getElementDimension(ElementLocator elementLocator) {
		return new Dimension(getElementHeight(elementLocator), getElementWidth(elementLocator));
	}

	public int getElementHeight(ElementLocator elementLocator) {
		return selenium.getElementHeight(elementLocator.getAsString()).intValue();
	}

	public int getElementIndex(ElementLocator elementLocator) {
		return selenium.getElementIndex(elementLocator.getAsString()).intValue();
	}

	public Point getElementPosition(ElementLocator elementLocator) {
		return new Point(getElementPositionLeft(elementLocator), getElementPositionTop(elementLocator));
	}

	public int getElementPositionLeft(ElementLocator elementLocator) {
		return selenium.getElementPositionLeft(elementLocator.getAsString()).intValue();
	}

	public int getElementPositionTop(ElementLocator elementLocator) {
		return selenium.getElementPositionTop(elementLocator.getAsString()).intValue();
	}

	public int getElementWidth(ElementLocator elementLocator) {
		return selenium.getElementWidth(elementLocator.getAsString()).intValue();
		
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

	public List<String> getSelectOptions(ElementLocator selectLocator) {
		return Arrays.asList(selenium.getSelectOptions(selectLocator.getAsString()));
	}

	public String getSelectedId(ElementLocator selectLocator) {
		return selenium.getSelectedId(selectLocator.getAsString());
	}

	public List<String> getSelectedIds(ElementLocator selectLocator) {
		return Arrays.asList(selenium.getSelectedIds(selectLocator.getAsString()));
	}

	public int getSelectedIndex(ElementLocator selectLocator) {
		return Integer.valueOf(selenium.getSelectedIndex(selectLocator.getAsString()));
	}

	public List<Integer> getSelectedIndexes(ElementLocator selectLocator) {
		return Arrays.asList(transformArrayOfStringToInteger.transform(selenium.getSelectedIndexes(selectLocator.getAsString())));
	}
	
	private ArrayTransform<String, Integer> transformArrayOfStringToInteger = new ArrayTransform<String, Integer>(Integer.class) {
		public Integer transformation(String source) {
			return Integer.valueOf(source);
		}
	};

	public String getSelectedLabel(ElementLocator selectLocator) {
		return selenium.getSelectedLabel(selectLocator.getAsString());
	}

	public List<String> getSelectedLabels(ElementLocator selectLocator) {
		return Arrays.asList(selenium.getSelectedLabels(selectLocator.getAsString()));
	}

	public String getSelectedValue(ElementLocator selectLocator) {
		return selenium.getSelectedValue(selenium.getSelectedValue(selectLocator.getAsString()));
	}

	public List<String> getSelectedValues(ElementLocator selectLocator) {
		return Arrays.asList(selenium.getSelectedValues(selectLocator.getAsString()));
	}

	public long getSpeed() {
		return Long.valueOf(selenium.getSpeed());
	}

	public String getText(ElementLocator elementLocator) {
		return selenium.getText(elementLocator.getAsString());
	}

	public String getTitle() {
		return selenium.getTitle();
	}

	public String getValue(ElementLocator elementLocator) {
		return selenium.getValue(elementLocator.getAsString());
	}

	public boolean getWhetherThisFrameMatchFrameExpression(Frame currentFrame, Frame targetFrame) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public boolean getWhetherThisWindowMatchWindowExpression(Window currentWindowString, Window target) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public void goBack() {
		selenium.goBack();
	}

	public void highlight(ElementLocator elementLocator) {
		selenium.highlight(elementLocator.getAsString());
	}

	public void ignoreAttributesWithoutValue(boolean ignore) {
		selenium.ignoreAttributesWithoutValue(String.valueOf(ignore));
	}

	public boolean isAlertPresent() {
		return Boolean.valueOf(selenium.isAlertPresent());
	}

	public boolean isChecked(ElementLocator elementLocator) {
		return Boolean.valueOf(selenium.isChecked(elementLocator.getAsString()));
	}

	public boolean isConfirmationPresent() {
		return Boolean.valueOf(selenium.isConfirmationPresent());
	}

	public boolean isCookiePresent(Cookie name) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public boolean isEditable(ElementLocator elementLocator) {
		return Boolean.valueOf(selenium.isEditable(elementLocator.getAsString()));
	}

	public boolean isElementPresent(ElementLocator elementLocator) {
		return selenium.isElementPresent(elementLocator.getAsString());
	}

	public boolean isOrdered(ElementLocator elementLocator1, ElementLocator elementLocator2) {
		return selenium.isOrdered(elementLocator1.getAsString(), elementLocator2.getAsString());
	}

	public boolean isPromptPresent() {
		return isPromptPresent();
	}

	public boolean isSomethingSelected(ElementLocator selectLocator) {
		return selenium.isSomethingSelected(selectLocator.getAsString());
	}

	public boolean isTextPresent(String text) {
		return selenium.isTextPresent(text);
	}

	public boolean isVisible(ElementLocator elementLocator) {
		return selenium.isVisible(elementLocator.getAsString());
	}

	public void keyDown(ElementLocator elementLocator, String keySequence) {
		selenium.keyDown(elementLocator.getAsString(), keySequence);
	}

	public void keyDownNative(String keycode) {
		selenium.keyDownNative(keycode);
	}

	public void keyPress(ElementLocator elementLocator, String keySequence) {
		selenium.keyPress(elementLocator.getAsString(), keySequence);
	}

	public void keyPressNative(String keycode) {
		selenium.keyPressNative(keycode);
	}

	public void keyUp(ElementLocator elementLocator, String keySequence) {
		selenium.keyUp(elementLocator.getAsString(), keySequence);
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

	public void mouseDown(ElementLocator elementLocator) {
		selenium.mouseDown(elementLocator.getAsString());
	}

	public void mouseDownAt(ElementLocator elementLocator, Point point) {
		selenium.mouseDownAt(elementLocator.getAsString(), point.getCoords());
	}

	public void mouseDownRight(ElementLocator elementLocator) {
		selenium.mouseDownRight(elementLocator.getAsString());
	}

	public void mouseDownRightAt(ElementLocator elementLocator, Point point) {
		selenium.mouseDownRightAt(elementLocator.getAsString(), point.getCoords());
	}

	public void mouseMove(ElementLocator elementLocator) {
		selenium.mouseMove(elementLocator.getAsString());
	}

	public void mouseMoveAt(ElementLocator elementLocator, Point point) {
		selenium.mouseMoveAt(elementLocator.getAsString(), point.getCoords());
	}

	public void mouseOut(ElementLocator elementLocator) {
		selenium.mouseOut(elementLocator.getAsString());
	}

	public void mouseOver(ElementLocator elementLocator) {
		selenium.mouseOver(elementLocator.getAsString());
	}

	public void mouseUp(ElementLocator elementLocator) {
		selenium.mouseUp(elementLocator.getAsString());
	}

	public void mouseUpAt(ElementLocator elementLocator, Point point) {
		selenium.mouseUpAt(elementLocator.getAsString(), point.getCoords());
	}

	public void mouseUpRight(ElementLocator elementLocator) {
		selenium.mouseUpRight(elementLocator.getAsString());
	}

	public void mouseUpRightAt(ElementLocator elementLocator, Point point) {
		selenium.mouseUpRightAt(elementLocator.getAsString(), point.getCoords());
	}

	public void open(URL url) {
		selenium.open(url.toString());
	}

	public void openWindow(URL url, WindowId windowID) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public void refresh() {
		selenium.refresh();
	}

	public void removeAllSelections(ElementLocator elementLocator) {
		selenium.removeAllSelections(elementLocator.getAsString());
	}

	public void removeScript(ScriptTag scriptTagId) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public void removeSelection(ElementLocator elementLocator, ElementLocator optionLocator) {
		selenium.removeSelection(elementLocator.getAsString(), optionLocator.getAsString());
	}

	public String retrieveLastRemoteControlLogs() {
		return selenium.retrieveLastRemoteControlLogs();
	}

	public void runScript(JavaScript script) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public void select(ElementLocator selectLocator, ElementLocator optionLocator) {
		selenium.select(selectLocator.getAsString(), optionLocator.getAsString());
	}

	public void selectFrame(ElementLocator elementLocator) {
		selenium.selectFrame(elementLocator.getAsString());
	}

	public void selectPopUp(WindowId windowID) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public void selectWindow(WindowId windowID) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public void setBrowserLogLevel(LogLevel logLevel) {
		selenium.setBrowserLogLevel(logLevel.getLogLevelName());
	}

	public void setCursorPosition(ElementLocator elementLocator, int position) {
		selenium.setCursorPosition(elementLocator.getAsString(), String.valueOf(position));
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

	public void submit(ElementLocator formLocator) {
		selenium.submit(formLocator.getAsString());
	}

	public void type(ElementLocator elementLocator, String value) {
		selenium.type(elementLocator.getAsString(), value);
	}

	public void typeKeys(ElementLocator elementLocator, String value) {
		selenium.type(elementLocator.getAsString(), value);
	}

	public void uncheck(ElementLocator elementLocator) {
		selenium.uncheck(elementLocator.getAsString());
	}

	public void useXpathLibrary(XpathLibrary xpathLibrary) {
		selenium.useXpathLibrary(xpathLibrary.getXpathLibraryName());
	}

	public void waitForCondition(JavaScript script, long timeoutInMilis) {
		selenium.waitForCondition(script.getJavaScript(), String.valueOf(timeoutInMilis));
	}

	public void waitForFrameToLoad(URL frameURL, long timeout) {
		selenium.waitForFrameToLoad(frameURL.toString(), String.valueOf(timeout));
	}

	public void waitForPageToLoad(long timeout) {
		selenium.waitForPageToLoad(String.valueOf(timeout));
	}

	public void waitForPopUp(WindowId windowId, long timeoutInMilis) {
		throw new UnsupportedOperationException("not implemented yet");
	}

	public void windowFocus() {
		selenium.windowFocus();
	}

	public void windowMaximize() {
		selenium.windowMaximize();
	}

}
