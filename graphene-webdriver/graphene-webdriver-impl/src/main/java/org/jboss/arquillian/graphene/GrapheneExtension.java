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
package org.jboss.arquillian.graphene;

import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.graphene.configuration.GrapheneConfigurationResourceProvider;
import org.jboss.arquillian.graphene.configuration.GrapheneConfigurator;
import org.jboss.arquillian.graphene.enricher.GrapheneEnricher;
import org.jboss.arquillian.graphene.enricher.PageFragmentEnricher;
import org.jboss.arquillian.graphene.enricher.PageObjectEnricher;
import org.jboss.arquillian.graphene.spi.enricher.SearchContextTestEnricher;
import org.jboss.arquillian.graphene.enricher.WebElementEnricher;
import org.jboss.arquillian.graphene.page.extension.GraphenePageExtensionRegistrar;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class GrapheneExtension implements LoadableExtension {

    @Override
    public void register(ExtensionBuilder builder) {
        /* Configurator */
        builder.observer(GrapheneConfigurator.class);
        /* Component Objects */
        builder.service(TestEnricher.class, GrapheneEnricher.class);
        builder.service(SearchContextTestEnricher.class, WebElementEnricher.class);
        builder.service(SearchContextTestEnricher.class, PageFragmentEnricher.class);
        builder.service(SearchContextTestEnricher.class, PageObjectEnricher.class);
        /* Page Extensions */
        builder.observer(GraphenePageExtensionRegistrar.class);
        /* Resource Providers */
        builder.service(ResourceProvider.class, GrapheneConfigurationResourceProvider.class);
    }

}
