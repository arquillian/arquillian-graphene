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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AttributeIsPresent extends AbstractAttributeBooleanCondition {

    public AttributeIsPresent(WebElement element, String attribute) {
        this(element, attribute, false);
    }

    public AttributeIsPresent(WebElement element, String attribute, boolean negation) {
        super(element, attribute, negation);
    }

    @Override
    public Boolean check(WebDriver driver) {
        String attributeValue = getElement().getAttribute(getAttribute());
        return attributeValue != null && attributeValue.trim().length() > 0;
    }

    @Override
    public String toString() {
        return String.format("attribute ('%s')%s to be present in element %s", getAttribute(), getNegation() ? " not" : "",
                getElement().toString());
    }

}
