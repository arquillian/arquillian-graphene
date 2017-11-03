/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene;

import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.interactions.internal.Locatable;

/**
 * <p>
 * Implementation of {@link GrapheneElement} - interface for Graphene extensions of {@link WebElement}.
 * </p>
 *
 * <p>
 * Following methods are provided over the {@link WebElement} interface:
 * </p>
 *
 * <ul>
 * <li>{@link #isPresent()}</li>
 * <li>{@link #findGrapheneElements(By)}</li>
 * </ul>
 *
 * <p>
 * <b>Important</b>: {@link GrapheneElementImpl} <i>is not intended for extension</i>, do not subclass it. The
 * {@link GrapheneElementImpl} might become abstract class or interface in the future. It can't be final because then it
 * couldn't be proxied by Graphene.
 * </p>
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class GrapheneElementImpl implements GrapheneElement {

    private final WebElement element;

    public GrapheneElementImpl(WebElement element) {
        this.element = element;
    }

    @Override
    public boolean isPresent() {
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    @Override
    public List<GrapheneElement> findGrapheneElements(By by) {
        List<GrapheneElement> grapheneElements = new LinkedList<GrapheneElement>();
        for (WebElement e : element.findElements(by)) {
            grapheneElements.add(new GrapheneElementImpl(e));
        }
        return grapheneElements;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#click()
     */
    @Override
    public void click() {
        element.click();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#submit()
     */
    @Override
    public void submit() {
        element.submit();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#sendKeys(java.lang.CharSequence[])
     */
    @Override
    public void sendKeys(CharSequence... keysToSend) {
        element.sendKeys(keysToSend);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#clear()
     */
    @Override
    public void clear() {
        element.clear();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#getTagName()
     */
    @Override
    public String getTagName() {
        return element.getTagName();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#getAttribute(java.lang.String)
     */
    @Override
    public String getAttribute(String name) {
        return element.getAttribute(name);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#isSelected()
     */
    @Override
    public boolean isSelected() {
        return element.isSelected();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#isEnabled()
     */
    @Override
    public boolean isEnabled() {
        return element.isEnabled();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#getText()
     */
    @Override
    public String getText() {
        return element.getText();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#findElements(org.openqa.selenium.By)
     */
    @Override
    public List<WebElement> findElements(By by) {
        return element.findElements(by);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#findElement(org.openqa.selenium.By)
     */
    @Override
    public GrapheneElementImpl findElement(By by) {
        return new GrapheneElementImpl(element.findElement(by));
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#isDisplayed()
     */
    @Override
    public boolean isDisplayed() {
        return element.isDisplayed();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#getLocation()
     */
    @Override
    public Point getLocation() {
        return element.getLocation();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#getSize()
     */
    @Override
    public Dimension getSize() {
        return element.getSize();
    }

    @Override
    public Rectangle getRect() {
        return element.getRect();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.WebElement#getCssValue(java.lang.String)
     */
    @Override
    public String getCssValue(String propertyName) {
        return element.getCssValue(propertyName);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.internal.WrapsElement#getWrappedElement()
     */
    @Override
    public WebElement getWrappedElement() {
        return element;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.interactions.internal.Locatable#getCoordinates()
     */
    @Override
    public Coordinates getCoordinates() {
        return ((Locatable) element).getCoordinates();
    }

    /*
     * (non-Javadoc)
     *
     * @see org.openqa.selenium.TakesScreenshot#getScreenshotAs(org.openqa.selenium.OutputType)
     */
    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return element.getScreenshotAs(outputType);
    }

    @Override
    public int hashCode() {
        if (element == null) {
            // shouldn't ever happen
            return super.hashCode();
        }
        // see #equals for explanation
        return element.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (element == null) {
            // shouldn't ever happen
            return super.equals(obj);
        }
        if (this == obj) {
            return true;
        }
        // equals requires symmetry, so this equals implementation must conform to Selenium's unwrapping equals (see
        // org.openqa.selenium.remote.RemoteWebElement.equals(Object)) - it's enough to just delegate further
        return element.equals(obj);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GrapheneElement -> " + element;
    }

    @Override
    public void doubleClick() {
        Graphene.doubleClick(element);
    }

    @Override
    public void writeIntoElement(String text) {
        Graphene.writeIntoElement(element, text);
    }
}
