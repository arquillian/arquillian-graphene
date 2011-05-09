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
package org.jboss.arquillian.ajocado.css;

/**
 * <p>
 * Resolves the CSS property values from their string representation to their object representation.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 * @param <T>
 *            the type which can be resolved by given implementation of {@link CssResolver}.
 */
public interface CssResolver<T> {

    /**
     * Returns the name of property which can resolve
     * 
     * @return the name of property which can resolve
     */
    String getPropertyName();

    /**
     * Resolves the property - converts the string representation to object representation
     * 
     * @param propertyValue
     *            the value of property
     * @return converted property value string representation to object representation
     */
    T resolveProperty(String propertyValue);

}
