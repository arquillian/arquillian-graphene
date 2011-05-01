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
import org.jboss.arquillian.ajocado.css.CssProperty;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;

import static org.apache.commons.lang.StringEscapeUtils.escapeJavaScript;
import static org.jboss.arquillian.ajocado.javascript.JavaScript.js;

/**
 * 
 * <p>
 * Implementation of Condition for waiting if element given by elementLocator has given CSS style property's value equal
 * to given value.
 * </p>
 * 
 * <p>
 * Implements Condition and JavaScriptCondition used in SeleniumWaiting and AjaxWaiting.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class StyleEquals implements SeleniumCondition, JavaScriptCondition {

    /**
     * Proxy to local selenium instance
     */
    private AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    /** The element locator. */
    private ElementLocator<?> elementLocator;

    /** The CSS property. */
    private CssProperty cssProperty;

    /**
     * The value
     */
    private String value;

    /**
     * Instantiates a new text equals.
     */
    protected StyleEquals() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.waiting.Condition#isTrue()
     */
    public boolean isTrue() {
        validate();
        return selenium.getStyle(elementLocator, cssProperty).equals(value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition#getJavaScriptCondition()
     */
    public JavaScript getJavaScriptCondition() {
        validate();
        String escapedLocator = escapeJavaScript(this.elementLocator.inSeleniumRepresentation());
        String escapedCssProperty = escapeJavaScript(this.cssProperty.getPropertyName());
        String escapedText = escapeJavaScript(this.value);
        return js("selenium.isElementPresent('{0}') && (selenium.getStyle('{0}', '{1}') == '{2}')").parametrize(
            escapedLocator, escapedCssProperty, escapedText);
    }
    
    private void validate() {
        Validate.notNull(elementLocator);
        Validate.notNull(cssProperty);
        Validate.notNull(value);
    }

    /**
     * Factory method.
     * 
     * @return single instance of TextEquals
     */
    public static StyleEquals getInstance() {
        return new StyleEquals();
    }

    /**
     * <p>
     * Returns the StyleEquals instance with given elementLocator set.
     * </p>
     * 
     * <p>
     * From this locator will be obtained the CSS property.
     * </p>
     * 
     * @param elementLocator
     *            the element locator
     * @return the StyleEquals object with preset locator
     */
    public StyleEquals locator(ElementLocator<?> elementLocator) {
        Validate.notNull(elementLocator);

        StyleEquals copy = copy();
        copy.elementLocator = elementLocator;

        return copy;
    }

    /**
     * <p>
     * Returns the StyleEquals instance with CSS property preset.
     * </p>
     * 
     * <p>
     * This CSS property will be obtained in way to compare equality to given value.
     * </p>
     * 
     * @param cssProperty
     *            the property to obtain
     * @return the StyleEquals object with preset CSS property to obtain
     */
    public StyleEquals property(CssProperty cssProperty) {
        Validate.notNull(cssProperty);

        StyleEquals copy = copy();
        copy.cssProperty = cssProperty;

        return copy;
    }

    /**
     * <p>
     * Returns the StyleEquals instance with value set.
     * </p>
     * 
     * <p>
     * For equality with this value the condition will wait.
     * </p>
     * 
     * @param value
     *            it should wait for equality
     * @return the StyleEquals object with preset value
     */
    public StyleEquals value(String value) {
        Validate.notNull(value);

        StyleEquals copy = copy();
        copy.value = value;

        return copy;
    }

    /**
     * Returns the exact copy of this ElementPresent object.
     * 
     * @return the copy of this TextEquals object
     */
    private StyleEquals copy() {
        StyleEquals copy = new StyleEquals();
        copy.elementLocator = elementLocator;
        copy.cssProperty = cssProperty;
        copy.value = value;
        return copy;
    }
}
