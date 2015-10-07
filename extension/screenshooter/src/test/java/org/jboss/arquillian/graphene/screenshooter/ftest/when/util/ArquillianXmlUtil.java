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

import java.util.Arrays;
import java.util.List;

import org.arquillian.extension.recorder.When;

/**
 * Util class for managing arquillian.xml file
 *
 * @author <a href="mailto:mjobanek@redhat.com">Matous Jobanek</a>
 */
public class ArquillianXmlUtil {

    /**
     * Set screenshots related properties according to the given parameters - for every given {@link When} it sets the
     * appropriate parameter to true (the associate screenshots are taken) otherwise it sets it to false.
     *
     * @param whenArray An array of  {@link When}
     */
    public static void setProperties(When... whenArray) {
        List<When> whens = Arrays.asList(whenArray);
        System.setProperty("screenshot.take.before.test", String.valueOf(whens.contains(When.BEFORE)));
        System.setProperty("screenshot.take.after.test", String.valueOf(whens.contains(When.AFTER)));
        System.setProperty("screenshot.take.on.every.action", String.valueOf(whens.contains(When.ON_EVERY_ACTION)));
        System.setProperty("screenshot.take.when.test.filed", String.valueOf(whens.contains(When.FAILED)));
    }

}
