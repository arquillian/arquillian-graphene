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
package org.jboss.arquillian.ajocado.framework;

import org.jboss.arquillian.ajocado.command.CommandInterceptorProxy;
import org.jboss.arquillian.ajocado.guard.RequestGuard;

/**
 * <p>
 * Extension for {@link TypedSelenium} extended by methods in {@link ExtendedTypedSelenium}.
 * </p>
 *
 * <p>
 * Internally using {@link CommandInterceptorProxy} to add aspects to commands.
 * </p>
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface AjaxSelenium extends ExtendedTypedSelenium, Cloneable {

    /**
     * <p>
     * Gets a PageExtensions object.
     * </p>
     *
     * <p>
     * PageExtensions represents the JavaScript extensions on the tested page.
     * </p>
     *
     * @return the PageExtensions object
     */
    PageExtensions getPageExtensions();

    /**
     * <p>
     * Returns a SeleniumExtensions object.
     * </p>
     *
     * <p>
     * SeleniumExtensions can be used in Selenium Test Runner to extend Selenium functionality.
     * </p>
     *
     * @return the SeleniumExtensions object
     */
    SeleniumExtensions getSeleniumExtensions();

    /**
     * <p>
     * Returns the RequestInterceptor object
     * </p>
     *
     * <p>
     * RequestInterceptor
     *
     * @return the RequestInterceptor object
     */
    RequestGuard getRequestGuard();

    /**
     * Returns associated command interception proxy
     *
     * @return associated command interception proxy
     */
    CommandInterceptorProxy getCommandInterceptionProxy();

    /**
     * <p>
     * Simulates restart of browser in order to be able get unpolluted browser session.
     * </p>
     *
     * <p>
     * When running in mode that reuses browser session, it will only delete all cookies.
     * </p>
     */
    void restartBrowser();

    /**
     * Returns the clone of AjaxSelenium pointing to the same Selenium session but with cloned configuration objects
     * (PageExtensions, SeleniumExtensions, RequestGuard and CommandInterceptionProxy).
     *
     * @return the clone of this AjaxSelenium instance
     */
    AjaxSelenium clone();
}
