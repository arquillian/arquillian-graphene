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
