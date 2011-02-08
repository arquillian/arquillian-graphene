package org.jboss.arquillian.ajocado.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

public class IOUtils {
	private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

	public static String toString(InputStream inputStream) throws IOException {
		StringWriter output = new StringWriter();
		InputStreamReader input = new InputStreamReader(inputStream);
		char[] buffer = new char[DEFAULT_BUFFER_SIZE];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toString();
	}
}
