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
package org.jboss.arquillian.graphene.ftest.intercept;

import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.EventContext;
import org.jboss.arquillian.core.spi.LoadableExtension;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.jboss.arquillian.test.spi.event.suite.Test;
import org.openqa.selenium.WebDriver;

public class InterceptorRegistrationExtension implements LoadableExtension {

    public static volatile boolean invoked = false;

    @Override
    public void register(ExtensionBuilder builder) {
        builder.observer(this.getClass());
    }

    public void register_interceptor(@Observes EventContext<Test> ctx) {
        invoked = false;
        try {
            Test event = ctx.getEvent();
            if (event.getTestClass().getJavaClass() == TestInterceptorRegistration.class) {
                WebDriver browser = org.jboss.arquillian.graphene.context.GrapheneContext.getContextFor(Default.class).getWebDriver();
                ((GrapheneProxyInstance) browser).registerInterceptor(new Interceptor() {

                    @Override
                    public Object intercept(InvocationContext context) throws Throwable {
                        invoked = true;
                        return context.invoke();
                    }

                    @Override
                    public int getPrecedence() {
                        return 0;
                    }
                });
            }
        } finally {
            ctx.proceed();
        }
    }
}
