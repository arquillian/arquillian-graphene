/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.test.selenium.encapsulated;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import static org.jboss.test.selenium.utils.text.LocatorFormat.format;

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

    public static JavaScript fromFile(File sourceFile) {
        String sourceCode;
        try {
            sourceCode = IOUtils.toString(new FileReader(sourceFile));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException(format("Unable to find JavaScript source file '{0}'", sourceFile), e);
        } catch (IOException e) {
            throw new RuntimeException(format("Unable to load JavaScript from file '{0}'", sourceFile), e);
        }
        return new JavaScript(sourceCode);
    }

    public static JavaScript fromResource(String resourceName) {
        InputStream inputStream;
        inputStream = ClassLoader.getSystemResourceAsStream(resourceName);

        String sourceCode;

        try {
            sourceCode = IOUtils.toString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(format("Unable to load JavaScript from resource with name '{0}'", resourceName), e);
        }

        return new JavaScript(sourceCode);
    }
}
