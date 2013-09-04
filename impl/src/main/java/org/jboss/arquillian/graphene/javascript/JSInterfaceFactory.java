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

import java.lang.reflect.Modifier;

import org.jboss.arquillian.graphene.context.GrapheneContext;

public class JSInterfaceFactory<T> {

    private final JSInterfaceHandler handler;

    public JSInterfaceFactory(GrapheneContext context, Class<T> jsInterface) {
        if (!jsInterface.isInterface() && !Modifier.isAbstract(jsInterface.getModifiers())) {
            throw new IllegalArgumentException("interface or abstract class must be provided :" + jsInterface);
        }
        this.handler = new JSInterfaceHandler(new JSTarget(jsInterface), context);
    }

    public static <T> T create(GrapheneContext context, Class<T> jsInterface) {
        return new JSInterfaceFactory<T>(context, jsInterface).instantiate();
    }

    @SuppressWarnings("unchecked")
    public T instantiate() {
        Class<?> jsInterface = handler.getTarget().getInterface();
        return (T) ClassImposterizer.INSTANCE.imposterise(handler, jsInterface);
    }
}
