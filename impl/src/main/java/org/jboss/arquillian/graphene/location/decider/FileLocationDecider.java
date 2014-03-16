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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.graphene.location.exception.LocationException;
import org.jboss.arquillian.graphene.spi.location.LocationDecider;
import org.jboss.arquillian.graphene.spi.location.Scheme;

/**
 * Decides location of some {@link File}.
 *
 * @author <a href="smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
public class FileLocationDecider implements LocationDecider {

    private final Scheme scheme = new Scheme.FILE();

    @Override
    public Scheme canDecide() {
        return scheme;
    }

    @Override
    public String decide(String location) {
        Validate.notNull(location, "Location to decide can not be a null object.");

        if (!location.startsWith(scheme.toString())) {
            location = new StringBuilder(scheme.toString()).append(location).toString();
        }

        try {
            URI uri = new URI(location);

            File file = new File(uri);

            if (!file.exists()) {
                throw new IllegalArgumentException(String.format("File specified by %s was not found", location));
            }
            return fileToUrl(file).toExternalForm();
        } catch (URISyntaxException ex) {
            throw new LocationException(String.format("Unable to convert '%s' to URI", location), ex.getCause());
        }

    }

    private URL fileToUrl(File file) {
        Validate.notNull(file, "File to get URL of can not be a null object.");

        try {
            URL url = file.getAbsoluteFile().toURI().toURL();
            return url;
        } catch (MalformedURLException ex) {
            throw new IllegalArgumentException(String.format("Unable to get URL of file %s", file.getAbsolutePath()), ex.getCause());
        }

    }
}
