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
package org.jboss.arquillian.graphene.enricher.annotation;

import java.lang.reflect.Field;
import java.util.List;

import org.jboss.arquillian.graphene.enricher.ReflectionHelper;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class FindByUtilities {

    public static By getCorrectBy(Field field) {
        if (field == null) {
            throw new IllegalArgumentException("Parameter field cannot be null!");
        }
        By by = null;
        if (field.getAnnotation(FindBy.class) != null) {
            by = getReferencedBy(field.getAnnotation(FindBy.class));
        } else {
            by = getReferencedBy(field.getAnnotation(org.jboss.arquillian.graphene.spi.annotations.FindBy.class));
        }

        return by;
    }
    
    public static List<Field> getListOfFieldsAnnotatedWithFindBys(Object target) {
        List<Field> fields = ReflectionHelper.getFieldsWithAnnotation(target.getClass(), FindBy.class);
        fields.addAll(ReflectionHelper.getFieldsWithAnnotation(target.getClass(), org.jboss.arquillian.graphene.spi.annotations.FindBy.class));
        return fields;
    }

    private static final By getReferencedBy(FindBy findByAnnotation) {
        String value = findByAnnotation.className().trim();
        if (!value.isEmpty()) {
            return By.className(value);
        }

        value = findByAnnotation.css().trim();
        if (!value.isEmpty()) {
            return By.cssSelector(value);
        }

        value = findByAnnotation.id().trim();
        if (!value.isEmpty()) {
            return By.id(value);
        }

        value = findByAnnotation.xpath().trim();
        if (!value.isEmpty()) {
            return By.xpath(value);
        }

        value = findByAnnotation.name().trim();
        if (!value.isEmpty()) {
            return By.name(value);
        }

        value = findByAnnotation.tagName().trim();
        if (!value.isEmpty()) {
            return By.tagName(value);
        }

        value = findByAnnotation.linkText().trim();
        if (!value.isEmpty()) {
            return By.linkText(value);
        }

        value = findByAnnotation.partialLinkText().trim();
        if (!value.isEmpty()) {
            return By.partialLinkText(value);
        }

        return null;
    }

    private static final By getReferencedBy(org.jboss.arquillian.graphene.spi.annotations.FindBy findByAnnotation) {
        String value = findByAnnotation.className().trim();
        if (!value.isEmpty()) {
            return By.className(value);
        }

        value = findByAnnotation.css().trim();
        if (!value.isEmpty()) {
            return By.cssSelector(value);
        }

        value = findByAnnotation.id().trim();
        if (!value.isEmpty()) {
            return By.id(value);
        }

        value = findByAnnotation.xpath().trim();
        if (!value.isEmpty()) {
            return By.xpath(value);
        }

        value = findByAnnotation.name().trim();
        if (!value.isEmpty()) {
            return By.name(value);
        }

        value = findByAnnotation.tagName().trim();
        if (!value.isEmpty()) {
            return By.tagName(value);
        }

        value = findByAnnotation.linkText().trim();
        if (!value.isEmpty()) {
            return By.linkText(value);
        }

        value = findByAnnotation.jquery();
        if (!value.isEmpty()) {
            return ByJQuery.jquerySelector(value);
        }

        return null;
    }

}
