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
package org.jboss.arquillian.ajocado.waiting.conditions;

import static org.apache.commons.lang.StringEscapeUtils.escapeJavaScript;
import static org.jboss.arquillian.ajocado.javascript.JavaScript.js;

import org.apache.commons.lang.Validate;
import org.jboss.arquillian.ajocado.framework.AjaxSelenium;
import org.jboss.arquillian.ajocado.framework.AjaxSeleniumContext;
import org.jboss.arquillian.ajocado.javascript.JavaScript;
import org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition;
import org.jboss.arquillian.ajocado.waiting.selenium.SeleniumCondition;

import com.thoughtworks.selenium.SeleniumException;

/**
 * 
 * <p>
 * Implementation of Condition for waiting if alert is shown and its message has has given value.
 * </p>
 * 
 * <p>
 * If the alert appears but it's message doesn't match expected message, the SeleniumException is thrown.
 * </p>
 * 
 * <p>
 * Implements Condition and JavaScriptCondition used in SeleniumWaiting and AjaxWaiting.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AlertEquals implements SeleniumCondition, JavaScriptCondition {

    /**
     * Proxy to local selenium instance
     */
    private AjaxSelenium selenium = AjaxSeleniumContext.getProxy();

    /** The message. */
    private String message;

    /**
     * Instantiates a new message equals.
     */
    protected AlertEquals() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.waiting.Condition#isTrue()
     */
    @Override
    public boolean isTrue() {
        Validate.notNull(message);

        if (!selenium.isAlertPresent()) {
            return false;
        }

        String alertMessage = selenium.getAlert();

        if (!message.equals(alertMessage)) {
            throw new SeleniumException("Alert has been displayed, but the message '" + message
                + "' doesn't equal to the expected '" + alertMessage + "'");
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jboss.arquillian.ajocado.waiting.ajax.JavaScriptCondition#getJavaScriptCondition()
     */
    @Override
    public JavaScript getJavaScriptCondition() {
        Validate.notNull(message);
        String escapedMessage = escapeJavaScript(message);

        return js(
            "selenium.isAlertPresent() && ((alertMessage = selenium.getAlert()) == '{0}' "
                + " || selenium.throwError('Alert has been displayed, "
                + "but the message \\'' + alertMessage + '\\' doesn\\'t equal to the expected \\'{0}\\''))")
            .parametrize(escapedMessage);
    }

    /**
     * Factory method.
     * 
     * @return single instance of AlertEquals
     */
    public static AlertEquals getInstance() {
        return new AlertEquals();
    }

    /**
     * <p>
     * Returns the AlertEquals instance with text set.
     * </p>
     * 
     * <p>
     * For equality with this text the condition will wait.
     * </p>
     * 
     * @param message
     *            it should wait for equality
     * @return the AlertEquals object with preset text
     */
    public AlertEquals message(String message) {
        Validate.notNull(message);

        AlertEquals copy = copy();
        copy.message = message;

        return copy;
    }

    /**
     * Returns the exact copy of this ElementPresent object.
     * 
     * @return the copy of this AlertEquals object
     */
    private AlertEquals copy() {
        AlertEquals copy = new AlertEquals();
        copy.message = message;
        return copy;
    }
}
