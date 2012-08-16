/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.page.extension;

import java.util.Collection;


/**
 * Registry of {@link PageExtension}s required by the current test.
 *
 * @author Lukas Fryc
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public interface PageExtensionRegistry {

    /**
     * Registers given extensions to be injected to the page
     *
     * @param extensions
     */
    void register(PageExtension... extensions);

    /**
     * Registers given extensions to be injected to the page
     *
     * @param extensions
     */
    void register(Collection<PageExtension> extensions);

    /**
     * Returns an extension specified by the given name.
     * @param name
     * @return requested extension or null if there is no extension with the given name
     */
    PageExtension getExtension(String name);

    /**
     * Returns all registered extensions as a collection.
     */
    Collection<PageExtension> getExtensions();

    /**
     * Flushes the registered {@link PageExtension}s
     */
    void flush();
}
