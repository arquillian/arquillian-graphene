/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public interface PageExtensionInstallatorProvider {

    /**
     * Provides a {@link PageExtensionInstallator} instance for the extension
     * specified by the given name. Also cycles in page extension requirements
     * are checked.
     *
     * @param extensionName
     * @return installator or null, if the extension with the given name isn't available
     * @throws IllegalStateException if there is a cycle in page extension requirements
     */
    PageExtensionInstallator installator(String extensionName);

    /**
     * Provides a {@link PageExtensionInstallator} instance for the extension
     * specified by the given name.
     *
     * @param extensionName
     * @param checkRequirements if true, cycles in page extension requirements are checked.
     * @throws IllegalStateException if there is a cycle in page extension requirements
     */
    PageExtensionInstallator installator(String extensionName, boolean checkRequirements);

}
