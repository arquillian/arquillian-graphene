/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.spi.javascript;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;

/**
 * <p>
 * Encapsulates JavaScript definitions.
 * </p>
 *
 * <p>
 * Able to load JavaScript code from file or from classpath resource.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class JavaScript {

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 4;

    /** The source code */
    private String source;

    /**
     * Instantiates a new JavaScript.
     *
     * @param source the JavaScript source code
     */
    public JavaScript(String source) {
        if (source == null) {
            throw new IllegalArgumentException("The javascript source code can't be null.");
        }
        this.source = source;
    }

    /**
     * Loads the JavaScript from file.
     *
     * @param sourceFile the source file
     * @return the JavaScript object loaded from file
     * @throws RuntimeException when failed to load a script
     */
    public static JavaScript fromFile(File sourceFile) {
        String sourceCode;
        try {
            sourceCode = inputStreamToString(new FileInputStream(sourceFile));
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Unable to find JavaScript source file '" + sourceFile + "'", e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load JavaScript from file '" + sourceFile + "'", e);
        }
        return fromString(sourceCode);
    }

    /**
     * Loads the JavaScript from classpath resource.
     *
     * @param resourceName the resource name, e.g. "org/jboss/test/..."
     * @return the JavaScript object loaded from classpath resource
     * @throws RuntimeException when failed to load a script
     */
    public static JavaScript fromResource(String resourceName) {
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(resourceName);

        if (inputStream == null) {
            throw new IllegalStateException("Can't open the '" + resourceName + "' resource.");
        }

        String sourceCode;

        try {
            sourceCode = inputStreamToString(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load JavaScript from resource with name '" + resourceName + "'", e);
        }

        return fromString(sourceCode);
    }

    /**
     * The factory method for JavaScript object
     *
     * @param source the JavaScript source code
     * @return the new JavaScript object with predefined JavaScript code
     */
    public static JavaScript fromString(String source) {
        return new JavaScript(source);
    }

    /**
     * Joins this JavaScript object with another JavaScript object to single JavaScript.
     *
     * @param javaScriptToJoin the JavaScript object we want to join with
     * @return the joined JavaScript object
     */
    public JavaScript join(JavaScript javaScriptToJoin) {
        return fromString(this.source + '\n' + javaScriptToJoin.source);
    }

    /**
     * Append the JavaScript part to end of the this JavaScript
     *
     * @param sourceCodeToAppend JavaScript source code to append on the end of this JavaScript
     * @return this JavaScript with javaScriptPartToAppend appended
     */
    public JavaScript append(String sourceCodeToAppend) {
        return fromString(this.source + sourceCodeToAppend);
    }

    /**
     * Returns the source of hold by this {@link JavaScript} object instance.
     */
    public String getSourceCode() {
        return source;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return getSourceCode();
    }

    private static String inputStreamToString(InputStream inputStream) throws IOException {
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
