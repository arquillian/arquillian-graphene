/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
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
package org.jboss.test.selenium.encapsulated;

/**
 * The encapsulation of network traffic proceeded in tested scenario.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class NetworkTraffic {
    /**
     * The type of traffic
     */
    private NetworkTrafficType type;
    private String traffic;

    /**
     * Constructs the representation of network traffic with associated format type.
     * 
     * @param type
     *            the output format type
     * @param traffic
     *            the string representation of network traffic data
     */
    public NetworkTraffic(NetworkTrafficType type, String traffic) {
        this.type = type;
        this.traffic = traffic;
    }

    /**
     * Returns the associated output format type
     * 
     * @return the associated output format type
     */
    public NetworkTrafficType getType() {
        return type;
    }

    /**
     * Returns string representation of network traffic data
     * 
     * @return string representation of network traffic data
     */
    public String getTraffic() {
        return traffic;
    }
}
