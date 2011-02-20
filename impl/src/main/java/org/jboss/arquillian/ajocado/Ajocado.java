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
package org.jboss.arquillian.ajocado;

import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration.TimeoutType;
import org.jboss.arquillian.ajocado.framework.internal.WaitingProxy;
import org.jboss.arquillian.ajocado.waiting.Wait;
import org.jboss.arquillian.ajocado.waiting.ajax.AjaxWaiting;
import org.jboss.arquillian.ajocado.waiting.conditions.AlertEquals;
import org.jboss.arquillian.ajocado.waiting.conditions.AlertPresent;
import org.jboss.arquillian.ajocado.waiting.conditions.AttributeEquals;
import org.jboss.arquillian.ajocado.waiting.conditions.AttributePresent;
import org.jboss.arquillian.ajocado.waiting.conditions.CountEquals;
import org.jboss.arquillian.ajocado.waiting.conditions.ElementPresent;
import org.jboss.arquillian.ajocado.waiting.conditions.IsDisplayed;
import org.jboss.arquillian.ajocado.waiting.conditions.IsNotDisplayed;
import org.jboss.arquillian.ajocado.waiting.conditions.StyleEquals;
import org.jboss.arquillian.ajocado.waiting.conditions.TextEquals;
import org.jboss.arquillian.ajocado.waiting.retrievers.AttributeRetriever;
import org.jboss.arquillian.ajocado.waiting.retrievers.TextRetriever;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumWaiting;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class Ajocado {
    public static final int WAIT_GUI_INTERVAL = 100;
    public static final int WAIT_AJAX_INTERVAL = 500;
    public static final int WAIT_MODEL_INTERVAL = 1500;

    /*
     * Waitings
     */
    public static final AjaxWaiting waitGui = WaitingProxy.create(Wait.waitAjax.interval(WAIT_GUI_INTERVAL),
        TimeoutType.GUI);
    
    public static final AjaxWaiting waitAjax = WaitingProxy.create(Wait.waitAjax.interval(WAIT_AJAX_INTERVAL),
        TimeoutType.AJAX);
    
    public static final SeleniumWaiting waitModel = WaitingProxy.create(
        Wait.waitSelenium.interval(WAIT_MODEL_INTERVAL), TimeoutType.MODEL);

    /*
     * Wait Conditions
     */
    public static final ElementPresent elementPresent = ElementPresent.getInstance();
    public static final TextEquals textEquals = TextEquals.getInstance();
    public static final StyleEquals styleEquals = StyleEquals.getInstance();
    public static final AttributePresent attributePresent = AttributePresent.getInstance();
    public static final AttributeEquals attributeEquals = AttributeEquals.getInstance();
    public static final AlertPresent alertPresent = AlertPresent.getInstance();
    public static final AlertEquals alertEquals = AlertEquals.getInstance();
    public static final CountEquals countEquals = CountEquals.getInstance();
    public static final IsDisplayed isDisplayed = IsDisplayed.getInstance();
    public static final IsNotDisplayed isNotDisplayed = IsNotDisplayed.getInstance();

    /*
     * Retrievers
     */
    public static final TextRetriever retrieveText = TextRetriever.getInstance();
    public static final AttributeRetriever retrieveAttribute = AttributeRetriever.getInstance();

}
