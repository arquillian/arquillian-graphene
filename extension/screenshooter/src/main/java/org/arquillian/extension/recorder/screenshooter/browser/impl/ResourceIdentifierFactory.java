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
package org.arquillian.extension.recorder.screenshooter.browser.impl;

import org.arquillian.extension.recorder.ResourceIdentifier;
import org.arquillian.extension.recorder.ResourceMetaData;
import org.arquillian.extension.recorder.ResourceType;
import org.arquillian.extension.recorder.When;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class ResourceIdentifierFactory {

    private static int COUNTER = 0;

    public static ResourceIdentifier<ResourceType> getResoruceIdentifier(ResourceMetaData metaData, When when) {
        return new BrowserResourceIdentifier(metaData, when);
    }

    private static class BrowserResourceIdentifier implements ResourceIdentifier<ResourceType> {

        private ResourceMetaData metaData;
        private When when;

        public BrowserResourceIdentifier(ResourceMetaData metaData, When when) {
            super();
            this.metaData = metaData;
            this.when = when;
        }

        @Override
        public String getIdentifier(ResourceType resourceType) {
            StringBuilder sb = new StringBuilder();
            if (metaData == null || when == null || metaData.getOptionalDescription() == null
                    || metaData.getOptionalDescription().isEmpty()) {
                sb.append("unknown" + COUNTER++);
            } else {
                sb.append(metaData.getOptionalDescription());
            }
            if (when != null) {
                sb.append("_");
                if(when == When.AFTER || when == When.FAILED) {
                    sb.setLength(0);
                }
                if(when == When.ON_EVERY_ACTION && metaData.getOptionalDescription().equals("get0")) {
                    sb.setLength(0);
                    sb.append(When.BEFORE);
                } else if(when == When.BEFORE) {
                    sb.setLength(0);
                    sb.append(When.BEFORE);
                } else {
                    sb.append(when.toString());
                }
            }
            if (resourceType != null) {
                sb.append(".");
                sb.append(resourceType.toString());
            }
            return sb.toString();
        }
    }
}
