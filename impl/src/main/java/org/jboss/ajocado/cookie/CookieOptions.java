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
package org.jboss.ajocado.cookie;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Options for manipulation with cookies.
 * </p>
 * 
 * <p>
 * Immutable implementation.
 * </p>
 * 
 * @param <T>
 *            What final implementation can be derived by this object
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public abstract class CookieOptions<T extends CookieOptions<T>> implements Cloneable {
    private String domain = null;
    private String path = null;

    /**
     * Specifies to which domain is cookie bound.
     * 
     * @param domain
     * @return
     */
    public T domain(String domain) {
        T copy = copy();
        copy.domain = domain;
        return (T) copy;
    }

    /**
     * Specifies to which path is cookie bound.
     * 
     * @param path
     * @return
     */
    public T path(String path) {
        T copy = copy();
        copy.path = path;
        return (T) copy;
    }

    public String getDomain() {
        return domain;
    }

    public String getPath() {
        return path;
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

    public String getAsString() {
        List<String> list = new LinkedList<String>();
        if (domain != null) {
            list.add("domain=" + domain);
        }
        if (path != null) {
            list.add("path=" + path);
        }
        return StringUtils.join(list, ", ");
    }
}
