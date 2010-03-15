package org.jboss.test.selenium.encapsulated;

public class LogLevel {
	String logLevelName;

	public LogLevel(String logLevelName) {
		this.logLevelName = logLevelName;
	}

	public String getLogLevelName() {
		return logLevelName;
	}

	@Override
	public String toString() {
		return getLogLevelName();
	}

	public static final LogLevel DEBUG = new LogLevel("debug");
	public static final LogLevel INFO = new LogLevel("info");
	public static final LogLevel WARN = new LogLevel("warn");
	public static final LogLevel ERROR = new LogLevel("error");
	public static final LogLevel OFF = new LogLevel("off");
}
