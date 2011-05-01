/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.ajocado.cookie;

import java.io.Serializable;

import org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable;

/**
 * The immutable representation of Cookie
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class Cookie implements SeleniumRepresentable, Cloneable, Serializable {

    private static final long serialVersionUID = 8126558593039344559L;

    private String name;
    private String value;

    private Cookie(String name, String value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Creates cookie with specified name and value.
     * 
     * @param name
     *            the name of cookie (non-null)
     * @param value
     *            the value for cookie (non-null)
     * @return new cookie with specified name and value
     */
    public static Cookie createCookie(String name, String value) {
        if (name == null) {
            throw new IllegalArgumentException("name parameter can't be null");
        }
        if (value == null) {
            throw new IllegalArgumentException("value parameter can't be null");
        }
        return new Cookie(name, value);
    }

    /**
     * Returns name of the cookie
     * 
     * @return name of the cookie
     */
    public String getName() {
        return name;
    }

    /**
     * Returns value of the cookie
     * 
     * @return value of the cookie
     */
    public String getValue() {
        return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable#inSeleniumRepresentation()
     */
    public String inSeleniumRepresentation() {
        return name + "=" + value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Cookie other = (Cookie) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (value == null) {
            if (other.value != null) {
                return false;
            }
        } else if (!value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
