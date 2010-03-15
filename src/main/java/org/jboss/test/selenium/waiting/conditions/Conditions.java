package org.jboss.test.selenium.waiting.conditions;

import com.thoughtworks.selenium.Selenium;

public class Conditions {
	private TextEquals textEquals;
	
	public Conditions(Selenium selenium) {
		this.textEquals = new TextEquals(selenium);
	}

	public TextEquals getTextEquals() {
		return textEquals;
	}

	public void setTextEquals(TextEquals textEquals) {
		this.textEquals = textEquals;
	}
}