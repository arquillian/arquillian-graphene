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
package org.jboss.arquillian.graphene.screenshooter.ftest.when.util;

import java.io.File;

/**
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class Constants {

    public static final String TEST_METHOD_NAME = "testMethod";

    public static final String SAMPLE_HTML_PATH = ("file://" + System.getProperty("user.dir")
        + "/src/test/resources/org/jboss/arquillian/graphene/screenshooter/ftest/sample.html").replace("/",
        File.separator);

    public static final String SCREENSHOTS_DIRECTORY =
        (System.getProperty("user.dir") + "/target/screenshots/").replace("/", File.separator);

    public static final String PATH_TO_ARQ_XML =
        (System.getProperty("user.dir") + "/src/test/resources/when/arquillian.xml").replace("/", File.separator);
}
