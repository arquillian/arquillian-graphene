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
