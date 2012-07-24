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
import org.jboss.arquillian.graphene.context.GrapheneConfigurationContext;
import org.jboss.arquillian.graphene.wait.WebDriverWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 *
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class Graphene {

    public static AttributeConditionFactory attribute(WebElement element, String attribute) {
        return new AttributeConditionFactory(element, attribute);
    }

    public static ElementConditionFactory element(WebElement element) {
        return new ElementConditionFactory(element);
    }

    public static WebDriverWait waitAjax() {
        return waitAjax(GrapheneContext.getProxy());
    }

    public static WebDriverWait waitAjax(WebDriver driver) {
        return new WebDriverWait(driver, GrapheneConfigurationContext.getProxy().getWaitAjaxInterval());
    }

    public static WebDriverWait waitGui() {
        return waitGui(GrapheneContext.getProxy());
    }

    public static WebDriverWait waitGui(WebDriver driver) {
        return new WebDriverWait(driver, GrapheneConfigurationContext.getProxy().getWaitGuiInterval());
    }

    public static WebDriverWait waitModel() {
        return waitModel(GrapheneContext.getProxy());
    }

    public static WebDriverWait waitModel(WebDriver driver) {
        return new WebDriverWait(driver, GrapheneConfigurationContext.getProxy().getWaitModelInterval());
    }

}
