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
package org.jboss.arquillian.graphene;

/**
 * This class provides browser-local variables.  These variables differ from
 * their normal counterparts in that each browser context that accesses one (via its
 * <tt>get</tt> or <tt>set</tt> method) has its own, independently initialized
 * copy of the variable.  <tt>BrowserLocal</tt> instances are typically private
 * static fields in classes that wish to associate state with a browser context.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @see ThreadLocal
 * @see BrowserActions
 */
public class BrowserLocal<T> {

    /**
     * Returns the value in the current browser context's copy of this
     * browser-local variable.  If the variable has no value for the
     * current browser context, it is first initialized to the value returned
     * by an invocation of the {@link #initialValue} method.
     *
     * @return the current browser context's value of this browser-local
     * @throws IllegalStateException if there is no active browser context
     */
    public T get() {
        BrowserActions browser = BrowserActions.currentBrowserActions();
        T result = (T) browser.browserLocals.get(this);
        if (result == null) {
            T init = initialValue();
            if (init != null) {
                browser.browserLocals.put(this, init);
                return init;
            }
        }
        return result;
    }

    /**
     * Returns the value in the last browser context's copy of this
     * browser-local variable.  If the variable has no value for the
     * last browser context, it is first initialized to the value returned
     * by an invocation of the {@link #initialValue} method.
     *
     * <p>If the last browser context is not available, null is returned.
     *
     * @return the last browser context's value of this browser-local
     */
    public T getLast() {
        BrowserActions browser = BrowserActions.lastBrowserActions();
        if (browser == null) {
            return null;
        }
        T result = (T) browser.browserLocals.get(this);
        if (result == null) {
            T init = initialValue();
            if (init != null) {
                browser.browserLocals.put(this, init);
                return init;
            }
        }
        return result;
    }

    /**
     * Returns the current browser context's "initial value" for this
     * browser-local variable.  This method will be invoked the first
     * time a browser context accesses the variable with the {@link #get}
     * method, unless the browser context previously invoked the {@link #set}
     * method, in which case the <tt>initialValue</tt> method will not
     * be invoked for the browser context.  Normally, this method is invoked at
     * most once per browser context, but it may be invoked again in case of
     * subsequent invocations of {@link #remove} followed by {@link #get}.
     *
     * <p>This implementation simply returns <tt>null</tt>; if the
     * programmer desires browser-local variables to have an initial
     * value other than <tt>null</tt>, <tt>BrowserLocal</tt> must be
     * subclassed, and this method overridden.  Typically, an
     * anonymous inner class will be used.
     *
     * @return the initial value for this browser-local
     */
    protected T initialValue() {
        return null;
    }

    /**
     * Removes the current browser context's value for this browser-local
     * variable.  If this browser-local variable is subsequently
     * {@linkplain #get read} by the current browser context, its value will be
     * reinitialized by invoking its {@link #initialValue} method,
     * unless its value is {@linkplain #set set} by the current browser context
     * in the interim.  This may result in multiple invocations of the
     * <tt>initialValue</tt> method in the current browser context.
     *
     * @throws IllegalStateException if there is no active browser context
     */
    public void remove() {
        BrowserActions.currentBrowserActions().browserLocals.remove(this);
    }

    /**
     * Sets the current browser context's copy of this browser-local variable
     * to the specified value.  Most subclasses will have no need to
     * override this method, relying solely on the {@link #initialValue}
     * method to set the values of browser-locals.
     *
     * @param value the value to be stored in the current browser context's copy of
     *        this browser-local.
     * @throws IllegalStateException if there is no active browser context
     */
    public void set(T value) {
        BrowserActions.currentBrowserActions().browserLocals.put(this, value);
    }

}
