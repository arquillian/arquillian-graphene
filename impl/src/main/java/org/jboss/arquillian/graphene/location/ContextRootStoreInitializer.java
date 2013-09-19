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
package org.jboss.arquillian.graphene.location;

import java.lang.annotation.Annotation;
import java.net.URL;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.annotation.ClassScoped;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;
import org.jboss.arquillian.test.spi.event.enrichment.BeforeEnrichment;

public class ContextRootStoreInitializer {

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    @Inject
    @ClassScoped
    private InstanceProducer<ContextRootStore> contextRootStore;

    void setupLocationForClass(@Observes BeforeEnrichment event) {
        contextRootStore.set(new ContextRootStore(getContextRoot()));
    }

    private URL getContextRoot() {
        URL result = null;
        for (ResourceProvider provider : serviceLoader.get().all(ResourceProvider.class)) {
            if (provider.canProvide(URL.class)) {
                result = (URL) provider.lookup(new ArquillianResourceAnnotation());
            }
        }
        return result;
    }

    @SuppressWarnings("all")
    private class ArquillianResourceAnnotation implements ArquillianResource, Annotation {

        @Override
        public Class<? extends Annotation> annotationType() {
            return ArquillianResource.class;
        }

        @Override
        public Class<?> value() {
            return ArquillianResource.class;
        }
    }
}
