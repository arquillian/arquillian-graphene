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
package org.jboss.test.selenium.waiting.retrievers;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.AjaxSeleniumProxy;
import org.jboss.test.selenium.locator.ElementLocator;
import org.jboss.test.selenium.waiting.ajax.JavaScriptRetriever;
import org.jboss.test.selenium.waiting.conversion.Convertor;
import org.jboss.test.selenium.waiting.conversion.PassOnConvertor;
import org.jboss.test.selenium.waiting.selenium.SeleniumRetriever;

import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;
import static org.jboss.test.selenium.encapsulated.JavaScript.js;

/**
 * Retrieves the text for given elementLocator
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class TextRetriever implements SeleniumRetriever<String>, JavaScriptRetriever<String> {

    /**
     * Proxy to local selenium instance
     */
    private AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();
    
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
    public String retrieve() {
        Validate.notNull(elementLocator);

        return selenium.getText(elementLocator);
    }

    /**
     * JavaScript expression to retrieve text value from element given by elementLocator
     */
    public JavaScript getJavaScriptRetrieve() {
        return js(format("selenium.getText('{0}')", elementLocator.getAsString()));
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
     * @param elementLocator
     *            the element locator to preset
     * @return the TextRetriever preset with elementLocator of given value
     */
    public TextRetriever locator(ElementLocator<?> elementLocator) {
        Validate.notNull(elementLocator);

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
    public Convertor<String, String> getConvertor() {
        return new PassOnConvertor<String>();
    }
}
