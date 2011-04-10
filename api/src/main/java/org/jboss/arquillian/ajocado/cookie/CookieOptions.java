/*******************************************************************************
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
 *******************************************************************************/
package org.jboss.arquillian.ajocado.cookie;

import org.jboss.arquillian.ajocado.selenium.SeleniumRepresentable;

/**
 * <p>
 * Abstract immutable representation of options for manipulation with cookies.
 * </p>
 * 
 * @param <T>
 *            What type of cookie options are represented by this object
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class CookieOptions<T extends CookieOptions<T>> implements Cloneable, SeleniumRepresentable {

    private String domain = null;
    private String path = null;

    /**
     * Creates options for creating cookies
     * 
     * @return options for creating cookies
     */
    public static CookieCreateOptions forCreation() {
        return new CookieCreateOptions();
    }

    /**
     * Creates options for deleting cookies
     * 
     * @return options for deleting cookies
     */
    public static CookieDeleteOptions forDeletion() {
        return new CookieDeleteOptions();
    }

    /**
     * Specifies to which domain is cookie bound.
     * 
     * @param domain
     * @return domain to which is cookie bound
     */
    public T domain(String domain) {
        T copy = (T) copy();
        copy.domain = domain;
        return (T) copy;
    }

    /**
     * Specifies to which path is cookie bound.
     * 
     * @param path
     * @return path to which is cookie bound
     */
    public T path(String path) {
        T copy = (T) copy();
        copy.path = path;
        return (T) copy;
    }

    /**
     * Returns the domain for which is cookie registered
     * 
     * @return the domain for which is cookie registered
     */
    public String getDomain() {
        return domain;
    }

    /**
     * Returns the path for which is cookie registered
     * 
     * @return the path for which is cookie registered
     */
    public String getPath() {
        return path;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public String inSeleniumRepresentation() {
        StringBuffer result = new StringBuffer();
        if (domain != null) {
            append(result, "domain=" + domain);
        }
        if (path != null) {
            append(result, "path=" + path);
        }
        return result.toString();
    }

    protected void append(StringBuffer stringBuffer, String appendix) {
        if (stringBuffer.length() > 0) {
            stringBuffer.append(", ");
        }
        stringBuffer.append(appendix);
    }

    @SuppressWarnings("unchecked")
    protected T copy() {
        T clone;
        try {
            clone = (T) this.clone();
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
        return (T) clone;
    }
}
