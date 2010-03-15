package org.jboss.test.selenium.locator.type;

public interface LocationStrategy {
	public String getStrategyName();
	
	public static final LocationStrategy CSS = new CssStrategy();
	public static final LocationStrategy DOM = new DomStrategy();
	public static final LocationStrategy IDENTIFIER = new IdentifierStrategy();
	public static final LocationStrategy ID = new IdStrategy();
	public static final LocationStrategy JQUERY = new JQueryStrategy();
	public static final LocationStrategy LINK = new LinkStrategy();
	public static final LocationStrategy NAME = new NameStrategy();
	public static final LocationStrategy XPATH = new XpathStrategy();
}