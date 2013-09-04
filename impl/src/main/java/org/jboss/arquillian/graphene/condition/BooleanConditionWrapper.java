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
package org.jboss.arquillian.graphene.condition;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class BooleanConditionWrapper implements ExpectedCondition<Boolean> {

    private final ExpectedCondition<?> wrapped;
    private final boolean negation;
    private final Set<Class<? extends RuntimeException>> ignoredExceptions = new HashSet<Class<? extends RuntimeException>>();

    protected static final Logger LOGGER = Logger.getLogger(BooleanConditionWrapper.class.getName());

    public BooleanConditionWrapper(ExpectedCondition<?> wrapped, Class<? extends RuntimeException>... ignoredExceptions) {
        this(wrapped, false, ignoredExceptions);
    }

    public BooleanConditionWrapper(ExpectedCondition<?> wrapped, boolean negation, Class<? extends RuntimeException>... ignoredExceptions) {
        if (wrapped == null) {
            throw new IllegalArgumentException("The wrapped is null.");
        }
        this.wrapped = wrapped;
        this.negation = negation;
        this.ignoredExceptions.addAll(Arrays.asList(ignoredExceptions));
    }

    @Override
    public Boolean apply(WebDriver driver) {
        try {
            Object original = wrapped.apply(driver);
            if (original instanceof Boolean) {
                if (negation) {
                    return !((Boolean) original).booleanValue();
                } else {
                    return (Boolean) original;
                }
            } else {
                if (negation) {
                    return original == null;
                } else {
                    return original != null;
                }
            }
        } catch(StaleElementReferenceException ignored) {
            LOGGER.log(Level.FINE, "The element is stale.", ignored);
            return false;
        } catch(RuntimeException e) {
            if (ignoredExceptions.contains(e.getClass())) {
                LOGGER.log(Level.FINE, "Exception ignored, returning " + negation + ".", e);
                return negation;
            } else {
                throw e;
            }
        }
    }

    @Override
    public String toString() {
        return (negation ? "negated: " : "wrapped: ") + wrapped.toString();
    }

}
