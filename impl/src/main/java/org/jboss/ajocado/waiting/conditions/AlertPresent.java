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
package org.jboss.ajocado.waiting.conditions;

import static org.jboss.ajocado.encapsulated.JavaScript.js;

import org.jboss.ajocado.encapsulated.JavaScript;
import org.jboss.ajocado.framework.AjaxSelenium;
import org.jboss.ajocado.framework.AjaxSeleniumProxy;
import org.jboss.ajocado.waiting.ajax.JavaScriptCondition;
import org.jboss.ajocado.waiting.selenium.SeleniumCondition;

/**
 * <p>
 * Implementation of Condition for waiting, if an alert is present on the page.
 * </p>
 * 
 * <p>
 * Implements Condition and JavaScriptCondition used in SeleniumWaiting and AjaxWaiting.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AlertPresent implements SeleniumCondition, JavaScriptCondition {

    /**
     * Proxy to local selenium instance
     */
    private AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();

    /**
     * Instantiates a new element present.
     */
    protected AlertPresent() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.waiting.Condition#isTrue()
     */
    public boolean isTrue() {
        return selenium.isAlertPresent();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.ajocado.waiting.ajax.JavaScriptCondition#getJavaScriptCondition()
     */
    public JavaScript getJavaScriptCondition() {
        return js("selenium.isAlertPresent()");
    }

    /**
     * Factory method.
     * 
     * @return single instance of ElementPresent
     */
    public static AlertPresent getInstance() {
        return new AlertPresent();
    }
}
