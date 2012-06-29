package org.jboss.arquillian.graphene.spi.components.common;

import org.openqa.selenium.WebElement;


public class AbstractComponent implements Component {
	
	private RootReference rootReference = new RootReference();
	
	protected RootReference getRootReference() {
		return rootReference;
	}
	
	@Override
	public void setRoot(WebElement root) {
		rootReference.set(root);
	}
	
	@Override
	public WebElement getRoot() {
		return rootReference.get();
	}
}
