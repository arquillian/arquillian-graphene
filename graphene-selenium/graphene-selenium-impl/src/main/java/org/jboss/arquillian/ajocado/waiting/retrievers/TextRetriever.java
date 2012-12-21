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
package org.jboss.arquillian.ajocado.waiting.retrievers;

import static org.jboss.arquillian.ajocado.javascript.JavaScript.js;

import org.apache.commons.lang3.StringEscapeUtils;
import org.jboss.arquillian.ajocado.framework.GrapheneSelenium;
import org.jboss.arquillian.ajocado.framework.GrapheneSeleniumContext;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.element.ElementLocator;
import org.jboss.arquillian.ajocado.waiting.conversion.Convertor;
import org.jboss.arquillian.ajocado.waiting.conversion.PassOnConvertor;
import org.jboss.arquillian.core.spi.Validate;

/**
 * Retrieves the text for given elementLocator
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TextRetriever extends AbstractRetriever<String> implements Retriever<String> {

    /**
     * Proxy to local selenium instance
     */
    private GrapheneSelenium selenium = GrapheneSeleniumContext.getProxy();

    /** The element locator. */
    private ElementLocator<?> elementLocator;

    /**
     * Instantiates a new text retriever.
     */
    protected TextRetriever() {
    }

    /**
     * Retrieves the text value from element given by elementLocator
     */
    @Override
    public String retrieve() {
        Validate.notNull(elementLocator, "elementLocator should not be null");

        return selenium.getText(elementLocator);
    }

    /**
     * JavaScript expression to retrieve text value from element given by elementLocator
     */
    @Override
    public JavaScript getJavaScriptRetrieve() {
        String escapedLocator = StringEscapeUtils.escapeEcmaScript(elementLocator.inSeleniumRepresentation());
        return js("selenium.getText('{0}')").parametrize(escapedLocator);
    }

    /**
     * Factory method.
     *
     * @return single instance of TextRetriever
     */
    public static TextRetriever getInstance() {
        return new TextRetriever();
    }

    /**
     * Gets a TextRetriever object preset with elementLocator to given value.
     *
     * @param elementLocator the element locator to preset
     * @return the TextRetriever preset with elementLocator of given value
     */
    public TextRetriever locator(ElementLocator<?> elementLocator) {
        Validate.notNull(elementLocator, "elementLocator should not be null");

        TextRetriever copy = copy();
        copy.elementLocator = elementLocator;

        return copy;
    }

    /**
     * Returns a copy of this textRetriever with exactly same settings.
     *
     * Keeps the immutability of this class.
     *
     * @return the exact copy of this textRetriever
     */
    private TextRetriever copy() {
        TextRetriever copy = new TextRetriever();
        copy.elementLocator = elementLocator;
        return copy;
    }

    /**
     * Uses {@link PassOnConvertor} to pass the JavaScript result to result value.
     */
    @Override
    public Convertor<String, String> getConvertor() {
        return new PassOnConvertor<String>();
    }
}
