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
import static org.jboss.arquillian.ajocado.locator.element.ElementLocationStrategy.JQUERY;
import static org.jboss.arquillian.ajocado.locator.element.ElementLocationStrategy.XPATH;

import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.IterableLocator;
import org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;
import org.jboss.arquillian.core.spi.Validate;

/**
 *
 * <p>
 * Implementation of Condition for waiting if count of elements with given locator is equal to given count.
 * </p>
 *
 * <p>
 * Implements Condition and JavaScriptCondition used in SeleniumWaiting and AjaxWaiting.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class CountEquals implements SeleniumCondition, JavaScriptCondition {

    /**
     * Proxy to local selenium instance
     */
    private GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();

    /** The element locator. */
    private IterableLocator<?> iterableLocator;

    /**
     * The count
     */
    private Integer count;

    /**
     * Instantiates a new text equals.
     */
    protected CountEquals() {
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.ajocado.waiting.Condition#isTrue()
     */
    @Override
    public boolean isTrue() {
        validate();
        return selenium.getCount(iterableLocator) == count;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition#getJavaScriptCondition()
     */
    @Override
    public JavaScript getJavaScriptCondition() {
        validate();
        String escapedRawLocator = escapeEcmaScript(this.iterableLocator.getRawLocator());
        String countFunction;

        if (iterableLocator.getLocationStrategy() == JQUERY) {
            countFunction = "getJQueryCount";
        } else {
            countFunction = "getXPathCount";
        }

        return js("selenium.{0}('{1}') == {2}").parametrize(countFunction, escapedRawLocator, count);

    }

    private void validate() {
        Validate.notNull(iterableLocator, "iterableLocator should not be null");
        Validate.notNull(count, "count should not be null");
    }

    /**
     * Factory method.
     *
     * @return single instance of CountEquals
     */
    public static CountEquals getInstance() {
        return new CountEquals();
    }

    /**
     * <p>
     * Returns the CountEquals instance with given elementLocator set.
     * </p>
     *
     * <p>
     * From this locator will be the count of elements.
     * </p>
     *
     * @param iterableLocator the element locator
     * @return the CountEquals object with preset locator
     */
    public CountEquals locator(IterableLocator<?> iterableLocator) {
        Validate.notNull(iterableLocator, "iterableLocator should not be null");

        if (iterableLocator.getLocationStrategy() != JQUERY && iterableLocator.getLocationStrategy() != XPATH) {
            throw new IllegalArgumentException("Only XPath and JQuery locators are supported for counting");
        }

        CountEquals copy = copy();
        copy.iterableLocator = iterableLocator;

        return copy;
    }

    /**
     * <p>
     * Returns the CountEquals instance with the count set.
     * </p>
     *
     * <p>
     * For this count of elements will condition wait.
     * </p>
     *
     * @param count it should wait for this element count
     * @return the CountEquals object with preset value
     */
    public CountEquals count(int count) {
        CountEquals copy = copy();
        copy.count = count;

        return copy;
    }

    /**
     * Returns the exact copy of this ElementPresent object.
     *
     * @return the copy of this CountEquals object
     */
    private CountEquals copy() {
        CountEquals copy = new CountEquals();
        copy.iterableLocator = iterableLocator;
        copy.count = count;
        return copy;
    }
}
