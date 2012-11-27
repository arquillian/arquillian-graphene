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
package org.jboss.arquillian.graphene.condition.locator;

import org.jboss.arquillian.graphene.condition.AbstractBooleanConditionFactory;
import org.jboss.arquillian.graphene.condition.BooleanConditionWrapper;
import org.jboss.arquillian.graphene.condition.StringConditionFactory;
import org.jboss.logging.Logger;
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
public class LocatorElementTextConditionFactory extends AbstractBooleanConditionFactory<StringConditionFactory> implements StringConditionFactory<StringConditionFactory> {

    private final By locator;

    protected static final Logger LOGGER = Logger.getLogger(LocatorElementTextConditionFactory.class);

    public LocatorElementTextConditionFactory(By locator) {
        this.locator = locator;
    }

    @Override
    protected StringConditionFactory copy() {
        return new LocatorElementTextConditionFactory(locator);
    }

    @Override
    public ExpectedCondition<Boolean> contains(String expected) {
        if (expected == null) {
            throw new IllegalArgumentException("The expected string is null.");
        }
        return new BooleanConditionWrapper(ExpectedConditions.textToBePresentInElement(locator, expected), getNegation());
    }

    @Override
    public ExpectedCondition<Boolean> equalTo(final String expected) {
        if (expected == null) {
            throw new IllegalArgumentException("The expected string is null.");
        }
        return new BooleanConditionWrapper(new ExpectedCondition<Boolean>() {
            @Override
            public Boolean apply(WebDriver driver) {
                String elementText = findElement(locator, driver).getText();
                return expected.equals(elementText);
            }
        }, getNegation());
    }

    private static WebElement findElement(By by, WebDriver driver) {
        try {
            return driver.findElement(by);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (WebDriverException e) {
            LOGGER.debug(String.format("WebDriverException thrown by findElement(%s)", by), e);
            throw e;
        }
    }
}
