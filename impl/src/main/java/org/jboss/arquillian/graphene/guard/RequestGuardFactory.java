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
package org.jboss.arquillian.graphene.guard;

import static org.jboss.arquillian.graphene.Graphene.waitAjax;

import java.util.concurrent.TimeUnit;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.page.document.Document;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.jboss.arquillian.graphene.request.RequestGuard;
import org.jboss.arquillian.graphene.request.RequestGuardException;
import org.jboss.arquillian.graphene.request.RequestState;
import org.jboss.arquillian.graphene.request.RequestType;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class RequestGuardFactory {

    private final RequestGuard guard;
    private final Document document;
    private final FluentWait<WebDriver, Void> waitGuard;
    private final GrapheneContext context;
    private final DocumentReady documentReady = new DocumentReady();
    private final RequestIsDone requestIsDone = new RequestIsDone();
    private final RequestChange requestChange = new RequestChange();


    public RequestGuardFactory(RequestGuard guard, Document document, GrapheneContext context) {
        this.guard = guard;
        this.document = document;
        this.context = context;
        this.waitGuard = waitAjax()
            .withTimeout(context.getConfiguration().getWaitGuardInterval(), TimeUnit.SECONDS)
            .pollingEvery(Math.min(context.getConfiguration().getWaitGuardInterval() * 100, 200), TimeUnit.MILLISECONDS);
    }

    /**
     * Returns the guarded object checking whether the request of the given type is done during each method invocation. If the
     * request is not found, the {@link RequestGuardException} is thrown.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @param requestExpected the request type being checked after each method invocation
     * @param strict indicates that the expected request type can be interleaved by another type
     * @return the guarded object
     */
    public <T> T guard(T target, final RequestType requestExpected, final boolean strict) {
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
            proxy = (GrapheneProxyInstance) GrapheneProxy.getProxyForTarget(context, target);
        }
        proxy.registerInterceptor(new Interceptor() {
            @Override
            public Object intercept(InvocationContext context) throws Throwable {

                guard.clearRequestDone();

                Object result = context.invoke();

                RequestType requestType;

                if (strict) {
                    requestType = waitForRequestChange();
                } else {
                    requestType = waitForRequestType(requestExpected);
                }

                if (requestType.equals(requestExpected)) {
                    waitForRequestFinished();
                } else {
                    throw new RequestGuardException(requestExpected, requestType);
                }

                return result;
            }

            private RequestType waitForRequestChange() {
                try {
                    return waitGuard.until(requestChange);
                } catch (TimeoutException e) {
                    return RequestType.NONE;
                }
            }

            private RequestType waitForRequestType(RequestType requestType) {
                try {
                    return waitGuard.until(new RequestTypeDone(requestExpected));
                } catch (TimeoutException e) {
                    return guard.getRequestType();
                }
            }

            private void waitForRequestFinished() {
                switch (requestExpected) {

                    case HTTP:
                        waitGuard.withMessage("Document didn't become ready").until(documentReady);
                        return;

                    case XHR:
                        waitGuard.until(requestIsDone);
                        return;

                    case NONE:
                        return;
                }
            }
        });
        return (T) proxy;
    }

    private class DocumentReady implements Predicate<WebDriver> {
        @Override
        public boolean apply(WebDriver driver) {
            return "complete".equals(document.getReadyState());
        }
    }

    private class RequestIsDone implements Predicate<WebDriver> {
        @Override
        public boolean apply(WebDriver driver) {
            RequestState state = guard.getRequestState();
            return RequestState.DONE.equals(state);
        }
    }

    private class RequestChange implements Function<WebDriver, RequestType> {
        @Override
        public RequestType apply(WebDriver driver) {
            RequestType type = guard.getRequestType();
            return (RequestType.NONE.equals(type)) ? null : type;
        }
    }

    private class RequestTypeDone implements Function<WebDriver, RequestType> {

        private RequestType requestExpected;

        public RequestTypeDone(RequestType requestExpected) {
            this.requestExpected = requestExpected;
        }

        @Override
        public RequestType apply(WebDriver driver) {
            RequestType type = guard.getRequestType();
            if (!requestExpected.equals(type)) {
                return null;
            }
            return type;
        }
    }
}