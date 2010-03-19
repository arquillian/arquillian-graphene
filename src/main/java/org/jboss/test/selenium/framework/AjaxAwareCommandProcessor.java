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
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AjaxAwareCommandProcessor extends HttpCommandProcessor {
	public AjaxAwareCommandProcessor(String serverHost, int serverPort, String browserStartCommand, String browserURL) {
		super(serverHost, serverPort, browserStartCommand, browserURL);
	}

	private abstract class AjaxCommand<T> {
		public abstract T command();
	}

	private static String[] PERMISSION_DENIED = new String[] {
			"ERROR: Threw an exception: Error executing strategy function jquery: Permission denied",
			"ERROR: Threw an exception: Permission denied",
//			"ERROR: Threw an exception: Object doesn't support this property or method",
//			"ERROR: Command execution failure. Please search the forum at http://clearspace.openqa.org for error details from the log window.  The error message is: Permission denied",
//			"ERROR: Threw an exception: null property value",
			};

	private <T> T doAjax(final AjaxCommand<T> ajaxCommand) {
		final AssertionError fail = new AssertionError("Fails with Permission denied when trying to execute jQuery");

		final T start = null;
		return Wait.noDelay().timeout(Wait.DEFAULT_TIMEOUT).interval(1000).failWith(fail).waitForChangeAndReturn(start,
				new Retriever<T>() {
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
								} else {
								}
								return null;
							}

							throw e;
						}
					}
				});
	}

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
