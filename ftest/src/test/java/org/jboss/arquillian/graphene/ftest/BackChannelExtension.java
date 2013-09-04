package org.jboss.arquillian.graphene.ftest;
import org.jboss.arquillian.container.test.impl.enricher.resource.URLResourceProvider;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;


public class BackChannelExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.override(ResourceProvider.class, URLResourceProvider.class, BackChannel.class);
    }

}
