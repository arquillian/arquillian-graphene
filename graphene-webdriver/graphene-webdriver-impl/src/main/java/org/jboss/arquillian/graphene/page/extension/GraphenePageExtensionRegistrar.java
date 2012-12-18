/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.page.extension;

import java.util.Collection;

import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.context.GraphenePageExtensionsContext;
import org.jboss.arquillian.graphene.spi.page.PageExtensionProvider;
import org.jboss.arquillian.graphene.spi.page.PageExtensionsCleaned;
import org.jboss.arquillian.graphene.spi.page.PageExtensionsReady;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.annotation.ClassScoped;
import org.jboss.arquillian.test.spi.event.suite.AfterClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class GraphenePageExtensionRegistrar {

    @Inject
    @ClassScoped
    private InstanceProducer<PageExtensionRegistry> pageExtensionRegistry;
    @Inject
    @ClassScoped
    private InstanceProducer<PageExtensionInstallatorProvider> pageExtensionInstallatorProvider;
    @Inject
    private Instance<ServiceLoader> serviceLoader;
    @Inject
    private Event<PageExtensionsReady> pageExtensionsReady;
    @Inject
    private Event<PageExtensionsCleaned> pageExtensionsCleaned;

    public void registerPageExtensionRegistry(@Observes BeforeClass event, TestClass testClass) {
        pageExtensionRegistry.set(new PageExtensionRegistryImpl());
        loadPageExtensions(testClass);
        GraphenePageExtensionsContext.setRegistry(pageExtensionRegistry.get());
        pageExtensionInstallatorProvider.set(new RemotePageExtensionInstallatorProvider(pageExtensionRegistry.get(), GrapheneContext.getProxy()));
        GraphenePageExtensionsContext.setInstallatorProvider(pageExtensionInstallatorProvider.get());
        pageExtensionsReady.fire(new PageExtensionsReady());
    }

    public void unregisterPageExtensionRegistry(@Observes AfterClass event) {
        pageExtensionRegistry.get().flush();
        GraphenePageExtensionsContext.reset();
        pageExtensionsCleaned.fire(new PageExtensionsCleaned());
    }

    protected void loadPageExtensions(TestClass testClass) {
        Collection<PageExtensionProvider> providers = serviceLoader.get().all(PageExtensionProvider.class);
        for (PageExtensionProvider provider: providers) {
            pageExtensionRegistry.get().register(provider.getPageExtensions(testClass));
        }
    }

}
