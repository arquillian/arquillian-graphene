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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class ElementLocatorConditionFactory extends AbstractBooleanConditionFactory<ElementConditionFactory> implements ElementConditionFactory {

    private final By locator;

    protected static final Logger LOGGER = Logger.getLogger(ElementLocatorConditionFactory.class.getName());

    public ElementLocatorConditionFactory(By locator) {
        if (locator == null) {
            throw new IllegalArgumentException("The locator can't be null.");
        }
        this.locator = locator;
    }

    @Override
    public ExpectedCondition<Boolean> isVisible() {
        return new BooleanConditionWrapper(ExpectedConditions.visibilityOfElementLocated(locator), getNegation(), NoSuchElementException.class);
    }

    @Override
    public ExpectedCondition<Boolean> isPresent() {
        return new BooleanConditionWrapper(ExpectedConditions.presenceOfElementLocated(locator), getNegation(), NoSuchElementException.class);
    }

    @Override
    public ExpectedCondition<Boolean> isSelected() {
        return ExpectedConditions.elementSelectionStateToBe(locator, !getNegation());
    }

    @Override
    public StringConditionFactory text() {
        return getNegation() ? new LocatorElementTextConditionFactory(locator).not() : new LocatorElementTextConditionFactory(locator);
    }

    @Override
    public ExpectedCondition<Boolean> textContains(String expected) {
        return text().contains(expected);
    }

    @Override
    public ExpectedCondition<Boolean> textEquals(final String expected) {
        if (expected == null) {
            throw new IllegalArgumentException("The expected string is null.");
        }
        return new BooleanConditionWrapper(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String elementText = findElement(locator, driver).getText();
                return expected.equals(elementText);
            }

            @Override
            public String toString() {
                return String.format("text ('%s') to be equal to text  in element found by %s", expected, locator);
            }

        }, getNegation());
    }

    @Override
    protected ElementConditionFactory copy() {
        ElementLocatorConditionFactory copy = new ElementLocatorConditionFactory(locator);
        copy.setNegation(getNegation());
        return copy;

    }

    @Override
    public AttributeConditionFactory attribute(String attribute) {
        return new LocatorAttributeConditionFactory(locator, attribute);
    }

    private static WebElement findElement(By by, WebDriver driver) {
        try {
            return driver.findElement(by);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (WebDriverException e) {
            LOGGER.log(Level.FINE, String.format("WebDriverException thrown by findElement(%s)", by), e);
            throw e;
        }
    }
}
