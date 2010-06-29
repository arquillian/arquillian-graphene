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
package org.jboss.test.selenium.waiting.ajax;

import static org.jboss.test.selenium.utils.text.SimplifiedFormat.format;
import static org.jboss.test.selenium.encapsulated.JavaScript.js;

import org.jboss.test.selenium.encapsulated.JavaScript;
import org.jboss.test.selenium.framework.AjaxSelenium;
import org.jboss.test.selenium.framework.AjaxSeleniumProxy;
import org.jboss.test.selenium.waiting.DefaultWaiting;

/**
 * <p>
 * Implementation of waiting for satisfaction of conditions on page after the Ajax request.
 * </p>
 * 
 * <p>
 * It uses custom JavaScript and {@link com.thoughtworks.selenium.Selenium.Selenium#waitForCondition(String, String)} to
 * wait for satisfying given condition.
 * </p>
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
public class AjaxWaiting extends DefaultWaiting<AjaxWaiting> {

    /**
     * Proxy to local selenium instance
     */
    private AjaxSelenium selenium = AjaxSeleniumProxy.getInstance();
    
    /**
     * Stars loop waiting to satisfy condition.
     * 
     * @param condition
     *            what wait for to be satisfied
     */
    public void until(JavaScriptCondition condition) {
        selenium.waitForCondition(condition.getJavaScriptCondition(), this.getTimeout());
    }

    /**
     * Waits until Retrieve's implementation doesn't retrieve value other than oldValue.
     * 
     * @param <T>
     *            type of value what we are waiting for change
     * @param oldValue
     *            value that we are waiting for change
     * @param retrieve
     *            implementation of retrieving actual value
     */
    public <T> void waitForChange(T oldValue, JavaScriptRetriever<T> retrieve) {
        JavaScript waitCondition = js(format("{0} != '{1}'", retrieve.getJavaScriptRetrieve().getAsString(), oldValue));
        selenium.waitForCondition(waitCondition, this.getTimeout());
    }

    /**
     * Waits until Retrieve's implementation doesn't retrieve value other than oldValue and this new value returns.
     * 
     * @param <T>
     *            type of value what we are waiting for change
     * @param oldValue
     *            value that we are waiting for change
     * @param retrieve
     *            implementation of retrieving actual value
     * @return new retrieved value
     */
    public <T> T waitForChangeAndReturn(T oldValue, JavaScriptRetriever<T> retrieve) {
        final String oldValueString = retrieve.getConvertor().forwardConversion(oldValue);
        JavaScript waitingRetriever =
            js(format("selenium.waitForCondition({0} != '{1}'); {0}", retrieve.getJavaScriptRetrieve().getAsString(),
                oldValueString));
        String retrieved = selenium.getEval(waitingRetriever);
        return retrieve.getConvertor().backwardConversion(retrieved);
    }
}