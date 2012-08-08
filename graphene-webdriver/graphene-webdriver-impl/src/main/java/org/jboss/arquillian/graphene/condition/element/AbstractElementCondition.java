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
package org.jboss.arquillian.graphene.condition.element;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.jboss.logging.Logger;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public abstract class AbstractElementCondition<R> implements ExpectedCondition<R> {

    private WebElement element;
    protected static final Logger LOGGER = Logger.getLogger(AbstractElementCondition.class);

    public AbstractElementCondition(WebElement element) {
        if (element == null) {
            throw new IllegalArgumentException("The element can't be null.");
        }
        this.element = element;
    }

    @Override
    public R apply(WebDriver driver) {
        try {
            return check(driver);
        } catch(StaleElementReferenceException ignored) {
            LOGGER.debug("The element is stale.", ignored);
            return null;
        }
    }

    protected WebElement getElement() {
        return element;
    }

    abstract protected R check(WebDriver driver);

}
