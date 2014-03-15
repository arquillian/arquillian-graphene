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
package org.jboss.arquillian.graphene;

import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.drone.spi.DroneInstanceEnhancer;
import org.jboss.arquillian.graphene.configuration.GrapheneConfigurationResourceProvider;
import org.jboss.arquillian.graphene.configuration.GrapheneConfigurator;
import org.jboss.arquillian.graphene.container.ServletURLLookupService;
import org.jboss.arquillian.graphene.container.ServletURLLookupServiceImpl;
import org.jboss.arquillian.graphene.enricher.FieldAccessValidatorEnricher;
import org.jboss.arquillian.graphene.enricher.GrapheneContextProvider;
import org.jboss.arquillian.graphene.enricher.GrapheneEnricher;
import org.jboss.arquillian.graphene.enricher.InFrameEnricher;
import org.jboss.arquillian.graphene.enricher.JavaScriptEnricher;
import org.jboss.arquillian.graphene.enricher.PageFragmentEnricher;
import org.jboss.arquillian.graphene.enricher.PageObjectEnricher;
import org.jboss.arquillian.graphene.enricher.SeleniumResourceProvider;
import org.jboss.arquillian.graphene.enricher.WebElementEnricher;
import org.jboss.arquillian.graphene.enricher.WebElementWrapperEnricher;
import org.jboss.arquillian.graphene.integration.GrapheneEnhancer;
import org.jboss.arquillian.graphene.location.ContextRootStoreInitializer;
import org.jboss.arquillian.graphene.location.LocationEnricher;
import org.jboss.arquillian.graphene.location.decider.FileLocationDecider;
import org.jboss.arquillian.graphene.location.decider.HTTPLocationDecider;
import org.jboss.arquillian.graphene.location.decider.ResourceLocationDecider;
import org.jboss.arquillian.graphene.spi.enricher.SearchContextTestEnricher;
import org.jboss.arquillian.graphene.spi.location.LocationDecider;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class GrapheneExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        builder.service(GrapheneRuntime.class, DefaultGrapheneRuntime.class);
        builder.observer(GrapheneRuntimeInitializer.class);

        builder.service(DroneInstanceEnhancer.class, GrapheneEnhancer.class);
        /* Configurator */
        builder.observer(GrapheneConfigurator.class);
        /* Component Objects */
        builder.service(TestEnricher.class, GrapheneEnricher.class);
        builder.service(TestEnricher.class, LocationEnricher.class);
        builder.service(ContextRootStoreInitializer.class, ContextRootStoreInitializer.class);
        builder.service(LocationEnricher.class, LocationEnricher.class);
        builder.observer(ContextRootStoreInitializer.class);
        builder.service(ServletURLLookupService.class, ServletURLLookupServiceImpl.class);
        builder.service(SearchContextTestEnricher.class, WebElementEnricher.class);
        builder.service(SearchContextTestEnricher.class, PageFragmentEnricher.class);
        builder.service(SearchContextTestEnricher.class, PageObjectEnricher.class);
        builder.service(SearchContextTestEnricher.class, WebElementWrapperEnricher.class);
        builder.service(SearchContextTestEnricher.class, FieldAccessValidatorEnricher.class);
        builder.service(SearchContextTestEnricher.class, InFrameEnricher.class);
        /** Javascript enrichment */
        builder.service(SearchContextTestEnricher.class, JavaScriptEnricher.class);
        /* Resource Providers */
        builder.service(ResourceProvider.class, GrapheneContextProvider.class);
        builder.service(ResourceProvider.class, GrapheneConfigurationResourceProvider.class);
        SeleniumResourceProvider.registerAllProviders(builder);
        /* Location deciders */
        builder.service(LocationDecider.class, HTTPLocationDecider.class);
        builder.service(LocationDecider.class, ResourceLocationDecider.class);
        builder.service(LocationDecider.class, FileLocationDecider.class);
    }
}
