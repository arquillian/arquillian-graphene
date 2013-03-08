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
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class WebElementWrapperEnricher extends AbstractSearchContextEnricher {

    @Override
    public void enrich(final SearchContext searchContext, Object target) {
        List<Field> fields = FindByUtilities.getListOfFieldsAnnotatedWithFindBys(target);
        for (Field field: fields) {
            final Field finalField = field;
            if (isValidClass(field.getType())) {
                final By rootBy = FindByUtilities.getCorrectBy(field);
                try {
                    Object component = GrapheneProxy.getProxyForFutureTarget(new GrapheneProxy.FutureTarget() {
                        @Override
                        public Object getTarget() {
                            try {
                                return instantiate(finalField.getType(), WebElementUtils.findElementLazily(rootBy, searchContext));
                            } catch (Exception e) {
                                throw new IllegalStateException("Can't instantiate the " + finalField.getType());
                            }
                        }
                    }, field.getType());


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
