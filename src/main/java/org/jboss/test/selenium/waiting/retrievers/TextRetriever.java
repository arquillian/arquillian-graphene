package org.jboss.test.selenium.waiting.retrievers;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.waiting.Retrieve;

import com.thoughtworks.selenium.Selenium;

public class TextRetriever implements Retrieve<String> {
	Selenium selenium;
	String locator;
	String text;

	public String retrieve() {
		Validate.notNull(locator);
		Validate.notNull(text);
		
		return selenium.getText(locator);
	}
	
	protected TextRetriever(Selenium selenium) {
		Validate.notNull(selenium);
		this.selenium = selenium;
	}

	public TextRetriever locator(String locator) {
		Validate.notNull(locator);

		TextRetriever copy = copy();
		copy.locator = locator;

		return copy;
	}

	public TextRetriever text(String text) {
		Validate.notNull(text);

		TextRetriever copy = copy();
		copy.text = text;

		return copy;
	}

	private TextRetriever copy() {
		TextRetriever copy = new TextRetriever(this.selenium);
		copy.locator = locator;
		copy.text = text;
		return copy;
	}
}
