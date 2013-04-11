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
package org.jboss.arquillian.graphene;

import org.jboss.arquillian.graphene.condition.AttributeConditionFactory;
import org.jboss.arquillian.graphene.condition.ElementConditionFactory;
import org.jboss.arquillian.graphene.condition.attribute.ElementAttributeConditionFactory;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.condition.locator.ElementLocatorConditionFactory;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import org.jboss.arquillian.graphene.context.GrapheneConfigurationContext;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.PageFragmentEnricher;
import org.jboss.arquillian.graphene.guard.RequestGuardFactory;
import org.jboss.arquillian.graphene.page.RequestType;
import org.jboss.arquillian.graphene.wait.WebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class Graphene {

    /**
     * <p>
     * DEPRECATED: use fluent API instead ({@link #waitAjax()} / {@link #waitGui()} / {@link #waitModel()}
     * and {@link WebDriverWait#until() }). For example:
     *
     * </p>
     * <pre>
     * Graphene.waitAjax().until().element(...).attribute(...).is().present();
     * </pre>
     * <p>
     * Returns an attribute condition factory which can be used to formulate
     * conditions related to the given attribute.
     * </p>
     *
     * @param element element which the attribute belongs to
     * @param attribute attribute name
     * @see org.jboss.arquillian.graphene.wait.ElementBuilder#attribute(java.lang.String)
     */
    @Deprecated
    public static AttributeConditionFactory attribute(WebElement element, String attribute) {
        return new ElementAttributeConditionFactory(element, attribute);
    }

    /**
     * <p>
     * DEPRECATED: use fluent API instead ({@link #waitAjax()} / {@link #waitGui()} / {@link #waitModel()}
     * and {@link WebDriverWait#until() }). For example:
     *
     * </p>
     * <pre>
     * Graphene.waitAjax().until().element(...).is().present();
     * </pre>
     * <p>
     * Returns an element condition factory which can be used to formulate
     * conditions related to the given element.
     * </p>
     *
     * @param element
     * @see org.jboss.arquillian.graphene.wait.FluentBuilder#element(org.openqa.selenium.WebElement)
     */
    @Deprecated
    public static ElementConditionFactory element(WebElement element) {
        return new WebElementConditionFactory(element);
    }

    /**
     * <p>
     * DEPRECATED: use fluent API instead ({@link #waitAjax()} / {@link #waitGui()} / {@link #waitModel()}
     * and {@link WebDriverWait#until() }). For example:
     *
     * </p>
     * <pre>
     * Graphene.waitAjax().until().element(...).is().present();
     * </pre>
     * <p>
     * Returns an element condition factory which can be used to formulate
     * conditions related to the element determined by the given locater.
     * </p>
     *
     * @param locator
     * @see org.jboss.arquillian.graphene.wait.FluentBuilder#element(org.openqa.selenium.By)
     */
    @Deprecated
    public static ElementConditionFactory element(By locator) {
        return new ElementLocatorConditionFactory(locator);
    }

    /**
     * Returns the guarded object checking whether the HTTP request is done during
     * each method invocation. If the request is not found,
     * the {@link org.jboss.arquillian.graphene.guard.RequestGuardException} is thrown.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @return the guarded object
     */
    public static <T> T guardHttp(T target) {
        return RequestGuardFactory.guard(target, RequestType.HTTP);
    }

    /**
     * Returns the guarded object checking that no request is done during
     * each method invocation. If any request is found,
     * the {@link org.jboss.arquillian.graphene.guard.RequestGuardException} is thrown.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @return the guarded object
     */
    public static <T> T guardNoRequest(T target) {
        return RequestGuardFactory.guard(target, RequestType.NONE);
    }

    /**
     * Returns the guarded object checking whether the XHR (Ajax) request is done during
     * each method invocation. If the request is not found,
     * the {@link org.jboss.arquillian.graphene.guard.RequestGuardException} is thrown.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @return the guarded object
     *
     * @deprecated use {@link #guardAjax(Object)} instead
     */
    @Deprecated
    public static <T> T guardXhr(T target) {
        return RequestGuardFactory.guard(target, RequestType.XHR);
    }

    /**
     * Returns the guarded object checking whether the Ajax (XHR) request is done during
     * each method invocation. If the request is not found,
     * the {@link org.jboss.arquillian.graphene.guard.RequestGuardException} is thrown.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @return the guarded object
     */
    public static <T> T guardAjax(T target) {
        return RequestGuardFactory.guard(target, RequestType.XHR);
    }

    public static WebDriverWait<Void> waitAjax() {
        return waitAjax(GrapheneContext.getProxy());
    }

    public static WebDriverWait<Void> waitAjax(WebDriver driver) {
        return new WebDriverWait<Void>(null, driver, getConfiguration().getWaitAjaxInterval());
    }

    public static WebDriverWait<Void> waitGui() {
        return waitGui(GrapheneContext.getProxy());
    }

    public static WebDriverWait<Void> waitGui(WebDriver driver) {
        return new WebDriverWait<Void>(null, driver, getConfiguration().getWaitGuiInterval());
    }

    public static WebDriverWait<Void> waitModel() {
        return waitModel(GrapheneContext.getProxy());
    }

    public static WebDriverWait<Void> waitModel(WebDriver driver) {
        return new WebDriverWait<Void>(null, driver, getConfiguration().getWaitModelInterval());
    }

    public static <T> T createPageFragment(Class<T> clazz, WebElement root) {
        return PageFragmentEnricher.createPageFragment(clazz, root);
    }

    private static GrapheneConfiguration getConfiguration() {
        return GrapheneConfigurationContext.getProxy();
    }

}
