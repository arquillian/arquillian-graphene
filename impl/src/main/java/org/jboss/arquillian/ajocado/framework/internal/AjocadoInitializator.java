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
package org.jboss.arquillian.ajocado.framework.internal;

/**
 * <p>
 * Utilities for control of Selenium session and extensions.
 * </p>
 *
 * <p>
 * For proper initialization, methods should be called in following order:
 * </p>
 *
 * <ol>
 * <li>{@link #initializeBrowser()}</li>
 * <li>{@link #initializeSeleniumExtensions()}</li>
 * <li>{@link #initializePageExtensions()}</li>
 * <li>{@link #configureBrowser()}</li>
 * </ol>
 *
 *
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface AjocadoInitializator {

    /**
     * Initializes browser session and prepares it for further configuration
     */
    void initializeBrowser();

    /**
     * Initializes Ajocado extentensions for Selenium
     */
    void initializeSeleniumExtensions();

    /**
     * Initializes Ajocado extentensions for application page
     */
    void initializePageExtensions();

    /**
     * Configures browser from users configuration
     */
    void configureBrowser();

    /**
     * Cleans browser window (by closing window or deleting all cookies)
     */
    void finalizeBrowser();
}
