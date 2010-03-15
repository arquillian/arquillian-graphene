package org.jboss.test.selenium.waiting.retrievers;

import org.apache.commons.lang.Validate;
import org.jboss.test.selenium.waiting.Retrieve;

import com.thoughtworks.selenium.Selenium;

public class AttributeRetriever implements Retrieve<String> {
	Selenium selenium;
	String attributeLocator;
	String text;

	public String retrieve() {
		Validate.notNull(attributeLocator);
		Validate.notNull(text);
		
		return selenium.getAttribute(attributeLocator);
	}
	
	protected AttributeRetriever(Selenium selenium) {
		Validate.notNull(selenium);
		this.selenium = selenium;
	}

	public AttributeRetriever attributeLocator(String attributeLocator) {
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
		AttributeRetriever copy = new AttributeRetriever(this.selenium);
		copy.attributeLocator = attributeLocator;
		copy.text = text;
		return copy;
	}
}
