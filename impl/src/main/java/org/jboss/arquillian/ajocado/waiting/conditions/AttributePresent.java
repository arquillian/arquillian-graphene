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
package org.jboss.arquillian.ajocado.waiting.conditions;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.ajocado.encapsulated.JavaScript;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumProxy;
import org.jboss.arquillian.ajocado.locator.AttributeLocator;
import org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;

import static org.jboss.arquillian.ajocado.utils.SimplifiedFormat.format;
import static org.apache.commons.lang.StringEscapeUtils.escapeJavaScript;
import static org.jboss.arquillian.ajocado.encapsulated.JavaScript.js;

/**
 * <p>
 * Implementation of Condition for waiting, if given element's attribute is already present on the page.
 * </p>
 * 
 * <p>
 * Implements Condition and JavaScriptCondition used in SeleniumWaiting and AjaxWaiting.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AttributePresent implements SeleniumCondition, JavaScriptCondition {

    /**
     * Proxy to local selenium instance
     */
    private AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();
    
    /** The element locator. */
    private AttributeLocator<?> attributeLocator;

    /**
     * Instantiates a new element present.
     */
    protected AttributePresent() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.waiting.Condition#isTrue()
     */
    public boolean isTrue() {
        Validate.notNull(attributeLocator);

        return selenium.isAttributePresent(attributeLocator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition#getJavaScriptCondition()
     */
    public JavaScript getJavaScriptCondition() {
        String escapedElementLocator = escapeJavaScript(this.attributeLocator.getAssociatedElement().getAsString());
        String escapedAttributeName = escapeJavaScript(this.attributeLocator.getAttribute().getAttributeName());
        return js(format("selenium.isAttributePresent('{0}', '{1}')", escapedElementLocator,
            escapedAttributeName));
    }

    /**
     * Factory method.
     * 
     * @return single instance of AttributePresent
     */
    public static AttributePresent getInstance() {
        return new AttributePresent();
    }

    /**
     * Returns the AttributePresent instance with given attributeLocator set.
     * 
     * @param attributeLocator
     *            the attribute locator
     * @return the attribute present
     */
    public AttributePresent locator(AttributeLocator<?> attributeLocator) {
        Validate.notNull(attributeLocator);

        AttributePresent copy = copy();
        copy.attributeLocator = attributeLocator;

        return copy;
    }

    /**
     * Returns the exact copy of this AttributePresent object.
     * 
     * @return the attribute present
     */
    private AttributePresent copy() {
        AttributePresent copy = new AttributePresent();
        copy.attributeLocator = attributeLocator;
        return copy;
    }
}
