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
package org.jboss.arquillian.ajocado.guard;

import static org.jboss.arquillian.ajocado.javascript.JavaScript.js;

import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration;
import org.jboss.arquillian.ajocado.framework.AjocadoConfiguration.TimeoutType;
import org.jboss.arquillian.ajocado.framework.AjocadoConfigurationContext;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.request.RequestType;

/**
 * <p>
 * Java API for interception of HTTP/XMLHttpRequest requests.
 * </p>
 * 
 * <p>
 * Needs to have PageExtensions installed in the current page using
 * {@link org.jboss.arquillian.ajocado.framework.internal.PageExtensionsImpl#install()}.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class RequestGuardImpl implements RequestGuard {

    private static final String WAIT_FOR_REQUEST = "waitForRequest";
    private static final JavaScript CLEAR_REQUEST_DONE = js("Ajocado.RequestInterceptor.clearRequestDone()");
    private static final JavaScript GET_REQUEST_DONE = js("Ajocado.RequestInterceptor.getRequestDone()");

    /**
     * Proxy for thread local context of AjaxSelenium
     */
    private AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    private AjocadoConfiguration configuration = AjocadoConfigurationContext.getProxy();

    /**
     * Obtains the done requestType from page.
     * 
     * @return the RequestType what was done
     * @throws IllegalStateException
     *             when the unknown type of request was obtained
     */
    public RequestType getRequestDone() {
        String requestDone = selenium.getEval(GET_REQUEST_DONE);
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
    public RequestType clearRequestDone() {
        String lastRequest = selenium.getEval(CLEAR_REQUEST_DONE);
        return parseRequest(lastRequest);
    }

    /**
     * Waits for change of RequestType indicated on the page from NONE to other value.
     */
    public void waitForRequest() {
        selenium.doCommand(WAIT_FOR_REQUEST, Long.toString(configuration.getTimeout(TimeoutType.AJAX)), null);
    }

    private RequestType parseRequest(String request) {
        try {
            return RequestType.valueOf(request);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Request was evaluated to unknown type '" + request + "'");
        }
    }
}
