/**
 * JBoss, Home of Professional Open Source Copyright 2012, Red Hat, Inc. and
 * individual contributors by the
 *
 * @authors tag. See the copyright.txt in the distribution for a full listing of
 * individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA, or see the FSF
 * site: http://www.fsf.org.
 */
package org.jboss.arquillian.graphene.condition.locator;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.arquillian.graphene.condition.AbstractBooleanConditionFactory;
import org.jboss.arquillian.graphene.condition.AttributeConditionFactory;
import org.jboss.arquillian.graphene.condition.BooleanConditionWrapper;
import org.jboss.arquillian.graphene.condition.ElementConditionFactory;
import org.jboss.arquillian.graphene.condition.StringConditionFactory;
import org.jboss.arquillian.graphene.condition.attribute.LocatorAttributeConditionFactory;
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
public class ElementLocatorConditionFactory extends AbstractBooleanConditionFactory<ElementConditionFactory> implements ElementConditionFactory {

    private final By locator;
    private final SearchContext searchContext;
    protected static final Logger LOGGER = Logger.getLogger(ElementLocatorConditionFactory.class.getName());

    public ElementLocatorConditionFactory(By locator) {
        if (locator == null) {
            throw new IllegalArgumentException("The locator can't be null.");
        }
        this.locator = locator;
        this.searchContext = null;
    }

    public ElementLocatorConditionFactory(SearchContext searchContext, By locator) {
        if (searchContext == null) {
            throw new IllegalArgumentException("The search context can't be null.");
        }
        if (locator == null) {
            throw new IllegalArgumentException("The locator can't be null.");
        }
        this.locator = locator;
        this.searchContext = searchContext;
    }

    @Override
    public ExpectedCondition<Boolean> isVisible() {
        return new BooleanConditionWrapper(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return findElement(locator, driver).isDisplayed();
            }

            @Override
            public String toString() {
                return String.format("element %s to be visible",
                    locator);
            }

        }, getNegation());
    }

    @Override
    public ExpectedCondition<Boolean> isPresent() {
        return new BooleanConditionWrapper(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    findElement(locator, driver);
                    return true;
                } catch(NoSuchElementException ignored) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("element %s to be present",
                    locator);
            }

        }, getNegation());
    }

    @Override
    public ExpectedCondition<Boolean> isSelected() {
        return new BooleanConditionWrapper(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return findElement(locator, driver).isSelected();
            }

            @Override
            public String toString() {
                return String.format("element %s to be selected",
                    locator);
            }

        }, getNegation());
    }

    @Override
    public ExpectedCondition<Boolean> isEnabled() {
        return new BooleanConditionWrapper(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                return findElement(locator, driver).isEnabled();
            }

            @Override
            public String toString() {
                return String.format("element %s to be enabled",
                    locator);
            }

        }, getNegation());
    }

    @Override
    public StringConditionFactory text() {
        return getNegation() ? new LocatorElementTextConditionFactory(searchContext, locator).not() : new LocatorElementTextConditionFactory(searchContext, locator);
    }

    @Override
    public ExpectedCondition<Boolean> textContains(String expected) {
        return text().contains(expected);
    }

    @Override
    public ExpectedCondition<Boolean> textEquals(final String expected) {
        return text().equalTo(expected);
    }

    @Override
    protected ElementConditionFactory copy() {
        ElementLocatorConditionFactory copy;
        if (searchContext != null) {
            copy = new ElementLocatorConditionFactory(searchContext, locator);
        } else {
            copy = new ElementLocatorConditionFactory(locator);
        }
        copy.setNegation(getNegation());
        return copy;

    }

    @Override
    public AttributeConditionFactory attribute(String attribute) {
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
