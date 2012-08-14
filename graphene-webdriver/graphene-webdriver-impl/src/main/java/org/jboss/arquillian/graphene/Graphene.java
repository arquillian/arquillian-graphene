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

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.condition.AttributeConditionFactory;
import org.jboss.arquillian.graphene.condition.ElementConditionFactory;
import org.jboss.arquillian.graphene.condition.attribute.AttributeConditionFactoryImpl;
import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.condition.locator.ElementLocatorConditionFactory;
import org.jboss.arquillian.graphene.context.GrapheneConfigurationContext;
import org.jboss.arquillian.graphene.wait.ExtendedWebDriverWait;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    public static ExtendedWebDriverWait waitAjax() {
        return waitAjax(GrapheneContext.getProxy());
    }

    public static ExtendedWebDriverWait waitAjax(WebDriver driver) {
        return new ExtendedWebDriverWait(driver, GrapheneConfigurationContext.getProxy().getWaitAjaxInterval());
    }

    public static ExtendedWebDriverWait waitGui() {
        return waitGui(GrapheneContext.getProxy());
    }

    public static ExtendedWebDriverWait waitGui(WebDriver driver) {
        return new ExtendedWebDriverWait(driver, GrapheneConfigurationContext.getProxy().getWaitGuiInterval());
    }

    public static ExtendedWebDriverWait waitModel() {
        return waitModel(GrapheneContext.getProxy());
    }

    public static ExtendedWebDriverWait waitModel(WebDriver driver) {
        return new ExtendedWebDriverWait(driver, GrapheneConfigurationContext.getProxy().getWaitModelInterval());
    }

}
