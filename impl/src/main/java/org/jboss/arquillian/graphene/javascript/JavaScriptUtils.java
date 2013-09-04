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
package org.jboss.arquillian.graphene.javascript;

import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.android.AndroidDriver;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public final class JavaScriptUtils {

    private JavaScriptUtils() {
    }

    public static Object execute(JavascriptExecutor executor, org.jboss.arquillian.graphene.spi.javascript.JavaScript javaScript, Object... args) {
        return execute(executor, javaScript.getSourceCode(), args);
    }

    public static Object execute(JavascriptExecutor executor, String javaScript, Object... args) {
        try {
            if ((executor instanceof AndroidDriver) || (executor instanceof GrapheneProxyInstance && ((GrapheneProxyInstance) executor).unwrap() instanceof AndroidDriver)) {
                return executor.executeScript(javaScript.replace("\n", ""), args);
            } else {
                return executor.executeScript(javaScript, args);
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

}
