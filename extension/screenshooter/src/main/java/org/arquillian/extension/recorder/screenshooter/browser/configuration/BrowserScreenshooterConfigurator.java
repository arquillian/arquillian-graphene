/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
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
package org.arquillian.extension.recorder.screenshooter.browser.configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.arquillian.extension.recorder.screenshooter.ScreenshooterConfiguration;
import org.arquillian.extension.recorder.screenshooter.ScreenshooterConfigurator;
import org.arquillian.extension.recorder.screenshooter.event.ScreenshooterExtensionConfigured;
import org.arquillian.recorder.reporter.ReporterConfiguration;
import org.arquillian.recorder.reporter.event.ReportingExtensionConfigured;
import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.config.descriptor.api.ExtensionDef;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;

/**
 * Observes:
 * <ul>
 * <li>{@link ReportingExtensionConfigured}</li>
 * <ul>
 * Produces {@link ApplicationScoped}:
 * <ul>
 * <li>{@link ScreenshooterConfiguration}</li>
 * </ul>
 * Fires:
 * <ul>
 * <li>{@link ScreenshooterExtensionConfigured}</li>
 * </ul>
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 *
 */
public class BrowserScreenshooterConfigurator extends ScreenshooterConfigurator {

    private static final Logger logger = Logger.getLogger(BrowserScreenshooterConfigurator.class.getSimpleName());

    @Inject
    @ApplicationScoped
    private InstanceProducer<ScreenshooterConfiguration> configuration;

    @Inject
    private Instance<ReporterConfiguration> reporterConfiguration;

    @Inject
    private Event<ScreenshooterExtensionConfigured> extensionConfiguredEvent;

    public void configureExtension(@Observes ReportingExtensionConfigured event, ArquillianDescriptor descriptor) {
        BrowserScreenshooterConfiguration conf = new BrowserScreenshooterConfiguration(reporterConfiguration.get());

        for (ExtensionDef extension : descriptor.getExtensions()) {
            if (extension.getExtensionName().equals(EXTENSION_NAME)) {
                conf.setConfiguration(extension.getExtensionProperties());
                conf.validate();
                break;
            }
        }

        this.configuration.set(conf);

        if (logger.isLoggable(Level.INFO)) {
            System.out.println("Configuration of Arquillian Browser Screenshooter:");
            System.out.println(this.configuration.get().toString());
        }

        extensionConfiguredEvent.fire(new ScreenshooterExtensionConfigured());
    }
}
