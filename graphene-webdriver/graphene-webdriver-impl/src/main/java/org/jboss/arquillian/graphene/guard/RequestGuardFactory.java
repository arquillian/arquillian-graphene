/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.guard;

import static org.jboss.arquillian.graphene.Graphene.waitModel;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.context.GrapheneConfigurationContext;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.page.RequestType;
import org.jboss.arquillian.graphene.page.document.Document;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.openqa.selenium.WebDriver;

import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RequestGuardFactory {

    private static RequestGuard guard = JSInterfaceFactory.create(RequestGuard.class);
    private static Document document = JSInterfaceFactory.create(Document.class);

    private static DocumentReady documentReady = new DocumentReady();

    /**
     * Returns the guarded object checking whether the request of the given type is done during each method invocation. If the
     * request is not found, the {@link RequestGuardException} is thrown.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @param requestExpected the request type being checked after each method invocation
     * @return the guarded object
     */
    public static <T> T guard(T target, final RequestType requestExpected) {
        if (requestExpected == null) {
            throw new IllegalArgumentException("The paremeter [requestExpected] is null.");
        }
        if (target == null) {
            throw new IllegalArgumentException("The paremeter [target] is null.");
        }
        GrapheneProxyInstance proxy;
        if (GrapheneProxy.isProxyInstance(target)) {
            proxy = (GrapheneProxyInstance) ((GrapheneProxyInstance) target).copy();
        } else {
            proxy = (GrapheneProxyInstance) GrapheneProxy.getProxyForTarget(target);
        }
        proxy.registerInterceptor(new Interceptor() {
            @Override
            public Object intercept(InvocationContext context) throws Throwable {

                guard.clearRequestDone();

                Object result = context.invoke();

                final long timeout = System.currentTimeMillis()
                        + TimeUnit.SECONDS.toMillis(GrapheneConfigurationContext.getProxy().getWaitGuardInterval());
                final long toSleep = Math.min(GrapheneConfigurationContext.getProxy().getWaitGuardInterval() * 100, 200);

                while (System.currentTimeMillis() < timeout) {
                    RequestType requestDone = guard.getRequestDone();
                    if (!requestDone.equals(RequestType.NONE)) {
                        if (requestDone.equals(requestExpected)) {
                            if (requestDone.equals(RequestType.HTTP)) {
                                waitModel().withMessage("Document didn't become ready").until(documentReady);
                            }
                            return result;
                        } else {
                            throw new RequestGuardException(requestExpected, guard.getRequestDone());
                        }
                    } else {
                        try {
                            Thread.sleep(toSleep);
                        } catch (InterruptedException ignored) {
                        }
                    }
                }

                if (requestExpected.equals(RequestType.NONE)) {
                    return result;
                } else {
                    throw new RequestGuardException(requestExpected, guard.getRequestDone());
                }
            }
        });
        return (T) proxy;
    }

    private static class DocumentReady implements Predicate<WebDriver> {
        public boolean apply(WebDriver arg0) {
            return "complete".equals(document.getReadyState());
        }
    }
}
