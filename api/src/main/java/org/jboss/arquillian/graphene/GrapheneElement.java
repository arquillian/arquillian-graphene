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
import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;

/**
 * <p>
 * Interface for Graphene extensions of {@link WebElement}.
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
 * <b>Important</b>: {@link GrapheneElement} <i>is not intended for extension</i>, do not subclass it. The {@link GrapheneElement} might become abstract class or interface in the future. It can't be final because then it couldn't be proxied by Graphene.
 * </p>
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class GrapheneElement implements WebElement, Locatable, WrapsElement {

    private final WebElement element;

    public GrapheneElement(WebElement element) {
        this.element = element;
    }

    /**
     * <p>
     * Returns true if this element is present in the page
     * </p>
     *
     * <p>
     * Note: WebDriver generally does not need this method since their elements are traditionally returned by calls as
     * {@link SearchContext#findElement(By)}. However Graphene de-references elements in time of call, and the object exposed
     * publicly is just an proxy object. In that case we can call any method on that object which can lead into
     * {@link NoSuchElementException}. To prevent this behavior, you should first check that the element is present in the page
     * using this method.
     * </p>
     *
     * @return true if this element is present in the page
     */
    public boolean isPresent() {
        try {
            element.isDisplayed();
            return true;
        } catch (NoSuchElementException ignored) {
            return false;
        }
    }

    /**
     * This method is alternative to {@link #findElements(By)}, but it returns list of type {@link GrapheneElement}.
     *
     * @return list of elements
     *
     * @see WebElement#findElement(By)
     */
    public List<GrapheneElement> findGrapheneElements(By by) {
        List<GrapheneElement> grapheneElements = new LinkedList<GrapheneElement>();
        for (WebElement e : element.findElements(by)) {
            grapheneElements.add(new GrapheneElement(e));
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
    public GrapheneElement findElement(By by) {
        return new GrapheneElement(element.findElement(by));
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
     * @see org.openqa.selenium.internal.Locatable#getCoordinates()
     */
    @Override
    public Coordinates getCoordinates() {
        return ((Locatable) element).getCoordinates();
    }

}
