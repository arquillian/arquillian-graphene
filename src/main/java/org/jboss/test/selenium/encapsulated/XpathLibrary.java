package org.jboss.test.selenium.encapsulated;

public class XpathLibrary {
	String xpathLibraryName;

	public XpathLibrary(String xpathLibraryName) {
		this.xpathLibraryName = xpathLibraryName;
	}

	public String getXpathLibraryName() {
		return xpathLibraryName;
	}

	@Override
	public String toString() {
		return getXpathLibraryName();
	}

	public static final XpathLibrary DEFAULT = new XpathLibrary("default");
	public static final XpathLibrary AJAXSLT = new XpathLibrary("ajaxslt");
	public static final XpathLibrary JAVASCRIPT_XPATH = new XpathLibrary("javascript-xpath");
}
