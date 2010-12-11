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

import static org.jboss.test.selenium.guard.GuardedCommands.INTERACTIVE_COMMANDS;
import static org.jboss.test.selenium.request.RequestType.HTTP;
import static org.jboss.test.selenium.request.RequestType.NONE;

import org.jboss.test.selenium.SystemProperties;
import org.jboss.test.selenium.SystemProperties.SeleniumTimeoutType;
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.AjaxSeleniumProxy;
import org.jboss.test.selenium.interception.CommandContext;
import org.jboss.test.selenium.interception.CommandInterceptionException;
import org.jboss.test.selenium.interception.CommandInterceptor;
import org.jboss.test.selenium.request.RequestType;


import com.thoughtworks.selenium.SeleniumException;

/**
 * The Guard which guards that request what was expected to be done will be actually done.
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class RequestTypeGuard implements CommandInterceptor {

    /**
     * Proxy to local selenium instance
     */
    private AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();

    /**
     * The request what is expected to be done
     */
    private RequestType requestExpected;

    /**
     * Denotes that request can be interlayed by any other type of request
     */
    private boolean interlayed;

    /**
     * Constructs the guard with predefined expected RequestType
     * 
     * @param requestExpected
     *            the RequestType which is expected to be done
     * @param interlayed
     *            indicates whenever the request can be interlayed by another request
     */
    public RequestTypeGuard(RequestType requestExpected, boolean interlayed) {
        super();
        this.requestExpected = requestExpected;
        this.interlayed = interlayed;
    }

    /**
     * Enfolds the command with guarding code to detect request type
     */
    public void intercept(CommandContext ctx) throws CommandInterceptionException {
        final String command = ctx.getCommand();

        if (INTERACTIVE_COMMANDS.contains(command) || command.equals("getEval")) {
            doBeforeCommand();
        }
        ctx.invoke();
        if (INTERACTIVE_COMMANDS.contains(command) || command.equals("getEval")) {
            doAfterCommand();
        }
    }

    /**
     * Install the PageExtensions (which is used to figure out, what requestType was actually done) and clear the
     * request type to NONE state.
     */
    public void doBeforeCommand() {
        selenium.getPageExtensions().install();
        selenium.getRequestInterceptor().clearRequestTypeDone();
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
     * @throws RequestTypeGuardException
     *             when done requestType doesn't equal to expected one
     */
    public void doAfterCommand() {
        final long end = System.currentTimeMillis() + SystemProperties.getSeleniumTimeout(SeleniumTimeoutType.AJAX);
        
        RequestType lastRequestDone = NONE;
        
        while (System.currentTimeMillis() <= end) {
            try {
                selenium.getRequestInterceptor().waitForRequestTypeChange();
            } catch (SeleniumException e) {
                // ignore the timeout exception
                System.out.println(e.getMessage());
            }

            RequestType requestDone = selenium.getRequestInterceptor().clearRequestTypeDone();

            if (requestDone == requestExpected) {
                lastRequestDone = requestDone;
                break;
            } else {
                if (interlayed) {
                    if (requestDone == HTTP) {
                        selenium.getPageExtensions().install();
                        selenium.getRequestInterceptor().clearRequestTypeDone();
                    }
                    if (requestDone != NONE) {
                        lastRequestDone = requestDone;
                    }
                    continue;
                } else {
                    lastRequestDone = requestDone;
                    break;
                }
            }
        }
        
        if (lastRequestDone != requestExpected) {
            throw new RequestTypeGuardException(requestExpected, lastRequestDone);
        }
    }
}
