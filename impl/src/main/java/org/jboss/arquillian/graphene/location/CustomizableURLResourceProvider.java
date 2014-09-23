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
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.container.test.impl.enricher.resource.URLResourceProvider;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneConfiguration;
import org.jboss.arquillian.test.api.ArquillianResource;

public class CustomizableURLResourceProvider extends URLResourceProvider {

    @Inject
    private Instance<GrapheneConfiguration> grapheneConfiguration;

    @Override
    public boolean canProvide(Class<?> type) {
        return super.canProvide(type);
    }

    @Override
    public Object lookup(ArquillianResource resource, Annotation... qualifiers) {
        URL url = (URL) super.doLookup(resource, qualifiers);

        if (url == null) {

            URL customURL;

            String grapheneCustomURL = grapheneConfiguration.get().getUrl();

            if (grapheneCustomURL != null) {

                try {
                    customURL = new URL(grapheneCustomURL);
                    url = customURL;
                } catch (MalformedURLException ex) {
                    throw new IllegalStateException("Configured custom URL from GrapheneConfiguration should be already a valid URL.");
                }
            }
        }

        return url;
    }
}
