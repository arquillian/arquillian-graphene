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
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.internal.Contextual;
import org.jboss.test.selenium.locator.AttributeLocator;
import org.jboss.test.selenium.waiting.Retrieve;

public class AttributeRetriever implements Retrieve<String>, Contextual {
	AjaxSelenium selenium;
	AttributeLocator attributeLocator;
	String text;

	public String retrieve() {
		Validate.notNull(attributeLocator);
		Validate.notNull(text);
		
		return selenium.getAttribute(attributeLocator);
	}
	
	protected AttributeRetriever() {
	}
	
	public static AttributeRetriever getInstance() {
        return new AttributeRetriever();
    }

	public AttributeRetriever attributeLocator(AttributeLocator attributeLocator) {
		Validate.notNull(attributeLocator);

		AttributeRetriever copy = copy();
		copy.attributeLocator = attributeLocator;

		return copy;
	}

	public AttributeRetriever text(String text) {
		Validate.notNull(text);

		AttributeRetriever copy = copy();
		copy.text = text;

		return copy;
	}

	private AttributeRetriever copy() {
		AttributeRetriever copy = new AttributeRetriever();
		copy.attributeLocator = attributeLocator;
		copy.text = text;
		return copy;
	}
}
