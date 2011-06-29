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
package org.jboss.arquillian.ajocado.waiting;

import org.jboss.arquillian.ajocado.waiting.ajax.AjaxWaiting;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumWaiting;

/**
 * <p>
 * Factory class with static methods to create new instances of {@link AjaxWaiting} and {@link SeleniumWaiting}.
 * </p>
 *
 * <p>
 * Keeps the default timeouts and intervals used by implementations of {@link DefaultWaiting}.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class Wait {

    /**
     * Default waiting interval
     */
    public static final long DEFAULT_INTERVAL = com.thoughtworks.selenium.Wait.DEFAULT_INTERVAL;
    /**
     * Default waiting timeout
     */
    public static final long DEFAULT_TIMEOUT = com.thoughtworks.selenium.Wait.DEFAULT_TIMEOUT;

    /**
     * Provides instance of {@link AjaxWaiting}
     */
    public static final AjaxWaiting waitAjax = new AjaxWaiting();

    /**
     * Provides instance of {@link SeleniumWaiting}
     */
    public static final SeleniumWaiting waitSelenium = new SeleniumWaiting();

    private Wait() {
    }
}
