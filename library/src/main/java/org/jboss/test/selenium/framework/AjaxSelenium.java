/*
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
 */
package org.jboss.test.selenium.framework;

import org.jboss.test.selenium.framework.internal.PageExtensions;
import org.jboss.test.selenium.framework.internal.SeleniumExtensions;
import org.jboss.test.selenium.interception.InterceptionProxy;

/**
 * <p>
 * Implementation of {@link TypedSelenium} extended by methods in {@link ExtendedTypedSeleniumImpl}.
 * </p>
 * 
 * <p>
 * Internally using {@link AjaxAwareInterceptor} and {@link InterceptionProxy}.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public interface AjaxSelenium extends ExtendedTypedSelenium, Cloneable {

    /**
     * <p>
     * Gets a PageExtensions associated with this AjaxSelenium object.
     * </p>
     * 
     * <p>
     * PageExtensions represents the JavaScript extensions on the testes page.
     * </p>
     * 
     * @return the PageExtensions associated with this AjaxSelenium object.
     */
    PageExtensions getPageExtensions();

    /**
     * <p>
     * Gets a SeleniumExtensions associated with this AjaxSelenium object.
     * </p>
     * 
     * <p>
     * Selenium extensions can be used in Selenium Test Runner to extend functionality.
     * </p>
     * 
     * @return the SeleniumExtensions associated with this AjaxSelenium object.
     */
    SeleniumExtensions getSeleniumExtensions();

    /**
     * Returns associated command interception proxy
     * 
     * @return associated command interception proxy
     */
    InterceptionProxy getInterceptionProxy();

    /**
     * Immutable clone of this object.
     * 
     * @return immutable clone of this object
     */
    AjaxSelenium clone() throws CloneNotSupportedException;
}
