package org.jboss.arquillian.graphene.spi.components.common;

import javax.inject.Inject;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;


public class AbstractComponent implements Component {
	
    protected WebDriver webDriver;
    
    protected final int GUI_WAIT = 2;
    
	private RootReference rootReference = new RootReference();
	
	protected RootReference getRootReference() {
		return rootReference;
	}
	
	public WebDriver getWebDriver() {
        return webDriver;
    }

	public void setWebDriver(WebDriver webDriver) {
        this.webDriver = webDriver;
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
