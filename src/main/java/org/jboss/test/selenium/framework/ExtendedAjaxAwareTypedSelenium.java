package org.jboss.test.selenium.framework;

import java.net.URL;



public class ExtendedAjaxAwareTypedSelenium extends ExtendedTypedSelenium {
	public ExtendedAjaxAwareTypedSelenium(String serverHost, int serverPort, String browserStartCommand, URL browserURL) {
		selenium = new ExtendedAjaxAwareSelenium(serverHost, serverPort, browserStartCommand, browserURL);
	}

	private class ExtendedAjaxAwareSelenium extends ExtendedSelenium {
		public ExtendedAjaxAwareSelenium(String serverHost, int serverPort, String browserStartCommand, URL browserURL) {
			super(new AjaxAwareCommandProcessor(serverHost, serverPort, browserStartCommand, browserURL.toString()));
		}
	}
}
