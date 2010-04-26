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
package org.jboss.test.selenium.waiting.conditions;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.internal.Contextual;
import org.jboss.test.selenium.locator.ElementLocator;
import org.jboss.test.selenium.waiting.Condition;
import org.jboss.test.selenium.waiting.ajax.JavaScriptCondition;

import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;

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
public class ElementPresent implements Condition, JavaScriptCondition, Contextual {
    
    /** The selenium. */
    AjaxSelenium selenium = AjaxSelenium.getCurrentContext(this);
    
    /** The element locator. */
    ElementLocator elementLocator;

    /**
     * Instantiates a new element present.
     */
    protected ElementPresent() {
    }

    /* (non-Javadoc)
     * @see org.jboss.test.selenium.waiting.Condition#isTrue()
     */
    public boolean isTrue() {
        Validate.notNull(elementLocator);

        return selenium.isElementPresent(elementLocator);
    }

    /* (non-Javadoc)
     * @see org.jboss.test.selenium.waiting.ajax.JavaScriptCondition#getJavaScriptCondition()
     */
    public JavaScript getJavaScriptCondition() {
        return new JavaScript(format("selenium.isElementPresent('{0}')", elementLocator.getAsString()));
    }

    /**
     * Factory method.
     *
     * @return single instance of ElementPresent
     */
    public static ElementPresent getInstance() {
        return new ElementPresent();
    }

    /**
     * Returns the ElementPresent instance with given elementLocator set.
     *
     * @param elementLocator the element locator
     * @return the element present
     */
    public ElementPresent locator(ElementLocator elementLocator) {
        Validate.notNull(elementLocator);

        ElementPresent copy = copy();
        copy.elementLocator = elementLocator;

        return copy;
    }

    /**
     * Returns the exact copy of this ElementPresent object.
     *
     * @return the element present
     */
    private ElementPresent copy() {
        ElementPresent copy = new ElementPresent();
        copy.elementLocator = elementLocator;
        return copy;
    }
}
