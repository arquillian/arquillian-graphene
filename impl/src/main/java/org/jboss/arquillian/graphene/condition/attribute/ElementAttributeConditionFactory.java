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
package org.jboss.arquillian.graphene.condition.attribute;

import org.jboss.arquillian.graphene.condition.AbstractBooleanConditionFactory;
import org.jboss.arquillian.graphene.condition.AttributeConditionFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ElementAttributeConditionFactory extends AbstractBooleanConditionFactory<AttributeConditionFactory> implements AttributeConditionFactory {

    private WebElement element;
    private String attribute;

    public ElementAttributeConditionFactory(WebElement element, String attribute) {
        if (element == null) {
            throw new IllegalArgumentException("The element can't be null.");
        }
        if (attribute == null) {
            throw new IllegalArgumentException("The attribute can't be null.");
        }
        this.element = element;
        this.attribute = attribute;
    }

    @Override
    public ExpectedCondition<Boolean> isPresent() {
        return new AttributeIsPresent(element, attribute, getNegation());
    }

    @Override
    protected ElementAttributeConditionFactory copy() {
        ElementAttributeConditionFactory copy = new ElementAttributeConditionFactory(element, attribute);
        copy.setNegation(getNegation());
        return copy;
    }

    @Override
    public ExpectedCondition<Boolean> contains(String expected) {
        return new AttributeValueContains(element, attribute, expected, getNegation());
    }

    @Override
    public ExpectedCondition<Boolean> equalTo(String expected) {
        return new AttributeValueEquals(element, attribute, expected, getNegation());
    }

}
