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
import org.jboss.arquillian.graphene.condition.StringConditionFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class WebElementTextConditionFactory extends AbstractBooleanConditionFactory<StringConditionFactory> implements StringConditionFactory<StringConditionFactory> {

    private final WebElement element;

    public WebElementTextConditionFactory(WebElement element, boolean negation) {
        this.element = element;
        this.setNegation(negation);
    }

    @Override
    protected StringConditionFactory copy() {
        return new WebElementTextConditionFactory(element, getNegation());
    }

    @Override
    public ExpectedCondition<Boolean> contains(String expected) {
        return new ElementTextContains(element, expected, getNegation());
    }

    @Override
    public ExpectedCondition<Boolean> equalTo(String expected) {
        return new ElementTextEquals(element, expected, getNegation());
    }

}
