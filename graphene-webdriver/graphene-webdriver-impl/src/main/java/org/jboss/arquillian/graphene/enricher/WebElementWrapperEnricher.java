/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jboss.arquillian.graphene.enricher;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import org.jboss.arquillian.graphene.enricher.exception.GrapheneTestEnricherException;
import org.jboss.arquillian.graphene.enricher.findby.FindByUtilities;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class WebElementWrapperEnricher extends AbstractWebElementEnricher {

    @Override
    public void enrich(SearchContext searchContext, Object target) {
        List<Field> fields = FindByUtilities.getListOfFieldsAnnotatedWithFindBys(target);
        for (Field field: fields) {
            if (isValidClass(field.getType())) {
                By rootBy = FindByUtilities.getCorrectBy(field);
                try {
                    Object component = instantiate(field.getType(), createWebElement(rootBy, searchContext));
                    setValue(field, target, component);
                } catch (Exception e) {
                    throw new GrapheneTestEnricherException("Can't set a value to the " + target.getClass() + "." + field.getName() + ".", e);
                }
            }
        }
    }

    protected final boolean isValidClass(Class<?> clazz) {
        Class<?> outerClass = clazz.getDeclaringClass();
        if (outerClass == null || Modifier.isStatic(clazz.getModifiers())) {
            return ReflectionHelper.hasConstructor(clazz, WebElement.class);
        } else {
            return ReflectionHelper.hasConstructor(clazz, outerClass, WebElement.class);
        }
    }

}
