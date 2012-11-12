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
package org.jboss.arquillian.graphene.enricher;

import java.lang.reflect.Field;
import java.util.List;
import org.jboss.arquillian.graphene.enricher.exception.GrapheneTestEnricherException;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class WebElementEnricher extends AbstractWebElementEnricher {

    @Override
    public void enrich(SearchContext searchContext, Object target) {
        try {
            List<Field> fields = ReflectionHelper.getFieldsWithAnnotation(target.getClass(), FindBy.class);
            for (Field field : fields) {
                By by = getReferencedBy(field.getAnnotation(FindBy.class));
                String message = "Your @FindBy annotation over field " + NEW_LINE + field.getClass()
                        + NEW_LINE + " declared in: " + NEW_LINE + field.getDeclaringClass().getName() + NEW_LINE
                        + " is annotated with empty @FindBy annotation, in other words it "
                        + "should contain parameter which will define the strategy for referencing that element.";
                // WebElement
                if (field.getType().isAssignableFrom(WebElement.class)) {
                    if (by == null) {
                        throw new GrapheneTestEnricherException(message);
                    }
                    WebElement element = createWebElement(
                            by,
                            searchContext);
                    setValue(field, target, element);
                // List<WebElement>
                } else if (field.getType().isAssignableFrom(List.class) && getListType(field).isAssignableFrom(WebElement.class)) {
                    if (by == null) {
                        throw new GrapheneTestEnricherException(message);
                    }
                    List<WebElement> elements = createWebElements(
                            by,
                            searchContext);
                    setValue(field, target, elements);
                }
            }
        } catch(ClassNotFoundException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

}
