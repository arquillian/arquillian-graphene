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
import org.jboss.arquillian.graphene.condition.attribute.AttributeConditionFactoryImpl;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.condition.locator.ElementLocatorConditionFactory;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import org.jboss.arquillian.graphene.context.GrapheneConfigurationContext;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.guard.RequestGuardFactory;
import org.jboss.arquillian.graphene.page.RequestType;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class Graphene {

    /**
     * Returns an attribute condition factory which can be used to formulate
     * conditions related to the given attribute.
     *
     * @param element element which the attribute belongs to
     * @param attribute attribute name
     * @return
     */
    public static AttributeConditionFactory attribute(WebElement element, String attribute) {
        return new AttributeConditionFactoryImpl(element, attribute);
    }

    /**
     * Returns an element condition factory which can be used to formulate
     * conditions related to the given element.
     *
     * @param element
     * @return
     */
    public static ElementConditionFactory element(WebElement element) {
        return new WebElementConditionFactory(element);
    }

    /**
     * Returns an element condition factory which can be used to formulate
     * conditions related to the element determined by the given locater.
     *
     * @param locator
     * @return
     */
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
     */
    public static <T> T guardXhr(T target) {
        return RequestGuardFactory.guard(target, RequestType.XHR);
    }

    public static WebDriverWait waitAjax() {
        return waitAjax(GrapheneContext.getProxy());
    }

    public static WebDriverWait waitAjax(WebDriver driver) {
        return new WebDriverWait(driver, getConfiguration().getWaitAjaxInterval());
    }

    public static WebDriverWait waitGui() {
        return waitGui(GrapheneContext.getProxy());
    }

    public static WebDriverWait waitGui(WebDriver driver) {
        return new WebDriverWait(driver, getConfiguration().getWaitGuiInterval());
    }

    public static WebDriverWait waitModel() {
        return waitModel(GrapheneContext.getProxy());
    }

    public static WebDriverWait waitModel(WebDriver driver) {
        return new WebDriverWait(driver, getConfiguration().getWaitModelInterval());
    }

    private static GrapheneConfiguration getConfiguration() {
        return GrapheneConfigurationContext.getProxy();
    }

}
