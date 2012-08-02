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
package org.jboss.arquillian.graphene.condition;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ElementConditionFactory {

    private WebElement element;
    private boolean negation = false;

    public ElementConditionFactory(WebElement element) {
        if (element == null) {
            throw new IllegalArgumentException("The element can't be null.");
        }
        this.element = element;
    }

    public ExpectedCondition<Boolean> isDisplayed() {
        return new ElementIsDisplayed(element, negation);
    }

    public ExpectedCondition<Boolean> isPresent() {
        return new ElementIsPresent(element, negation);
    }

    public ExpectedCondition<Boolean> textContains(String expected) {
        return new ElementTextContains(element, expected, negation);
    }

    public ExpectedCondition<Boolean> textEquals(String expected) {
        return new ElementTextEquals(element, expected, negation);
    }

    public ElementConditionFactory not() {
        ElementConditionFactory copy = copy();
        copy.negation = true;
        return copy;
    }

    protected ElementConditionFactory copy() {
        ElementConditionFactory copy = new ElementConditionFactory(element);
        copy.negation = this.negation;
        return copy;
    }
}
