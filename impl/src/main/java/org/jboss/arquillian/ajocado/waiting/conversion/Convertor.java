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
package org.jboss.arquillian.ajocado.waiting.conversion;

/**
 * Convertor for converting the values from one type to another and vice versa.
 *
 * @param <F>
 *            the from type (type which we want to convert from)
 * @param <T>
 *            the to type (type which we want to convert to)
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface Convertor<F, T> {

    /**
     * Convert from object of F type to T type.
     *
     * @param object
     *            of F type to convert to T type
     * @return the T type converted from F type
     */
    T forwardConversion(F object);

    /**
     * Convert from object of T type to F type.
     *
     * @param object
     *            of T type to convert to F type
     * @return the F type converted from T type
     */
    F backwardConversion(T object);
}
