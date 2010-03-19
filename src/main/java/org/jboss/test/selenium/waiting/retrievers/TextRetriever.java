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
import org.jboss.test.selenium.locator.ElementLocator;
import org.jboss.test.selenium.waiting.Retriever;
import org.jboss.test.selenium.waiting.ajax.JavaScriptRetriever;
import org.jboss.test.selenium.waiting.conversion.Convertor;
import org.jboss.test.selenium.waiting.conversion.PassOnConvertor;

import static org.jboss.test.selenium.utils.text.LocatorFormat.format;

public class TextRetriever implements Retriever<String>, JavaScriptRetriever<String> {
	AjaxSelenium selenium;
	ElementLocator elementLocator;

	public String retrieve() {
		Validate.notNull(elementLocator);
		
		return selenium.getText(elementLocator);
	}
	
	public String convertToJavaScript(String oldValue) {
	    return oldValue;
    }

    public String convertToResult(String retrieved) {
        return retrieved;
    }

    public JavaScript getJavaScriptRetrieve() {
        return new JavaScript(format("selenium.getText('{0}')", elementLocator.getAsString()));
    }
	
	protected TextRetriever() {
	}
	
	public static TextRetriever getInstance() {
	    return new TextRetriever();
	}

	public TextRetriever locator(ElementLocator elementLocator) {
		Validate.notNull(elementLocator);

		TextRetriever copy = copy();
		copy.elementLocator = elementLocator;

		return copy;
	}

	private TextRetriever copy() {
		TextRetriever copy = new TextRetriever();
		copy.elementLocator = elementLocator;
		return copy;
	}

    public Convertor<String, String> getConvertor() {
        return new PassOnConvertor<String>();
    }
}
