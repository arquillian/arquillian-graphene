/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
