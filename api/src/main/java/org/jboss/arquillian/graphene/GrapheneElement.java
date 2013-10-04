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

import java.util.List;

import org.jboss.arquillian.graphene.spi.ImplementedBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
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
 * <b>Important</b>: {@link GrapheneElementImpl} <i>is not intended for extension</i>, do not subclass it. The
 * {@link GrapheneElementImpl} might become abstract class or interface in the future. It can't be final because then it couldn't be
 * proxied by Graphene.
 * </p>
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@ImplementedBy(className = "org.jboss.arquillian.graphene.GrapheneElementImpl")
public interface GrapheneElement extends WebElement, Locatable, WrapsElement {

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
    boolean isPresent();

    /**
     * This method is alternative to {@link #findElements(By)}, but it returns list of type {@link GrapheneElement}.
     *
     * @return list of elements
     *
     * @see WebElement#findElement(By)
     */
    List<GrapheneElement> findGrapheneElements(By by);

    /**
     * Overriden version of <code>WebElement.findBy()</code> method.
     *
     * @return GrapheneElement
     *
     * @see WebElement#findElement(By)
     */
    GrapheneElement findElement(By by);
}
