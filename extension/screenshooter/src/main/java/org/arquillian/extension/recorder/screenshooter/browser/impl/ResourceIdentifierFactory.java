package org.arquillian.extension.recorder.screenshooter.browser.impl;

import java.util.UUID;

import org.arquillian.extension.recorder.ResourceIdentifier;
import org.arquillian.extension.recorder.ResourceMetaData;
import org.arquillian.extension.recorder.ResourceType;
import org.arquillian.extension.recorder.When;

public class ResourceIdentifierFactory {

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
                sb.append(UUID.randomUUID().toString());
            } else {
                sb.append(metaData.getOptionalDescription());
            }
            if (when != null) {
                sb.append("_");
                sb.append(when.toString());
            }
            if (resourceType != null) {
                sb.append(".");
                sb.append(resourceType.toString());
            }
            return sb.toString();
        }
    }
}
