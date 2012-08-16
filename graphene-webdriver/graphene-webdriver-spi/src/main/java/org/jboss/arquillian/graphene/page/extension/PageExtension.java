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
 * Denotes extension to be injected to the page
 *
 * @author Lukas Fryc
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public interface PageExtension {

    /**
     * Returns the name of the script that can uniquely identify this script
     */
    String getName();

    /**
     * Returns the source code of the script to be injected to the page
     */
    JavaScript getExtensionScript();

    /**
     * Returns the source code of the script which will be executed to check
     * whether the extension is already installed. The script has to return
     * boolean value.
     */
    JavaScript getInstallationDetectionScript();

    /**
     * Returns a collection of extension names required by this extension
     */
    Collection<String> getRequired();

}
