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

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.graphene.condition.AbstractBooleanConditionFactory;
import org.jboss.arquillian.graphene.condition.AttributeConditionFactory;
import org.jboss.arquillian.graphene.condition.element.AbstractElementBooleanCondition;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:jstefek@redhat.com">Jiri Stefek</a>
 */
public class LocatorAttributeConditionFactory extends AbstractBooleanConditionFactory<AttributeConditionFactory> implements AttributeConditionFactory {

    private final SearchContext searchContext;
    private final By locator;
    private final String attribute;

    protected static final Logger LOGGER = Logger.getLogger(LocatorAttributeConditionFactory.class.getName());

    public LocatorAttributeConditionFactory(SearchContext searchContext, By locator, String attribute) {
        this.locator = locator;
        this.attribute = attribute;
        this.searchContext = searchContext;
    }

    @Override
    public ExpectedCondition<Boolean> isPresent() {
        return new BooleanCondition() {
            @Override
            protected AbstractElementBooleanCondition initCondition(WebElement element) {
                return new AttributeIsPresent(element, attribute, getNegation());
            }
        };
    }

    @Override
    public ExpectedCondition<Boolean> contains(final String expected) {
        return new BooleanCondition() {
            @Override
            protected AbstractElementBooleanCondition initCondition(WebElement element) {
                return new AttributeValueContains(element, attribute, expected, getNegation());
            }
        };
    }

    @Override
    public ExpectedCondition<Boolean> equalTo(final String expected) {
        return new BooleanCondition() {
            @Override
            protected AbstractElementBooleanCondition initCondition(WebElement element) {
                return new AttributeValueEquals(element, attribute, expected, getNegation());
            }
        };
    }

    @Override
    public ExpectedCondition<Boolean> equalToIgnoreCase(final String expected) {
        return new BooleanCondition() {
            @Override
            protected AbstractElementBooleanCondition initCondition(WebElement element) {
                return new AttributeValueEqualToIgnoreCase(element, attribute, expected, getNegation());
            }
        };
    }

    @Override
    public ExpectedCondition<Boolean> matches(final String expected) {
        return new BooleanCondition() {
            @Override
            protected AbstractElementBooleanCondition initCondition(WebElement element) {
                return new AttributeValueEqualToIgnoreCase(element, attribute, expected, getNegation());
            }
        };
    }

    @Override
    protected AttributeConditionFactory copy() {
        return new LocatorAttributeConditionFactory(searchContext, locator, attribute);
    }

    protected WebElement findElement(By by, WebDriver driver) {
        try {
            return (searchContext == null ? driver : searchContext).findElement(by);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (WebDriverException e) {
            LOGGER.log(Level.FINE, String.format("WebDriverException thrown by findElement(%s)", by), e);
            throw e;
        }
    }

    private abstract class BooleanCondition implements ExpectedCondition<Boolean> {

        private AbstractElementBooleanCondition condition;

        @Override
        public Boolean apply(WebDriver driver) {
            condition = initCondition(findElement(locator, driver));
            return condition.apply(driver);
        }

        protected abstract AbstractElementBooleanCondition initCondition(WebElement element);

        @Override
        public String toString() {
            return condition.toString();
        }
    }
}
