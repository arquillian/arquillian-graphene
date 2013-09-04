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
package org.jboss.arquillian.graphene.condition.attribute;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.jboss.arquillian.graphene.condition.AbstractBooleanConditionFactory;
import org.jboss.arquillian.graphene.condition.AttributeConditionFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
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
    public ExpectedCondition<Boolean> valueContains(final String expected) {
        return contains(expected);
    }

    @Override
    public ExpectedCondition<Boolean> valueEquals(String expected) {
        return equalTo(expected);
    }

    @Override
    public ExpectedCondition<Boolean> isPresent() {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return new AttributeIsPresent(findElement(locator, driver), attribute, getNegation()).apply(driver);
            }

            @Override
            public String toString() {
                return String.format("attribute ('%s')%s to be present in element %s",
                        attribute,
                        getNegation() ? " not" : "",
                        locator.toString());
            }
        };
    }

    @Override
    public ExpectedCondition<Boolean> contains(final String expected) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return new AttributeValueContains(findElement(locator, driver), attribute, expected, getNegation()).apply(driver);
            }

            @Override
            public String toString() {
                return String.format("text ('%s')%s to be contained by value of attribute ('%s') in element %s",
                    expected,
                    (getNegation() ? " not" : ""),
                    attribute,
                    locator.toString());
            }
        };
    }

    @Override
    public ExpectedCondition<Boolean> equalTo(final String expected) {
        return new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return new AttributeValueEquals(findElement(locator, driver), attribute, expected, getNegation()).apply(driver);
            }

            @Override
            public String toString() {
                return String.format("text ('%s')%s to be equal to value of attribute ('%s') in element %s",
                    expected,
                    (getNegation() ? " not" : ""),
                    attribute,
                    locator.toString());
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
}
