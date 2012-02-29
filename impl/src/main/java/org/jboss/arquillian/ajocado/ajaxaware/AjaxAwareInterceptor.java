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
package org.jboss.arquillian.ajocado.ajaxaware;

import static org.jboss.arquillian.ajocado.Graphene.waitAjax;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.ajocado.command.CommandContext;
import org.jboss.arquillian.ajocado.command.CommandInterceptor;
import org.jboss.arquillian.ajocado.command.CommandInterceptorException;
import org.jboss.arquillian.ajocado.framework.GrapheneConfiguration;
import org.jboss.arquillian.ajocado.framework.GrapheneConfiguration.TimeoutType;
import org.jboss.arquillian.ajocado.framework.GrapheneConfigurationContext;
import org.jboss.arquillian.ajocado.waiting.Waiting;

import com.thoughtworks.selenium.SeleniumException;

/**
 * Class to use to extend functionality of command processors to catch Selenium exceptions and repeat the command for
 * specific issue types (like Permission denied in Internet Explorer).
 *
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AjaxAwareInterceptor implements CommandInterceptor {

    private static final String[] PERMISSION_DENIED = new String[] {
        "ERROR: Threw an exception: Permission denied",
        "ERROR: Command execution failure. Please search the forum at http://clearspace.openqa.org for error"
            + " details from the log window.  The error message is: Permission denied",
        "ERROR: Threw an exception: Error executing strategy function jquery: Permission denied", };

    private final GrapheneConfiguration configuration = GrapheneConfigurationContext.getProxy();

    private final Waiting<?> wait = waitAjax;

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
     */
    @Override
    public void intercept(final CommandContext ctx) throws CommandInterceptorException {
        long end = System.currentTimeMillis() + configuration.getTimeout(TimeoutType.AJAX);
        boolean exceptionLogged = false;
        while (System.currentTimeMillis() < end) {
            try {
                ctx.invoke();
                return;
            } catch (SeleniumException e) {
                final String message = StringUtils.defaultString(e.getMessage());

                if (ArrayUtils.contains(PERMISSION_DENIED, message)) {
                    System.err.println(message);
                    if (!exceptionLogged) {
                        exceptionLogged = true;
                        System.err.println(ctx.toString());
                        e.printStackTrace();
                    }
                    wait.waitForTimeout();
                    continue;
                }

                throw e;
            }
        }
        throw new PermissionDeniedException("Fails with 'Permission denied' errors when trying to execute jQuery");
    }

    /**
     * Thrown when the Selenium wasn't able in given interval to call the given command with no SeleniumException with
     * type of Permission Denied catched.
     */
    public static class PermissionDeniedException extends RuntimeException {
        private static final long serialVersionUID = 501755400552888059L;

        public PermissionDeniedException(String message) {
            super(message);
        }
    }

}
