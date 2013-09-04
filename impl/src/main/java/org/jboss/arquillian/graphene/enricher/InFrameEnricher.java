package org.jboss.arquillian.graphene.enricher;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.arquillian.graphene.enricher.exception.GrapheneTestEnricherException;
import org.jboss.arquillian.graphene.enricher.findby.FindBy;
import org.jboss.arquillian.graphene.page.InFrame;
import org.jboss.arquillian.graphene.page.Page;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class InFrameEnricher extends AbstractSearchContextEnricher {

    private final Set<Class<?>> DO_NOT_ENRICH_FURTHER = new HashSet<Class<?>>(Arrays.asList(new Class<?>[] { WebElement.class,
            Select.class, List.class, WebDriver.class }));

    @Override
    public void enrich(SearchContext searchContext, Object objectToEnrich) {
        Collection<Field> inFrameFields = ReflectionHelper.getFieldsWithAnnotation(objectToEnrich.getClass(), InFrame.class);
        for (Field field : inFrameFields) {
            boolean isAccessible = field.isAccessible();
            if (!isAccessible) {
                field.setAccessible(true);
            }
            InFrame inFrame = field.getAnnotation(InFrame.class);
            int index = inFrame.index();
            String nameOrId = inFrame.nameOrId();
            checkInFrameParameters(field, index, nameOrId);

            try {
                registerInFrameInterceptor(objectToEnrich, field, index, nameOrId);
            } catch (IllegalArgumentException e) {
                throw new GrapheneTestEnricherException(
                    "Only org.openqa.selenium.WebElement, Page fragments fields and Page Object fields can be annotated with @InFrame. Check the field: "
                        + field + " declared in the class: " + objectToEnrich.getClass(), e);
            } catch (Exception e) {
                throw new GrapheneTestEnricherException(e);
            }
            if (!isAccessible) {
                field.setAccessible(false);
            }
        }
    }

    private void registerInFrameInterceptor(Object objectToEnrich, Field field, int index, String nameOrId)
        throws IllegalAccessException, ClassNotFoundException {
        GrapheneProxyInstance proxy = (GrapheneProxyInstance) field.get(objectToEnrich);

        Class<?> fieldType = field.getType();
        if (index != -1) {
            proxy.registerInterceptor(new InFrameInterceptor(index));
        } else {
            proxy.registerInterceptor(new InFrameInterceptor(nameOrId));
        }
        if (!DO_NOT_ENRICH_FURTHER.contains(fieldType)) {
            enrichRecursivelyGrapheneProxyInstances(proxy, nameOrId, index, field);
            enrichRecursivelyGrapheneProxyInstances(proxy.unwrap(), nameOrId, index, field);
        }
    }

    private void enrichRecursivelyGrapheneProxyInstances(Object objectToEnrich, String nameOrId, int index, Field field)
        throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException {
        enrichFurtherSpecificAnnotation(objectToEnrich, nameOrId, index, FindBy.class, field);
        enrichFurtherSpecificAnnotation(objectToEnrich, nameOrId, index, org.openqa.selenium.support.FindBy.class, field);
        enrichFurtherSpecificAnnotation(objectToEnrich, nameOrId, index, Page.class, field);
    }

    private void enrichFurtherSpecificAnnotation(Object objectToEnrich, String nameOrId, int index,
        final Class<? extends Annotation> annotationClass, Field field) throws ClassNotFoundException,
        IllegalArgumentException, IllegalAccessException {
        Class<?> fieldType = field.getType();
        List<Field> fieldsToEnrich = ReflectionHelper.getFieldsWithAnnotation(fieldType, annotationClass);
        for (Field fieldToEnrich : fieldsToEnrich) {
            boolean isAccessible = field.isAccessible();
            if (!isAccessible) {
                fieldToEnrich.setAccessible(true);
            }
            registerInFrameInterceptor(objectToEnrich, fieldToEnrich, index, nameOrId);
            if (!isAccessible) {
                field.setAccessible(false);
            }
        }
    }

    private void checkInFrameParameters(Field field, int index, String nameOrId) {
        if ((nameOrId.trim().equals("") && index < 0)) {
            throw new GrapheneTestEnricherException(
                "You have to provide either non empty nameOrId or non negative index value of the frame/iframe in the @InFrame. Check field "
                    + field + " declared in: " + field.getDeclaringClass());
        }
    }

    @Override
    public int getPrecedence() {
        return 0;
    }
}
