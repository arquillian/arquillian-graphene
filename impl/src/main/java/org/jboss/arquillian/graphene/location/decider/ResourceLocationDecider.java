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

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.graphene.location.LocationEnricher;
import org.jboss.arquillian.graphene.location.exception.LocationException;
import org.jboss.arquillian.graphene.page.Location;
import org.jboss.arquillian.graphene.page.UriScheme;
import org.jboss.arquillian.graphene.page.location.LocationDecider;

/**
 * Decides URL location of some Location with "resource" schema.
 *
 * @author <a href="smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
public class ResourceLocationDecider implements LocationDecider {

    @Override
    public UriScheme canDecide() {
        return UriScheme.RESOURCE;
    }

    @Override
    public URL decide(Location location) {
        Validate.notNull(location, "Location to decide can not be a null object.");

        if (location.scheme() != canDecide()) {
            throw new IllegalArgumentException(String.format("You want to decide location of scheme \"%s\" which can not be "
                + "decided by this decider which decides '%s'.", location.scheme(), canDecide()));
        }

        URI uri = null;

        try {
            uri = new URI(location.value());
        } catch (URISyntaxException ex) {
            throw new LocationException(String.format("Unable to convert '%s' to URI", location.value()), ex.getCause());
        }

        if (uri != null && location.scheme().toString().equals(uri.getScheme())) {
            String resourceName = uri.getSchemeSpecificPart();

            if (resourceName.startsWith("//")) {
                resourceName = resourceName.substring(2);
            }

            URL url = LocationEnricher.class.getClassLoader().getResource(resourceName);

            if (url == null) {
                throw new IllegalArgumentException(String.format("Resource '%s' specified by %s was not found",
                    resourceName, location.value()));
            }
            return url;
        }

        throw new LocationException(String.format("URI scheme (%s) of location you want to decide (%s) is not "
            + "the scheme this decider can decide (%s)", location.scheme(), location.value(), canDecide()));

    }

}
