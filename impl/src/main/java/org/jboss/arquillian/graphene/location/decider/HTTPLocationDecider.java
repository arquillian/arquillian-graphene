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
package org.jboss.arquillian.graphene.location.decider;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.graphene.location.ContextRootStore;
import org.jboss.arquillian.graphene.location.exception.LocationException;
import org.jboss.arquillian.graphene.spi.location.LocationDecider;
import org.jboss.arquillian.graphene.spi.location.Scheme;

/**
 * Decides location of some HTTP URL.
 *
 * @author <a href="smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
public class HTTPLocationDecider implements LocationDecider {

    private final Scheme scheme = new Scheme.HTTP();

    @Inject
    private Instance<ContextRootStore> locationStore;

    @Override
    public Scheme canDecide() {
        return scheme;
    }

    @Override
    public String decide(String location) {

        URI uri = null;

        try {
            uri = new URI(location);
            if (!uri.isAbsolute()) {
                return getURLFromLocationWithRoot(location);
            }
        } catch (URISyntaxException e) {
            return getURLFromLocationWithRoot(location);
        }

        try {
            return uri.toURL().toExternalForm();
        } catch (MalformedURLException ex) {
            throw new LocationException(String.format("Location you want to decide (%s) can not be "
                + "converted to URL.", location));
        }
    }

    private String getURLFromLocationWithRoot(String location) {

        URL contextRoot = locationStore.get().getURL();

        if (contextRoot != null) {
            try {
                return new URL(contextRoot, location).toExternalForm();
            } catch (MalformedURLException ex) {
                throw new LocationException("URL to construct is malformed.", ex.getCause());
            }
        }

        throw new LocationException(String.format(
            "The location %s is not valid URI and no contextRoot was discovered to treat it as relative URL",
            location));
    }

}
