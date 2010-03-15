package org.jboss.test.selenium.encapsulated;

public class JavaScript {
	String javaScript;

	public JavaScript(String javaScript) {
		this.javaScript = javaScript;
	}
	
	public String getJavaScript() {
		return javaScript;
	}
	
	@Override
	public String toString() {
		return getJavaScript();
	}
}
