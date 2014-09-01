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
package org.jboss.arquillian.graphene.page.interception;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.wait.WebDriverWaitImpl;
import org.openqa.selenium.WebDriver;

import com.google.common.base.Predicate;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class AjaxHalter {

    private int handle;

    public AjaxHalter(int handle) {
        this.handle = handle;
    }

    private static class XHRHalterHolder {

        public static AjaxHalterInterface instance = JSInterfaceFactory.create(context(), AjaxHalterInterface.class);

        private static GrapheneContext context() {
            GrapheneContext context = GrapheneContext.lastContext();
            if (context == null) {
                throw new IllegalStateException("The last used Graphene context is not available.");
            }
            return context;
        }
    }

    public static void enable() {
        XHRHalterHolder.instance.setEnabled(true);
    }

    public static void disable() {
        XHRHalterHolder.instance.setEnabled(false);
    }

    public static boolean isEnabled() {
        return XHRHalterHolder.instance.isEnabled();
    }

    public static boolean isHandleAvailable() {
        return XHRHalterHolder.instance.isHandleAvailable();
    }

    public static void waitForHandleAvailable() {
        getWaitModel().until(new Predicate<WebDriver>() {

            @Override
            public boolean apply(WebDriver t) {
                return XHRHalterHolder.instance.isHandleAvailable();
            }
        });
    }

    private static WebDriverWaitImpl<Void> getWaitModel() {
        return new WebDriverWaitImpl<Void>(
                null,
                XHRHalterHolder.context().getWebDriver(),
                XHRHalterHolder.context().getConfiguration().getWaitModelInterval());
    }

    public static AjaxHalter getHandle() {
        int handle = XHRHalterHolder.instance.getHandle();
        if (handle < 0) {
            throw new IllegalStateException("Handle is not available");
        }
        return new AjaxHalter(handle);
    }

    public static AjaxHalter getHandleBlocking() {
        waitForHandleAvailable();
        return getHandle();
    }

    public void continueBefore(XHRState state) {
        Validate.notNull(state, "XHRState can not be null!");
        XHRState phaseToContinueAfter = XHRState.forId(state.getStateId() - 1);
        continueAfter(phaseToContinueAfter);
    }

    public void continueAfter(final XHRState state) {
        Validate.notNull(state, "XHRState can not be null!");
        XHRHalterHolder.instance.continueTo(handle, state);
        waitAjax().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver arg0) {
                XHRState currentState = XHRHalterHolder.instance.getCurrentState(handle);
                return currentState.ordinal() >= state.ordinal();
            }
        });
    }

    public void send() {
        continueAfter(XHRState.SEND);
    }

    public void initialize() {
        continueAfter(XHRState.UNINITIALIZED);
    }

    public void loading() {
        continueAfter(XHRState.LOADING);
    }

    public void loaded() {
        continueAfter(XHRState.LOADED);
    }

    public void interactive() {
        continueAfter(XHRState.INTERACTIVE);
    }

    public void complete() {
        continueAfter(XHRState.COMPLETE);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ": handle " + handle;
    }
}
