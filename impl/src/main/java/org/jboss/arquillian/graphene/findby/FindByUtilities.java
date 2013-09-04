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

import java.lang.reflect.Field;
import java.util.List;

import org.jboss.arquillian.graphene.enricher.ReflectionHelper;
import org.jboss.arquillian.graphene.spi.findby.ImplementsLocationStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;
import org.openqa.selenium.support.How;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class FindByUtilities {

    public static By getCorrectBy(Field field, How defaultElementLocatingStrategy) {
        Annotations annotations = new Annotations(field, defaultElementLocatingStrategy);
        By by = annotations.buildBy();

        return by;
    }

    public static List<Field> getListOfFieldsAnnotatedWithFindBys(Object target) {
        List<Field> fields = ReflectionHelper.getFieldsWithAnnotation(target.getClass(), FindBy.class);
        fields.addAll(ReflectionHelper.getFieldsWithAnnotation(target.getClass(), FindBys.class));
        fields.addAll(ReflectionHelper.getFieldsWithAnnotatedAnnotation(target.getClass(), ImplementsLocationStrategy.class));
        return fields;
    }
}
