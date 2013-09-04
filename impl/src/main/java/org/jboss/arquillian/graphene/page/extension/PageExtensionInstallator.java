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

import org.jboss.arquillian.graphene.spi.page.PageExtension;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public interface PageExtensionInstallator extends PageExtension {

    /**
     * Tries to install the given extension. If the extension isn't detected
     * as installed (via {@link PageExtensionInstallator#isInstalled() } method,
     * the installation fails. If the extension is already installed, the installation
     * is skipped.
     *
     * @throws IllegalStateException if the installation fails
     */
    void install();

    /**
     * Detects if extension is already loaded to the page
     *
     * @return the if extension is loaded in the page; false otherwise
     */
    boolean isInstalled();

}
