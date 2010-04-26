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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.jboss.test.selenium.waiting.Retriever;
import org.jboss.test.selenium.waiting.Wait;

import com.thoughtworks.selenium.HttpCommandProcessor;
import com.thoughtworks.selenium.SeleniumException;

/**
 * Class to use to extend HttpCommandProcessor functionality to catch Selenium's JavaScript exceptions and repeat the
 * command for specific issue types (like Permission denied in Internet Explorer).
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AjaxAwareCommandProcessor extends HttpCommandProcessor {

    /** The Constant PERMISSION_DENIED. */
    private static final String[] PERMISSION_DENIED = new String[] {
        "ERROR: Threw an exception: Permission denied",
        "ERROR: Command execution failure. Please search the forum at http://clearspace.openqa.org for error details"
            + " from the log window.  The error message is: Permission denied",
        "ERROR: Threw an exception: Error executing strategy function jquery: Permission denied", };

    /** The Constant DEFAULT_WAITING_INTERVAL. */
    private static final int DEFAULT_WAITING_INTERVAL = 1000;

    /**
     * Instantiates a new ajax aware command processor.
     * 
     * @param serverHost
     *            the selenium server host
     * @param serverPort
     *            the selenium server port
     * @param browserStartCommand
     *            the browser start command
     * @param browserURL
     *            the browser url
     */
    public AjaxAwareCommandProcessor(String serverHost, int serverPort, String browserStartCommand, String browserURL) {
        super(serverHost, serverPort, browserStartCommand, browserURL);
    }

    /**
     * Exception handler wrapper used in AjaxAwarecommandProcessor
     * 
     * @param <T>
     *            the return type for given command
     */
    private abstract class AjaxCommand<T> {

        /**
         * Command which should be executed wrapped by exception handler
         * 
         * @return the t
         */
        public abstract T command();
    }

    /**
     * <p>
     * Executes the command wrapped in exception handler.
     * </p>
     * 
     * <p>
     * Reacts to exceptions with 'Permission denied' type and try to reexecute the command in this situation.
     * </p>
     * 
     * <p>
     * Prints the exception stack trace to help identify the problematic commands.
     * </p>
     * 
     * <p>
     * Internally uses the {@link Wait} class to implement waiting repeated command.
     * </p>
     * 
     * @param <T>
     *            return type of the selenium command
     * @param ajaxCommand
     *            the command to be executed by selenium
     * @return the result of selenium command
     */
    private <T> T doAjax(final AjaxCommand<T> ajaxCommand) {
        final AssertionError fail = new AssertionError("Fails with Permission denied when trying to execute jQuery");

        final T start = null;
        return Wait.noDelay().timeout(Wait.DEFAULT_TIMEOUT).interval(DEFAULT_WAITING_INTERVAL).failWith(fail)
            .waitForChangeAndReturn(start, new Retriever<T>() {
                boolean exceptionLogged = false;

                public T retrieve() {
                    try {
                        return ajaxCommand.command();
                    } catch (SeleniumException e) {
                        final String message = StringUtils.defaultString(e.getMessage());
                        if (ArrayUtils.contains(PERMISSION_DENIED, message)) {
                            System.err.println(message);
                            if (!exceptionLogged) {
                                exceptionLogged = true;
                                System.err.println(ajaxCommand);
                                e.printStackTrace();
                            }
                            return null;
                        }

                        throw e;
                    }
                }
            });
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.thoughtworks.selenium.HttpCommandProcessor#doCommand(java.lang.String, java.lang.String[])
     */
    @Override
    public String doCommand(final String commandName, final String[] args) {
        return doAjax(new AjaxCommand<String>() {
            @Override
            public String command() {
                return AjaxAwareCommandProcessor.super.doCommand(commandName, args);
            }

            @Override
            public String toString() {
                return new ToStringBuilder(this).append("commandName", commandName).append("args", args).toString();
            }
        });
    }
}
