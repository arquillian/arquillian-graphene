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
import java.lang.reflect.Modifier;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.FieldValue;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperMethod;
import net.bytebuddy.implementation.bind.annotation.This;
import org.jboss.arquillian.graphene.bytebuddy.MethodInterceptor;
import org.jboss.arquillian.graphene.context.GrapheneContext;

public class JSInterfaceHandler implements MethodInterceptor {

    private final JSInterface target;
    private final GrapheneContext context;

    public JSInterfaceHandler(JSInterface target, GrapheneContext context) {
        this.target = target;
        this.context = context;
    }

    public JSInterface getTarget() {
        return target;
    }

    @RuntimeType
    public static Object intercept(@This Object self,
                                   @FieldValue("__interceptor") MethodInterceptor interceptor,
                                   @Origin Method method,
                                   @AllArguments Object[] args,
                                   @SuperMethod(nullIfImpossible = true) Method superMethod) throws Throwable {
        JSInterfaceHandler handler = (JSInterfaceHandler) interceptor;
        if (!handler.target.getInterface().isInterface()) {
            if (!Modifier.isAbstract(method.getModifiers())) {
                return superMethod.invoke(self, args);
            }
        }
        if (method.getName().equals("finalize") && method.getParameterTypes().length == 0) {
            return null;
        }
        if (method.getName().equals("unwrap")) {
            return handler.getTarget();
        }
        args = (args != null) ? args : new Object[]{};
        JSCall call = new JSCall(new JSMethod(handler.target, method), args);
        return handler.target.getResolver().execute(handler.context, call);
    }
}
