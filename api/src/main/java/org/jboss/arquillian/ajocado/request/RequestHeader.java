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
package org.jboss.arquillian.ajocado.request;

/**
 * Request header which can be added to Selenium requests
 * 
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 */
public class RequestHeader {
    
    private String name;
    private String value;

    /**
     * Creates a header with a given name and value
     * 
     * @param name
     *            Name of the header
     * @param value
     *            Value of the header
     */
    public RequestHeader(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Returns the name of the header
     * 
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the value of the header
     * 
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
