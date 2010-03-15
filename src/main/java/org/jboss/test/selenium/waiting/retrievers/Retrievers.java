package org.jboss.test.selenium.waiting.retrievers;

import com.thoughtworks.selenium.Selenium;

public class Retrievers {
	private TextRetriever textRetriever;
	
	public Retrievers(Selenium selenium) {
		this.textRetriever = new TextRetriever(selenium);
	}

	public TextRetriever getTextRetriever() {
		return textRetriever;
	}

	public void setTextRetriever(TextRetriever textEquals) {
		this.textRetriever = textEquals;
	}
}