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

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration.TimeoutType;
import org.jboss.arquillian.ajocado.framework.internal.WaitingProxy;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.locator.CssLocator;
import org.jboss.arquillian.ajocado.locator.DomLocator;
import org.jboss.arquillian.ajocado.locator.IdLocator;
import org.jboss.arquillian.ajocado.locator.JQueryLocator;
import org.jboss.arquillian.ajocado.locator.LinkLocator;
import org.jboss.arquillian.ajocado.locator.NameLocator;
import org.jboss.arquillian.ajocado.locator.XPathLocator;
import org.jboss.arquillian.ajocado.request.RequestType;
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
import org.jboss.arquillian.ajocado.guard.request.RequestTypeGuardFactory;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public final class Ajocado {
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

    private Ajocado() {
    }

    /*
     * Guards
     */
    public static AjaxSelenium guardNoRequest(AjaxSelenium selenium) {
        return RequestTypeGuardFactory.guard(selenium, RequestType.NONE);
    }

    public static AjaxSelenium guardHttp(AjaxSelenium selenium) {
        return RequestTypeGuardFactory.guard(selenium, RequestType.HTTP);
    }

    public static AjaxSelenium guardXhr(AjaxSelenium selenium) {
        return RequestTypeGuardFactory.guard(selenium, RequestType.XHR);
    }

    public static AjaxSelenium waitForHttp(AjaxSelenium selenium) {
        return RequestTypeGuardFactory.guardInterlayed(selenium, RequestType.HTTP);
    }

    public static AjaxSelenium waitForXhr(AjaxSelenium selenium) {
        return RequestTypeGuardFactory.guardInterlayed(selenium, RequestType.XHR);
    }

    /**
     * Gets element locator finding elements using CSS selectors.
     * 
     * @param cssSelector
     *            the <a href="http://www.w3.org/TR/css3-selectors/">CSS selector</a>
     * @return the locator for given CSS selector
     */
    public static CssLocator css(String cssSelector) {
        return new CssLocator(cssSelector);
    }

    /**
     * Find an element by evaluating the specified JavaScript expression.
     * 
     * @see DomLocator
     * @param javascriptExpression
     *            the JavaScript expression
     * @return the locator for given JavaScript expression
     */
    public static DomLocator dom(JavaScript javascriptExpression) {
        return new DomLocator(javascriptExpression);
    }

    /**
     * Locates the element with specified &#64;id attribute.
     * 
     * @param id
     *            the &#64;id attribute's value
     * @return the locator with specified &#64;id attribute
     */
    public static IdLocator id(String id) {
        return new IdLocator(id);
    }

    /**
     * Locates the link (anchor) element which contains text matching the specified pattern.
     * 
     * @param linkText
     *            the link (anchor) element's text
     * @return the locator for given linkText
     */
    public static LinkLocator link(String linkText) {
        return new LinkLocator(linkText);
    }

    /**
     * Locates the element using <a href="http://api.jquery.com/category/selectors/">JQuery Selector</a> syntax.
     * 
     * @param jquerySelector
     *            the jquery selector
     * @return the j query locator
     * @see JQueryLocator
     */
    public static JQueryLocator jq(String jquerySelector) {
        return new JQueryLocator(jquerySelector);
    }

    /**
     * Locates the first element with the specified &#64;name attribute.
     * 
     * @param name
     *            the &#64;name attribute's value
     * @return the locator for given &#64;name attribute
     */
    public static NameLocator name(String name) {
        return new NameLocator(name);
    }

    /**
     * Locates the element using <a href="http://www.w3.org/TR/xpath/">XPath expression</a>.
     * 
     * @param xpath
     *            the xpath expression
     * @return the xpath locator
     * @see XPathLocator
     */
    public static XPathLocator xp(String xpath) {
        return new XPathLocator(xpath);
    }
}
