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

import static org.apache.commons.lang.StringEscapeUtils.escapeJavaScript;
import static org.jboss.arquillian.ajocado.javascript.JavaScript.js;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.ajocado.format.SimplifiedFormat;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;

/**
 * <p>
 * Implementation of Condition for waiting until given element is not displayed.
 * </p>
 * 
 * <p>
 * Implements Condition and JavaScriptCondition used in SeleniumWaiting and AjaxWaiting.
 * </p>
 * 
 * @author <a href="mailto:ppitonak@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class IsNotVisible implements SeleniumCondition, JavaScriptCondition {

    /**
     * Proxy to local selenium instance
     */
    private AjaxSelenium selenium = AjaxSeleniumContext.getProxy();
    
    /** The element locator. */
    private ElementLocator<?> elementLocator;

    /**
     * Instantiates a new condition
     */
    protected IsNotVisible() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.waiting.Condition#isTrue()
     */
    public boolean isTrue() {
        Validate.notNull(elementLocator);

        return !selenium.isVisible(elementLocator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.test.selenium.waiting.ajax.JavaScriptCondition#getJavaScriptCondition()
     */
    public JavaScript getJavaScriptCondition() {
        String escapedLocator = escapeJavaScript(this.elementLocator.inSeleniumRepresentation());
        return js(SimplifiedFormat.format("!selenium.isVisible('{0}')", escapedLocator));
    }

    /**
     * Factory method.
     * 
     * @return single instance of IsNotDisplayed
     */
    public static IsNotVisible getInstance() {
        return new IsNotVisible();
    }

    /**
     * Returns the IsNotDisplayed instance with given elementLocator set.
     * 
     * @param elementLocator
     *            the element locator
     * @return the IsNotDisplayed instance
     */
    public IsNotVisible locator(ElementLocator<?> elementLocator) {
        Validate.notNull(elementLocator);

        IsNotVisible copy = copy();
        copy.elementLocator = elementLocator;

        return copy;
    }

    /**
     * Returns the exact copy of this IsNotDisplayed object.
     * 
     * @return the IsNotDisplayed instance
     */
    private IsNotVisible copy() {
        IsNotVisible copy = new IsNotVisible();
        copy.elementLocator = elementLocator;
        return copy;
    }
}
