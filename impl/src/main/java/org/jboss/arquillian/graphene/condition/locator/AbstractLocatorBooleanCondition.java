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
package org.jboss.arquillian.graphene.condition.locator;

import java.util.logging.Logger;

import org.jboss.arquillian.core.spi.Validate;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractLocatorBooleanCondition implements ExpectedCondition<Boolean> {

    protected static final Logger LOGGER = Logger.getLogger(AbstractLocatorBooleanCondition.class.getName());
    private final boolean negation;
    private final By locator;

    public AbstractLocatorBooleanCondition(By locator) {
        this(locator, false);
    }

    public AbstractLocatorBooleanCondition(By locator, boolean negation) {
        Validate.notNull(locator, "The locator can't be null.");
        this.locator = locator;
        this.negation = negation;
    }

    @Override
    public Boolean apply(WebDriver driver) {
        if (negation) {
            return !check(driver);
        } else {
            return check(driver);
        }
    }

    protected boolean getNegation() {
        return negation;
    }

    protected By getLocator() {
        return locator;
    }

    protected abstract boolean check(WebDriver driver);
}
