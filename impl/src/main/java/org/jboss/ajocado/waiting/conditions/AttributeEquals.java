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
package org.jboss.ajocado.waiting.conditions;

import org.apache.commons.lang.Validate;
import org.jboss.ajocado.encapsulated.JavaScript;
import org.jboss.ajocado.framework.AjaxSelenium;
import org.jboss.ajocado.framework.AjaxSeleniumProxy;
import org.jboss.ajocado.locator.AttributeLocator;
import org.jboss.ajocado.waiting.ajax.JavaScriptCondition;
import org.jboss.ajocado.waiting.selenium.SeleniumCondition;

import static org.jboss.ajocado.utils.text.SimplifiedFormat.format;
import static org.apache.commons.lang.StringEscapeUtils.escapeJavaScript;
import static org.jboss.ajocado.encapsulated.JavaScript.js;

/**
 * 
 * <p>
 * Implementation of Condition for waiting if element's attribute given by attributeLocator has value equal to given
 * value.
 * </p>
 * 
 * <p>
 * Implements Condition and JavaScriptCondition used in SeleniumWaiting and AjaxWaiting.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AttributeEquals implements SeleniumCondition, JavaScriptCondition {

    /**
     * Proxy to local selenium instance
     */
    private AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();
    
    /** The element locator. */
    private AttributeLocator<?> attributeLocator;

    /** The value. */
    private String value;

    /**
     * Instantiates a new AttributeEquals
     */
    protected AttributeEquals() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.waiting.Condition#isTrue()
     */
    public boolean isTrue() {
        Validate.notNull(attributeLocator);
        Validate.notNull(value);

        return selenium.getAttribute(attributeLocator).equals(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.waiting.ajax.JavaScriptCondition#getJavaScriptCondition()
     */
    public JavaScript getJavaScriptCondition() {
        String escapedElementLocator = escapeJavaScript(this.attributeLocator.getAssociatedElement().getAsString());
        String escapedAttributeLocator = escapeJavaScript(this.attributeLocator.getAsString());
        String escapedValue = escapeJavaScript(this.value);
        return js(format("selenium.isElementPresent('{0}') && (selenium.getAttribute('{1}') == '{2}')",
            escapedElementLocator, escapedAttributeLocator, escapedValue));
    }

    /**
     * Factory method.
     * 
     * @return single instance of AttributeEquals
     */
    public static AttributeEquals getInstance() {
        return new AttributeEquals();
    }

    /**
     * <p>
     * Returns the AttributeEquals instance with given attributeLocator preset.
     * </p>
     * 
     * <p>
     * From this element's attribute will be obtained the value.
     * </p>
     * 
     * @param attributeLocator
     *            the attribute locator
     * @return the AttributeEquals object with preset locator
     */
    public AttributeEquals locator(AttributeLocator<?> attributeLocator) {
        Validate.notNull(attributeLocator);

        AttributeEquals copy = copy();
        copy.attributeLocator = attributeLocator;

        return copy;
    }

    /**
     * <p>
     * Returns the AttributeEquals instance with preset value.
     * </p>
     * 
     * <p>
     * For equality with this value the condition will wait.
     * </p>
     * 
     * @param value
     *            it should wait for equality
     * @return the AttributeEquals object with preset value
     */
    public AttributeEquals text(String value) {
        Validate.notNull(value);

        AttributeEquals copy = copy();
        copy.value = value;

        return copy;
    }

    /**
     * Returns the exact copy of this AttributeEquals object.
     * 
     * @return the copy of this AttributeEquals object
     */
    private AttributeEquals copy() {
        AttributeEquals copy = new AttributeEquals();
        copy.attributeLocator = attributeLocator;
        copy.value = value;
        return copy;
    }
}
