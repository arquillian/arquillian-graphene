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
package org.arquillian.extension.recorder.screenshooter.browser.impl;

import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.openqa.selenium.WebDriver;

/**
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class InterceptorRegistry {

    private final List<Interceptor> registeredInterceptors = new ArrayList<Interceptor>();

    public void register(Interceptor interceptor) {
        registeredInterceptors.add(interceptor);
        ((GrapheneProxyInstance) getWebDriver()).registerInterceptor(interceptor);
    }

    public void registerAll(List<Interceptor> interceptors) {
        for(Interceptor interceptor : interceptors) {
            register(interceptor);
        }
    }

    public List<Interceptor> unregisterAll() {
        for(Interceptor interceptor : registeredInterceptors) {
            ((GrapheneProxyInstance) getWebDriver()).unregisterInterceptor(interceptor);
        }
        List<Interceptor> copy = new ArrayList<Interceptor>(registeredInterceptors);
        registeredInterceptors.clear();
        return copy;
    }

    private WebDriver getWebDriver() {
        return GrapheneContext.getContextFor(Default.class).getWebDriver();
    }
}