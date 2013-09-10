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
package org.jboss.arquillian.graphene.request;

import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.javascript.JavaScript;

/**
 * <p>Allows to manually retrieve information from Request Guard on a page.</p>
 *
 * <p>This method is used internally by:
 *
 * <ul>
 * <li>{@link Graphene#guardHttp(Object)}</li>
 * <li>{@link Graphene#guardAjax(Object)}</li>
 * <li>{@link Graphene#guardNoRequest(Object)}</li>
 * <li>{@link Graphene#waitForHttp(Object)}</li>
 * </ul>
 *
 * @author Lukas Fryc
 */
@JavaScript(implementation = "org.jboss.arquillian.graphene.guard.RequestGuardImpl")
public interface RequestGuard {

    /**
     * @return the last request type
     */
    RequestType getRequestType();

    /**
     * @return the state of last request
     */
    RequestState getRequestState();

    /**
     * Clears the request type cache and returns the last request type
     *
     * @return the last request type
     */
    RequestType clearRequestDone();

    /**
     * <p>
     * Specifies JavaScript implementation of filter, which declares what requests will be guarded.
     * </p>
     *
     * <p>
     * If the provided expression evaluates to false, a request won't be guarded.
     * </p>
     *
     * <p>
     * In a 'eval' expression, you can use following contextual information:
     * </p>
     *
     * <ul>
     * <li><tt>this.url</tt> - an URL</li>
     * <li><tt>this.method</tt> - GET/POST</li>
     * <li><tt>this.async</tt> - boolean expression determines whether a request is async</li>
     * <li><tt>this.body</tt> - a content of a request</li>
     * </ul>
     *
     * <p>
     * Note that some values might need processing, for example body might be escaped:
     * </p>
     *
     * <p>
     * E.g.: <tt>unescape(this.body).indexOf('javax.faces.source=f:poll') &gt; 0</tt>
     * </p>
     *
     * <p>
     * The expression above will detect, whether a request body contains some string.
     * </p>
     */
    void filter(String eval);

    /**
     * Clear all filters defined by {@link #filter(String)}.
     */
    void clearFilters();

    /**
     * <p>
     * Specifies maximum timeout (in miliseconds) for asynchronous scheduled callbacks which will be guarded (default: 50 ms).
     * </p>
     *
     * <p>
     * <i>Asynchronous scheduled callbacks</i> are callbacks which are scheduled in XMLHttpRequests's
     * <tt>onreadystatechange</tt> callback.
     * </p>
     *
     * <p>
     * Usually are those callbacks scheduled for timeouts &lt;= 50 just to postpone updates of user interface out of XHR
     * processing.
     * </p>
     * </p>
     *
     * <p>
     * Be aware that sometimes it is inappropriate to guard asynchronous scheduled callbacks - in this case you can set this
     * value to -1 in order to guarding of scheduled callbacks at all.
     * </p>
     */
    void setMaximumCallbackTimeout(int miliseconds);
}