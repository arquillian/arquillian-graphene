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
package org.jboss.arquillian.ajocado.waiting.conditions;

import static org.apache.commons.lang3.StringEscapeUtils.escapeEcmaScript;
import static org.jboss.arquillian.ajocado.javascript.JavaScript.js;

import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;
import org.jboss.arquillian.core.spi.Validate;

/**
 * <p>
 * Implementation of Condition for waiting, if given element is already present on the page.
 * </p>
 *
 * <p>
 * Implements Condition and JavaScriptCondition used in SeleniumWaiting and AjaxWaiting.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class ElementNotPresent implements SeleniumCondition, JavaScriptCondition {

    /**
     * Proxy to local selenium instance
     */
    private GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();

    /** The element locator. */
    private ElementLocator<?> elementLocator;

    /**
     * Instantiates a new element present.
     */
    protected ElementNotPresent() {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.ajocado.waiting.Condition#isTrue()
     */
    @Override
    public boolean isTrue() {
        Validate.notNull(elementLocator, "elementLocator should not be null");

        return !selenium.isElementPresent(elementLocator);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition#getJavaScriptCondition()
     */
    @Override
    public JavaScript getJavaScriptCondition() {
        String escapedLocator = escapeEcmaScript(this.elementLocator.inSeleniumRepresentation());
        return js("!selenium.isElementPresent('{0}')").parametrize(escapedLocator);
    }

    /**
     * Factory method.
     *
     * @return single instance of ElementPresent
     */
    public static ElementNotPresent getInstance() {
        return new ElementNotPresent();
    }

    /**
     * Returns the ElementPresent instance with given elementLocator set.
     *
     * @param elementLocator the element locator
     * @return the element present
     */
    public ElementNotPresent locator(ElementLocator<?> elementLocator) {
        Validate.notNull(elementLocator, "elementLocator should not be null");

        ElementNotPresent copy = copy();
        copy.elementLocator = elementLocator;

        return copy;
    }

    /**
     * Returns the exact copy of this ElementPresent object.
     *
     * @return the element present
     */
    private ElementNotPresent copy() {
        ElementNotPresent copy = new ElementNotPresent();
        copy.elementLocator = elementLocator;
        return copy;
    }
}
