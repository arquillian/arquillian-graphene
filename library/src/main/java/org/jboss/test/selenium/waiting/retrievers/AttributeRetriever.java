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
import org.jboss.test.selenium.framework.internal.Contextual;
import org.jboss.test.selenium.locator.AttributeLocator;
import org.jboss.test.selenium.waiting.Retriever;
import org.jboss.test.selenium.waiting.ajax.JavaScriptRetriever;
import org.jboss.test.selenium.waiting.conversion.Convertor;
import org.jboss.test.selenium.waiting.conversion.PassOnConvertor;

import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;

/**
 * Retrieves the attribute with given attributeLocator.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AttributeRetriever implements Retriever<String>, JavaScriptRetriever<String>, Contextual {

    /** The selenium. */
    AjaxSelenium selenium = AjaxSelenium.getCurrentContext(this);

    /** The attribute locator. */
    AttributeLocator attributeLocator;

    /**
     * Instantiates a new attribute retriever.
     */
    protected AttributeRetriever() {
    }

    /**
     * Retrieves the attribute value from element given by attributeLocator
     */
    public String retrieve() {
        Validate.notNull(attributeLocator);

        return selenium.getAttribute(attributeLocator);
    }

    /**
     * JavaScript expression to retrieve attribute value from element given by attributeLocator
     */
    public JavaScript getJavaScriptRetrieve() {
        return new JavaScript(format("selenium.getAttribute('{0}')", attributeLocator.getAsString()));
    }

    /**
     * Factory method.
     * 
     * @return single instance of AttributeRetriever
     */
    public static AttributeRetriever getInstance() {
        return new AttributeRetriever();
    }

    /**
     * Gets a AttributeRetriever object preset with attributeLocator to given value.
     * 
     * @param attributeLocator
     *            the attribute locator to preset
     * @return the AttributeRetriever preset with attributeLocator of given value
     */
    public AttributeRetriever attributeLocator(AttributeLocator attributeLocator) {
        Validate.notNull(attributeLocator);

        AttributeRetriever copy = copy();
        copy.attributeLocator = attributeLocator;

        return copy;
    }

    /**
     * Returns a copy of this attributeRetriever with exactly same settings.
     * 
     * Keeps the immutability of this class.
     * 
     * @return the exact copy of this attributeRetriever
     */
    private AttributeRetriever copy() {
        AttributeRetriever copy = new AttributeRetriever();
        copy.attributeLocator = attributeLocator;
        return copy;
    }

    /**
     * Uses {@link PassOnConvertor} to pass the JavaScript result to result value.
     */
    public Convertor<String, String> getConvertor() {
        return new PassOnConvertor<String>();
    }
}
