/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.configuration;


import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.context.GrapheneConfigurationContext;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneConfigured;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneUnconfigured;
import org.jboss.arquillian.test.spi.annotation.SuiteScoped;
import org.jboss.arquillian.test.spi.event.suite.AfterClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.jboss.logging.Logger;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 */
public class GrapheneConfigurator {

    private static final Logger LOGGER = Logger.getLogger(GrapheneConfigurator.class);

    @Inject
    @SuiteScoped
    private InstanceProducer<GrapheneConfiguration> configuration;

    @Inject
    private Event<GrapheneConfigured> configuredEvent;

    @Inject
    private Event<GrapheneUnconfigured> unconfiguredEvent;

    public void configureGraphene(@Observes BeforeClass event, ArquillianDescriptor descriptor) {
        GrapheneConfiguration c = new GrapheneConfiguration();
        c.configure(descriptor, Default.class).validate();
        this.configuration.set(c);
        this.configuredEvent.fire(new GrapheneConfigured());
        GrapheneConfigurationContext.set(c);
    }

    public void unconfigureGraphene(@Observes AfterClass event) {
        GrapheneConfigurationContext.reset();
        unconfiguredEvent.fire(new GrapheneUnconfigured());
    }

}
