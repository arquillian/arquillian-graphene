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
package org.jboss.arquillian.graphene;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.PageFragmentEnricher;
import org.jboss.arquillian.graphene.guard.RequestGuardFactory;
import org.jboss.arquillian.graphene.javascript.JSInterfaceFactory;
import org.jboss.arquillian.graphene.location.LocationEnricher;
import org.jboss.arquillian.graphene.page.document.Document;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.request.RequestGuard;
import org.jboss.arquillian.graphene.request.RequestType;
import org.jboss.arquillian.graphene.wait.WebDriverWait;
import org.jboss.arquillian.graphene.wait.WebDriverWaitImpl;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DefaultGrapheneRuntime extends GrapheneRuntime {

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    /**
     * Returns the guarded object checking whether the HTTP request is done during
     * each method invocation. If the request is not found,
     * the {@link org.jboss.arquillian.graphene.request.RequestGuardException} is thrown.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @return the guarded object
     */
    public <T> T guardHttp(T target) {
        return getRequestGuardFactoryFor(target).guard(target, RequestType.HTTP, true);
    }

    /**
     * Returns the guarded object checking that no request is done during
     * each method invocation. If any request is found,
     * the {@link org.jboss.arquillian.graphene.request.RequestGuardException} is thrown.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @return the guarded object
     */
    public <T> T guardNoRequest(T target) {
        return getRequestGuardFactoryFor(target).guard(target, RequestType.NONE, true);
    }

    /**
     * Returns the guarded object checking whether the Ajax (XHR) request is done during
     * each method invocation. If the request is not found,
     * the {@link org.jboss.arquillian.graphene.request.RequestGuardException} is thrown.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @return the guarded object
     */
    public <T> T guardAjax(T target) {
        return getRequestGuardFactoryFor(target).guard(target, RequestType.XHR, true);
    }

    public <T> T waitForHttp(T target) {
        return getRequestGuardFactoryFor(target).guard(target, RequestType.HTTP, false);
    }

    public WebDriverWait<Void> waitAjax() {
        return waitAjax(context().getWebDriver());
    }

    public WebDriverWait<Void> waitAjax(WebDriver driver) {
        return new WebDriverWaitImpl<Void>(null, driver, ((GrapheneProxyInstance) driver).getContext().getConfiguration().getWaitAjaxInterval());
    }

    public WebDriverWait<Void> waitGui() {
        return waitGui(context().getWebDriver());
    }

    public WebDriverWait<Void> waitGui(WebDriver driver) {
        return new WebDriverWaitImpl<Void>(null, driver, ((GrapheneProxyInstance) driver).getContext().getConfiguration().getWaitGuiInterval());
    }

    public WebDriverWait<Void> waitModel() {
        return waitModel(context().getWebDriver());
    }

    public WebDriverWait<Void> waitModel(WebDriver driver) {
        return new WebDriverWaitImpl<Void>(null, driver, ((GrapheneProxyInstance) driver).getContext().getConfiguration().getWaitModelInterval());
    }

    public <T> T createPageFragment(Class<T> clazz, WebElement root) {
        return PageFragmentEnricher.createPageFragment(clazz, root);
    }

    public <T> T goTo(Class<T> clazz) {
        return goTo(clazz, Default.class);
    }

    public <T> T goTo(Class<T> pageObject, Class<?> browserQualifier) {
        LocationEnricher locationEnricher = serviceLoader.get().onlyOne(LocationEnricher.class);
        return locationEnricher.goTo(pageObject, browserQualifier);
    }

    private RequestGuardFactory getRequestGuardFactoryFor(Object target) {
        GrapheneContext context;
        if (GrapheneProxy.isProxyInstance(target)) {
            context = ((GrapheneProxyInstance) target).getContext();
        } else {
            context = context();
        }
        return new RequestGuardFactory(
                JSInterfaceFactory.create(context, RequestGuard.class),
                JSInterfaceFactory.create(context, Document.class),
                context);
    }

    private GrapheneContext context() {
        GrapheneContext context = GrapheneContext.lastContext();
        if (context == null) {
            throw new IllegalStateException("The last used Graphene context is not available.");
        }
        return context;
    }

}