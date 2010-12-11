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
package org.jboss.ajocado.encapsulated;

/**
 * The enumeration of supported network traffic types.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class NetworkTrafficType {

    /**
     * The network traffic in JSON format type
     */
    public static final NetworkTrafficType JSON = new NetworkTrafficType("json");
    /**
     * The network traffic in XML format type
     */
    public static final NetworkTrafficType XML = new NetworkTrafficType("xml");
    /**
     * The network traffic in plain-text
     */
    public static final NetworkTrafficType PLAIN = new NetworkTrafficType("plain");

    private String type;

    /**
     * Constructs the new network traffic output format type using it's string identifier
     * 
     * @param type
     *            the string identifier of output format type
     */
    public NetworkTrafficType(String type) {
        this.type = type;
    }

    /**
     * Returns the type's string identifier
     * 
     * @return the type's string identifier
     */
    public String getType() {
        return type;
    }
}
