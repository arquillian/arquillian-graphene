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
package org.jboss.arquillian.graphene.condition.element;

import org.jboss.arquillian.graphene.condition.AbstractBooleanConditionFactory;
import org.jboss.arquillian.graphene.condition.AttributeConditionFactory;
import org.jboss.arquillian.graphene.condition.BooleanConditionWrapper;
import org.jboss.arquillian.graphene.condition.ElementConditionFactory;
import org.jboss.arquillian.graphene.condition.StringConditionFactory;
import org.jboss.arquillian.graphene.condition.attribute.ElementAttributeConditionFactory;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class WebElementConditionFactory extends AbstractBooleanConditionFactory<ElementConditionFactory> implements ElementConditionFactory{

    private WebElement element;

    public WebElementConditionFactory(WebElement element) {
        if (element == null) {
            throw new IllegalArgumentException("The element can't be null.");
        }
        this.element = element;
    }

    @Override
    public AttributeConditionFactory attribute(String attribute) {
        return new ElementAttributeConditionFactory(element, attribute);
    }

    @Override
    public ExpectedCondition<Boolean> isPresent() {
        return new ElementIsPresent(element, getNegation());
    }

    @Override
    public ExpectedCondition<Boolean> isSelected() {
        return ExpectedConditions.elementSelectionStateToBe(element, !getNegation());
    }

    @Override
    public ExpectedCondition<Boolean> isVisible() {
        return new BooleanConditionWrapper(ExpectedConditions.visibilityOf(element), getNegation(), NoSuchElementException.class);
    }

    @Override
    public ExpectedCondition<Boolean> isClickable() {
        return new BooleanConditionWrapper(ExpectedConditions.elementToBeClickable(element), getNegation(), NoSuchElementException.class);
    }

    @Override
    public StringConditionFactory text() {
        return new WebElementTextConditionFactory(element, getNegation());
    }

    @Override
    protected WebElementConditionFactory copy() {
        WebElementConditionFactory copy = new WebElementConditionFactory(element);
        copy.setNegation(getNegation());
        return copy;
    }

    @Override
    public ExpectedCondition<Boolean> isEnabled() {
        return new ElementIsEnabled(element, getNegation());
    }

}