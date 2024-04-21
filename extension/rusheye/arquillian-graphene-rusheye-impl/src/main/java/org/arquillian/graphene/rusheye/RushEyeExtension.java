package org.arquillian.graphene.rusheye;

import org.arquillian.graphene.rusheye.configuration.RushEyeConfigurator;
import org.arquillian.graphene.rusheye.enricher.RushEyeEnricher;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.test.spi.TestEnricher;


public class RushEyeExtension implements LoadableExtension{

    public void register(ExtensionBuilder builder) {
    	builder.observer(RushEyeConfigurator.class);
    	builder.service(TestEnricher.class, RushEyeEnricher.class);
    }
}
