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
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class ElementTextMatches extends AbstractElementAndTextBooleanCondition {

    public ElementTextMatches(WebElement element, String text) {
        super(element, text);
    }

    public ElementTextMatches(WebElement element, String text, boolean negation) {
        super(element, text, negation);
    }

    @Override
    protected Boolean check(WebDriver driver) {
        return getElement().getText().matches(getText());
    }

    @Override
    public String toString() {
        return String.format("text ('%s') to%s match text ('%s') in element %s",
            getText(),
            (getNegation() ? " not" : ""),
            getElement().getText(),
            getElement().toString());
    }
}
