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

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ElementTextEquals extends AbstractElementAndTextBooleanCondition {

    public ElementTextEquals(WebElement element, String text) {
        super(element, text);
    }

    public ElementTextEquals(WebElement element, String text, boolean negation) {
        super(element, text, negation);
    }

    @Override
    protected Boolean check(WebDriver driver) {
        return getElement().getText().equals(getText());
    }

    @Override
    public String toString() {
        return String.format("text ('%s')%s to be equal to text ('%s') in element %s",
                getText(),
                (getNegation() ? " not" : ""),
                getElement().getText(),
                getElement().toString());
    }
}
