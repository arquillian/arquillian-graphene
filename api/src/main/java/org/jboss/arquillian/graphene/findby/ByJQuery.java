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
package org.jboss.arquillian.graphene.findby;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;

import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.graphene.spi.findby.LocationStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public class ByJQuery extends By {

    private static final String IMPLEMENTATION_CLASS = "org.jboss.arquillian.graphene.findby.ByJQueryImpl";

    private By implementation;

    public ByJQuery(String selector) {
        Validate.notNull(selector, "Cannot find elements when selector is null!");
        this.implementation = instantiate(selector);
    }

    public static ByJQuery selector(String selector) {
        return new ByJQuery(selector);
    }

    @Override
    public WebElement findElement(SearchContext context) {
        return implementation.findElement(context);
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return implementation.findElements(context);
    }

    @Override
    public String toString() {
        return implementation.toString();
    }

    private static By instantiate(String selector) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends By> clazz = (Class<? extends By>) Class.forName(IMPLEMENTATION_CLASS);

            Constructor<? extends By> constructor = clazz.getConstructor(String.class);

            return constructor.newInstance(selector);

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find class " + IMPLEMENTATION_CLASS
                    + ", make sure you have arquillian-graphene-impl.jar included on the classpath.", e);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static class JQueryLocationStrategy implements LocationStrategy {

        @Override
        public ByJQuery fromAnnotation(Annotation annotation) {
            FindByJQuery findBy = (FindByJQuery) annotation;
            return new ByJQuery(findBy.value());
        }
    }
}
