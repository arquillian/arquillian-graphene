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

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.graphene.page.UriScheme;
import org.jboss.arquillian.graphene.page.location.LocationDecider;

/**
 * Holds all {@link LocationDecider}s.
 *
 * @see LocationDeciderRegistryInitializer
 *
 * @author <a href="smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
public class LocationDeciderRegistry {

    private List<LocationDecider> deciders;

    public LocationDeciderRegistry() {
        deciders = new ArrayList<LocationDecider>();
    }

    public void set(List<LocationDecider> deciders) {
        this.deciders = deciders;
    }

    /**
     * Gets {@link LocationDecider} which can decides given {@code scheme}.
     *
     * @param scheme scheme which returned decider will decide
     * @return decider which decides {@code scheme} or null if no such exists
     */
    LocationDecider get(UriScheme scheme) {
        for (LocationDecider decider : deciders) {
            if (decider.canDecide().equals(scheme)) {
                return decider;
            }
        }

        return null;
    }

    /**
     * Says if some scheme is supported.
     *
     * @param scheme scheme to query the availability of decider of
     * @return true if this register holds some decider which can decide given scheme
     */
    public boolean canDecide(UriScheme scheme) {
        for (LocationDecider decider : deciders) {
            if (decider.canDecide().equals(scheme)) {
                return true;
            }
        }

        return false;
    }

}
