/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.guard;

import org.jboss.arquillian.graphene.page.RequestType;

/**
 * This exception determines that when interacting with browser using Selenium, the wrong request type was executed.
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class RequestGuardException extends RuntimeException {

    private static final long serialVersionUID = -1945770825192733128L;

    /**
     * The RequestType which was expected to be done
     */
    private RequestType requestExpected;
    /**
     * The RequestType which was actually done
     */
    private RequestType requestDone;

    public RequestGuardException(RequestType requestExpected, RequestType requestDone) {
        this.requestExpected = requestExpected;
        this.requestDone = requestDone;
    }

    /**
     * Returns the RequestType which was expected to be done
     *
     * @return the requestExpected RequestType which was expected to be done
     */
    public RequestType getRequestExpected() {
        return requestExpected;
    }

    /**
     * Returns the RequestType which was actually done
     *
     * @return the requestDone RequestType which was actually done
     */
    public RequestType getRequestDone() {
        return requestDone;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Throwable#getMessage()
     */
    @Override
    public String getMessage() {
        return "Request type '" + requestExpected + "' was expected, but type '" + requestDone + "' was done instead";
    }
}
