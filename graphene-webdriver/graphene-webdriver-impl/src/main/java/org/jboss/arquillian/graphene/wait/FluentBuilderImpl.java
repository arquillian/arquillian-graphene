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
package org.jboss.arquillian.graphene.wait;

import org.jboss.arquillian.graphene.condition.element.WebElementConditionFactory;
import org.jboss.arquillian.graphene.condition.locator.ElementLocatorConditionFactory;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.fluent.FluentBase;
import org.jboss.arquillian.graphene.wait.ElementBuilder;
import org.jboss.arquillian.graphene.wait.FluentBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class FluentBuilderImpl<FLUENT> implements FluentBuilder<FLUENT> {

    private final FluentBase<FLUENT> fluentBase;

    public FluentBuilderImpl(FluentBase<FLUENT> fluentBase) {
        this.fluentBase = fluentBase;
    }

    @Override
    public ElementBuilder<FLUENT> element(WebElement element) {
        return new ElementBuilderImpl<FLUENT>(new WebElementConditionFactory(element), fluentBase);
    }

    @Override
    public ElementBuilder<FLUENT> element(By element) {
        return new ElementBuilderImpl<FLUENT>(new ElementLocatorConditionFactory(element), fluentBase);
    }

    @Override
    public ElementBuilder<FLUENT> element(SearchContext searchContext, By element) {
        return new ElementBuilderImpl<FLUENT>(new ElementLocatorConditionFactory(searchContext, element), fluentBase);
    }

}
