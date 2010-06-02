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
package org.jboss.test.selenium.guard.request;

import java.util.Set;

import org.jboss.test.selenium.encapsulated.JavaScript;
import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.guard.AbstractGuard;
import org.jboss.test.selenium.waiting.Wait;

import com.thoughtworks.selenium.SeleniumException;

/**
 * The Guard which guards that request what was expected to be done will be actually done.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class RequestTypeGuard extends AbstractGuard {

    private final JavaScript clearRequestDone = new JavaScript("getRFS().clearRequestDone()");
    private final JavaScript getRequestDone = new JavaScript(
        "(getRFS() === undefined) ? 'HTTP' : getRFS().getRequestDone()");
    private final JavaScript waitRequestChange = new JavaScript(
        "((getRFS() === undefined) ? 'HTTP' : getRFS().getRequestDone()) != 'NONE'");

    /**
     * The request what is expected to be done
     */
    private RequestType requestExpected;

    /**
     * Constructs the guard with predefined expected RequestType
     * 
     * @param requestExpected
     *            the RequestType which is expected to be done
     */
    public RequestTypeGuard(RequestType requestExpected) {
        super();
        this.requestExpected = requestExpected;
    }

    /**
     * Install the PageExtensions (which is used to figure out, what requestType was actually done) and clear the
     * request type to NONE state.
     */
    public void doBeforeCommand() {
        getSelenium().getPageExtensions().install();
        getSelenium().getEval(clearRequestDone);
    }

    /**
     * <p>
     * Waits for changing the requestDone flag (or for timeout, when flag stay to be NONE).
     * </p>
     * 
     * <p>
     * Then figure out what requestType was actually done and compare to expected one.
     * </p>
     * 
     * @throws RequestGuardException
     *             when done requestType doesn't equal to expected one
     */
    public void doAfterCommand() {
        try {
            // FIXME replace with Wait implementation
            getSelenium().waitForCondition(waitRequestChange, Wait.DEFAULT_TIMEOUT);
        } catch (SeleniumException e) {
            // ignore the timeout exception
        }
        RequestType requestDone = getRequestDone();

        if (requestDone != requestExpected) {
            throw new RequestGuardException(requestExpected, requestDone);
        }
    }

    /**
     * Obtains the done requestType from page.
     * 
     * @return the RequestType what was done
     * @throws IllegalStateException
     *             when the unknown type was obtained
     */
    private RequestType getRequestDone() {
        String requestDone = getSelenium().getEval(getRequestDone);
        try {
            return RequestType.valueOf(requestDone);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(format("Request was evaluated to unknown type", requestDone));
        }
    }

    @Override
    protected Set<String> getGuardedCommands() {
        return INTERACTIVE_COMMANDS;
    }

    private AjaxSelenium getSelenium() {
        return AjaxSelenium.getCurrentContext(this);
    }
}
