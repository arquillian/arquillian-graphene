package org.arquillian.graphene.rusheye.configuration;

import org.arquillian.graphene.rusheye.exception.RushEyeConfigurationException;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.config.descriptor.api.ExtensionDef;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RushEyeConfigurator {
	
    private static final Logger logger = Logger.getLogger(RushEyeConfigurator.class.getName());

    private static final String EXTENSION_NAME = "rusheye";

    @Inject
    @ApplicationScoped
    private InstanceProducer<RushEyeConfiguration> rusheyeConfiguration;


    public void onArquillianDescriptor(@Observes ArquillianDescriptor arquillianDescriptor) throws RushEyeConfigurationException {

        final RushEyeConfiguration rusheyeConfiguration = new RushEyeConfiguration();

        for (final ExtensionDef extension : arquillianDescriptor.getExtensions()) {
            if (extension.getExtensionName().equals(EXTENSION_NAME)) {
            	rusheyeConfiguration.setConfiguration(extension.getExtensionProperties());
            	rusheyeConfiguration.validate();
            	rusheyeConfiguration.convertToSystemProperties();
                break;
            }
        }

        this.rusheyeConfiguration.set(rusheyeConfiguration);
        RushEyeConfigExporter.set(rusheyeConfiguration);
        logger.log(Level.CONFIG, "Configuration of Arquillian Graphene RushEye extension:");
        logger.log(Level.CONFIG, rusheyeConfiguration.toString());

    }
}
