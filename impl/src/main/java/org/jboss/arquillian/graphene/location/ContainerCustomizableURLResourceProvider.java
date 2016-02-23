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

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.impl.enricher.resource.URLResourceProvider;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.arquillian.test.spi.enricher.resource.ResourceProvider;

/**
 * This resource provider is used if the Arquillian (core) resource provider for URLs is present on the classpath at
 * extension load time, i.e. you use the container integration option (see see https://docs.jboss.org/author/display/ARQGRA2/Framework+Integration+Options).
 * It will be used to ask the core resource provider first and only if the URL remains unknown, it
 * will fallback to the custom URL configured in Graphene configuration.
 */
public class ContainerCustomizableURLResourceProvider extends CustomizableURLResourceProvider {

    @Inject
    private Instance<TestClass> testClass;

    @Inject
    private Instance<ServiceLoader> loader;

    @Override
    protected URL doLookup(ArquillianResource resource, Annotation... qualifiers) {
        URL url = null;

        // check if there is any other URL provider on the classpath (Warp for example)
        for (ResourceProvider provider : loader.get().all(ResourceProvider.class)) {
            if (provider.canProvide(URL.class) && !(provider instanceof  CustomizableURLResourceProvider)) {
                url = (URL) provider.lookup(resource, qualifiers);
            }
        }

        if (url == null) {
            // if the class for core (Arquillian) URL resource provider is present, try if a fallback is possible
            ResourceProvider coreResourceProvider =
                loader.get().onlyOne(ResourceProvider.class, URLResourceProvider.class);
            if (coreResourceProvider != null && coreResourceProvider instanceof URLResourceProvider) {
                if (hasDeployment(testClass.get())) {
                    url = (URL) coreResourceProvider.lookup(resource, qualifiers);
                } else {
                    url = (URL) ((URLResourceProvider) coreResourceProvider).doLookup(resource, qualifiers);
                }
            }
        }

        return url;
    }

    private boolean hasDeployment(TestClass testClass) {
        return testClass.getMethods(Deployment.class).length != 0;
    }
}
