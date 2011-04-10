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

/**
 * Options for deleting cookies.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class CookieDeleteOptions extends CookieOptions<CookieDeleteOptions> {

    private Boolean recursive = null;

    CookieDeleteOptions() {
    }

    /**
     * <p>
     * Specifies whether the domain and path should be searched recursively.
     * </p>
     * 
     * <p>
     * In big-O notation, it operates in O(n*m) time, where n is the number of dots in the domain name and m is the
     * number of slashes in the path.
     * </p>
     * 
     * @param recurse
     *            specifies whether the domain and path should be searched for recursively
     * @return the delete cookie options with recurse specified to given value
     */
    public CookieDeleteOptions recurse(boolean recurse) {
        CookieDeleteOptions copy = copy();
        copy.recursive = recurse;
        return copy;
    }

    /**
     * Returns true if the domain and path should be searched recursively; false otherwise
     * 
     * @return true if the domain and path should be searched recursively; false otherwise
     */
    public boolean isRecursive() {
        return recursive;
    }

    @Override
    public String inSeleniumRepresentation() {
        String result = super.inSeleniumRepresentation();

        if (recursive != null) {
            return result + ", recurse=" + Boolean.toString(recursive);
        } else {
            return result;
        }
    }
}
