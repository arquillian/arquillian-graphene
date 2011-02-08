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
package org.jboss.arquillian.ajocado.request;

import static org.jboss.arquillian.ajocado.encapsulated.JavaScript.js;
import static org.jboss.arquillian.ajocado.utils.SimplifiedFormat.format;

import org.jboss.arquillian.ajocado.SystemProperties;
import org.jboss.arquillian.ajocado.SystemProperties.SeleniumTimeoutType;
import org.jboss.arquillian.ajocado.encapsulated.JavaScript;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumProxy;

/**
 * <p>
 * Java API for interception of HTTP/XMLHttpRequest requests.
 * </p>
 * 
 * <p>
 * Needs to have PageExtensions installed in the current page using {@link PageExtensionsImpl#install()}.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class RequestInterceptor {
    private final JavaScript clearRequestDone = js("cheiron.requestInterceptor.clearRequestDone()");
    private final JavaScript getRequestDone = js("cheiron.requestInterceptor.getRequestDone()");
    private final JavaScript waitRequestChange = js("cheiron.requestInterceptor.waitRequestChange()");

    /**
     * Proxy for thread local context of AjaxSelenium
     */
    private AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();

    /**
     * Obtains the done requestType from page.
     * 
     * @return the RequestType what was done
     * @throws IllegalStateException
     *             when the unknown type of request was obtained
     */
    public RequestType getRequestTypeDone() {
        String requestDone = selenium.getEval(getRequestDone);
        return parseRequest(requestDone);
    }

    /**
     * <p>
     * Clears the request type indicated on the page, gives possibility to intercept subsequent requests.
     * </p>
     * 
     * <p>
     * Returns the last RequestType set.
     * </p>
     * 
     * @return the last RequestType done
     * @throws IllegalStateException
     *             when the unknown type of request was obtained
     */
    public RequestType clearRequestTypeDone() {
        String lastRequest = selenium.getEval(clearRequestDone);
        return parseRequest(lastRequest);
    }

    /**
     * Waits for change of RequestType indicated on the page from NONE to other value.
     */
    public void waitForRequestTypeChange() {
        selenium.doCommand("waitForRequestChange", Long.toString(SystemProperties.getSeleniumTimeout(SeleniumTimeoutType.AJAX)), null);
    }

    private RequestType parseRequest(String request) {
        try {
            return RequestType.valueOf(request);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(format("Request was evaluated to unknown type '{0}'", request));
        }
    }
}
