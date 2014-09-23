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

import org.jboss.arquillian.graphene.halter.AjaxState;

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

    private static class AjaxHalterJSInstanceHolder {

        public static AjaxHalterJSInterface instance = JSInterfaceFactory.create(context(), AjaxHalterJSInterface.class);

        private static GrapheneContext context() {
            GrapheneContext context = GrapheneContext.lastContext();
            if (context == null) {
                throw new IllegalStateException("The last used Graphene context is not available.");
            }
            return context;
        }
    }

    public static void enable() {
        AjaxHalterJSInstanceHolder.instance.setEnabled(true);
    }

    public static void disable() {
        AjaxHalterJSInstanceHolder.instance.setEnabled(false);
    }

    public static boolean isEnabled() {
        return AjaxHalterJSInstanceHolder.instance.isEnabled();
    }

    public static boolean isHandleAvailable() {
        return AjaxHalterJSInstanceHolder.instance.isHandleAvailable();
    }

    public static void waitForHandleAvailable() {
        getWaitModel().until(new Predicate<WebDriver>() {

            @Override
            public boolean apply(WebDriver t) {
                return AjaxHalterJSInstanceHolder.instance.isHandleAvailable();
            }
        });
    }

    private static WebDriverWaitImpl<Void> getWaitModel() {
        return new WebDriverWaitImpl<Void>(
                null,
                AjaxHalterJSInstanceHolder.context().getWebDriver(),
                AjaxHalterJSInstanceHolder.context().getConfiguration().getWaitModelInterval());
    }

    public static AjaxHalter getHandle() {
        int handle = AjaxHalterJSInstanceHolder.instance.getHandle();
        if (handle < 0) {
            throw new IllegalStateException("Handle is not available");
        }
        return new AjaxHalter(handle);
    }

    public static AjaxHalter getHandleBlocking() {
        waitForHandleAvailable();
        return getHandle();
    }

    public void continueBefore(AjaxState state) {
        Validate.notNull(state, "XHRState can not be null!");
        AjaxState phaseToContinueAfter = AjaxState.forId(state.getStateId() - 1);
        continueAfter(phaseToContinueAfter);
    }

    public void continueAfter(final AjaxState state) {
        Validate.notNull(state, "XHRState can not be null!");
        AjaxHalterJSInstanceHolder.instance.continueTo(handle, state);
        waitAjax().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver arg0) {
                AjaxState currentState = AjaxHalterJSInstanceHolder.instance.getCurrentState(handle);
                return currentState.ordinal() >= state.ordinal();
            }
        });
    }

    public void complete() {
        continueAfter(AjaxState.DONE);
    }

    @Override
    public String toString() {
        return this.getClass().getName() + ": handle " + handle;
    }
}
