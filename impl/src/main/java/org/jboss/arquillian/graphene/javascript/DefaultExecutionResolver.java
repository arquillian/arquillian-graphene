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

import java.lang.reflect.Method;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.util.logging.Logger;

import org.jboss.arquillian.graphene.context.ExtendedGrapheneContext;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.page.extension.JavaScriptPageExtension;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistry;
import org.openqa.selenium.JavascriptExecutor;

import com.google.common.io.Resources;

/**
 * This resolver uses page extension mechanism to install needed JavaScript
 * and other required extensions.
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class DefaultExecutionResolver implements ExecutionResolver, org.jboss.arquillian.graphene.javascript.JavaScript.DefaultExecutionResolver  {

    public static final String FUNCTION;

    public static final String CALL = "return invokeInterface({0}, \"{1}\", arguments);";

    protected static final Logger LOGGER = Logger.getLogger(DefaultExecutionResolver.class.getName());

    static {
        try {
            URL resource = JSInterfaceHandler.class.getResource("call.js");
            FUNCTION = Resources.toString(resource, Charset.defaultCharset());
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public Object execute(GrapheneContext context, JSCall call) {

        ExtendedGrapheneContext c = (ExtendedGrapheneContext) context;

        // check name
        JSInterface target = call.getTarget();
        if (target.getName() == null) {
            throw new IllegalStateException("Can't use " + this.getClass() + " for " + target.getInterface() + ", because the @JavaScript annotation doesn't define non empty value()");
        }
        // register page extension
        registerExtension(c.getPageExtensionRegistry(), target);
        // try to execute javascript, if fails install the extension
        final long LIMIT = context.getConfiguration().getJavascriptInstallationLimit();
        for (long i=1; i<=5; i++) {
            try {
                // execute javascript
                Object returnValue = executeScriptForCall((JavascriptExecutor) context.getWebDriver(JavascriptExecutor.class), call);
                Object castedResult = castResult(call, returnValue);
                return castedResult;
            } catch (RuntimeException e) {
                // if the limit is reached -> FAIL
                if (i == LIMIT) {
                    throw new IllegalStateException("Can't invoke the javacript " + call.getTarget().getInterface().getName() + "#" + call.getMethod().getMethod().getName() + "()", e);
                // try to install the extension
                } else {
                    try {
                        c.getPageExtensionInstallatorProvider().installator(target.getName()).install();
                    } catch (RuntimeException ignored) {
                    }
                }
            }
        }
        // this situation shouldn't happen
        throw new IllegalStateException("Can't invoke the javacript " + call.getTarget().getInterface().getName() + "#" + call.getMethod().getMethod().getName() + "()");
    }

    protected Object[] castArguments(Object[] arguments) {

        Object[] result = new Object[arguments.length];
        System.arraycopy(arguments, 0, result, 0, arguments.length);

        for (int i = 0; i < result.length; i++) {
            Object arg = result[i];
            if (arg != null && arg.getClass().isEnum()) {
                result[i] = castEnumToString(arg);
            }
        }

        return result;
    }

    protected Object castResult(JSCall call, Object returnValue) {
        Class<?> returnType = call.getMethod().getMethod().getReturnType();

        if (returnType.isEnum()) {
            return castStringToEnum(returnType, returnValue);
        }

        return returnValue;
    }

    protected Object castEnumToString(Object enumValue) {
        return enumValue.toString();
    }

    protected Object castStringToEnum(Class<?> returnType, Object returnValue) {
        try {
            Method method = returnType.getMethod("valueOf", String.class);
            return method.invoke(null, returnValue.toString());
        } catch (Exception e) {
            throw new IllegalStateException("returnValue '" + returnValue + "' can't be cast to enum value", e);
        }
    }

    protected Object executeScriptForCall(JavascriptExecutor executor, JSCall call) {
        String script = resolveScriptToExecute(call);
        Object[] arguments = castArguments(call.getArguments());
        Object returnValue = JavaScriptUtils.execute(executor, script, arguments);
        if (returnValue instanceof String && ((String) returnValue).startsWith("GRAPHENE ERROR: ")) {
            throw new IllegalStateException("exception thrown when executing method '" + call.getMethod().getName() + "': " + ((String) returnValue).substring(16));
        }
        return returnValue;
    }

    protected String resolveScriptToExecute(JSCall call) {
        String functionCall = MessageFormat.format(CALL, call.getTarget().getName(), call.getMethod().getName());
        String functionDefinitionWithCall = FUNCTION + functionCall;
        return functionDefinitionWithCall;
    }

    protected <T> void registerExtension(PageExtensionRegistry registry, JSInterface target) {
        if (target.getName() == null || target.getName().length() == 0) {
            throw new IllegalArgumentException("The extension " + target.getInterface() + "has no mapping.");
        }
        if (registry.getExtension(target.getName()) != null) {
            return;
        }
        JavaScriptPageExtension extension = new JavaScriptPageExtension(target.getInterface());
        registry.register(extension);
        for (JSInterface dependency: target.getJSInterfaceDependencies()) {
            registerExtension(registry, dependency);
        }
    }

}
