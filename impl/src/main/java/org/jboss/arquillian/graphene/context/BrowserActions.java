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
package org.jboss.arquillian.graphene.context;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * This class provides a way to perform actions dependent on browser context.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @see BrowserLocal
 */
public class BrowserActions {

    Map<BrowserLocal, Object> browserLocals = new HashMap<BrowserLocal, Object>();

    private final String name;

    private static ThreadLocal<BrowserActions> currentBrowserActions = new ThreadLocal<BrowserActions>();
    private static ThreadLocal<BrowserActions> lastBrowserActions = new ThreadLocal<BrowserActions>();

    public BrowserActions(String name) {
        this.name = name;
    }

    /**
     * Performs the given action in a context associated to this browser actions.
     *
     * @param <T> return type of the given action
     * @param action action to be invoked in the context
     * @return value returned by the given action
     * @throws Exception
     */
    public <T> T performAction(Callable<T> action) throws Exception {
        if (currentBrowserActions.get() != null && currentBrowserActions.get() != this) {
            throw new IllegalStateException("There is an browser interleaving of " + currentBrowserActions().getName() + " and " + this.getName() + ".");
        }
        currentBrowserActions.set(this);
        lastBrowserActions.set(this);
        try {
            return action.call();
        } finally {
            currentBrowserActions.set(null);
        }
    }

    /**
     * @return browser actions associated to the current browser context.
     * @throws IllegalStateException if there is no active browser context available
     */
    public static BrowserActions currentBrowserActions() {
        BrowserActions browserActions = currentBrowserActions.get();
        if (browserActions == null) {
            throw new IllegalStateException("There are no active browser actions available.");
        }
        return browserActions;
    }

    /**
     * Get the browser actions associated to the last active browser context.
     * @return the last active browser context of null, if it is not available
     */
    public static BrowserActions lastBrowserActions() {
        return lastBrowserActions.get();
    }

    /**
     * @return name of the browser context
     */
    public String getName() {
        return name;
    }

}
