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

import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

import org.jboss.test.selenium.encapsulated.Browser;
import org.jboss.test.selenium.framework.internal.Contextual;
import org.jboss.test.selenium.guard.Guard;
import org.jboss.test.selenium.guard.Guarded;

public class AjaxSelenium extends ExtendedTypedSelenium implements Guarded {

    private static AtomicReference<AjaxSelenium> reference = new AtomicReference<AjaxSelenium>(null);

    public AjaxSelenium(String serverHost, int serverPort, Browser browser, URL contextPathURL) {
        selenium = new ExtendedAjaxAwareSelenium(serverHost, serverPort, browser, contextPathURL);
        setCurrentContext(this);
    }
    
    private AjaxSelenium() {
    }
    
    private ExtendedAjaxAwareSelenium getExtendedAjaxAwareSelenium() {
        return (ExtendedAjaxAwareSelenium) selenium;
    }

    private class ExtendedAjaxAwareSelenium extends ExtendedSelenium {

        public AjaxAwareCommandProcessor ajaxAwareCommandProcessor;
        public GuardedCommandProcessor guardedCommandProcessor;

        public ExtendedAjaxAwareSelenium(String serverHost, int serverPort, Browser browser, URL contextPathURL) {
            super(null);
            ajaxAwareCommandProcessor = new AjaxAwareCommandProcessor(serverHost, serverPort, browser.toString(),
                contextPathURL.toString());
            guardedCommandProcessor = new GuardedCommandProcessor(ajaxAwareCommandProcessor);
            this.commandProcessor = guardedCommandProcessor;
        }
    }
    
    

    // TODO not safe for multi-instance environment
    private static void setCurrentContext(AjaxSelenium selenium) {
        reference.set(selenium);
    }

    public static AjaxSelenium getCurrentContext(Contextual... inContext) {
        return reference.get();
    }

    public <T extends Guard> void guardOnce(Guard guard) {
        GuardedCommandProcessor copy = ((ExtendedAjaxAwareSelenium) selenium).guardedCommandProcessor.immutableCopy();
    }

    public void registerGuard(Guard guard) {
        ((ExtendedAjaxAwareSelenium) selenium).guardedCommandProcessor.registerGuard(guard);
    }

    public void unregisterGuard(Guard guard) {
        ((ExtendedAjaxAwareSelenium) selenium).guardedCommandProcessor.unregisterGuard(guard);
    }
    
    public AjaxSelenium immutableCopy() {
        AjaxSelenium copy = new AjaxSelenium();
        copy.selenium = this.selenium;
        ((ExtendedAjaxAwareSelenium) copy.selenium).guardedCommandProcessor = ((ExtendedAjaxAwareSelenium) selenium).guardedCommandProcessor.immutableCopy();
        return copy;
    }
}
