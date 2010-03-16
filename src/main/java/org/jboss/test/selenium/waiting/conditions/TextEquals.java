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
import org.jboss.test.selenium.waiting.Condition;

import com.thoughtworks.selenium.Selenium;

public class TextEquals implements Condition {
	Selenium selenium;
	String locator;
	String text;

	public boolean isTrue() {
		Validate.notNull(locator);
		Validate.notNull(text);
		
		return selenium.getText(locator).equals(text);
	}

	protected TextEquals(Selenium selenium) {
		Validate.notNull(selenium);
		this.selenium = selenium;
	}

	public TextEquals locator(String locator) {
		Validate.notNull(locator);

		TextEquals copy = copy();
		copy.locator = locator;

		return copy;
	}

	public TextEquals text(String text) {
		Validate.notNull(text);

		TextEquals copy = copy();
		copy.text = text;

		return copy;
	}

	private TextEquals copy() {
		TextEquals copy = new TextEquals(this.selenium);
		copy.locator = locator;
		copy.text = text;
		return copy;
	}
}
