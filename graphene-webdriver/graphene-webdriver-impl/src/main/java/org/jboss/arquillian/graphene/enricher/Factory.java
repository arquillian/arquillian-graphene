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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;

import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.FindBy;

/**
 * Factory class for initializing the particular <b>Page Fragment</b>.
 * 
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * 
 */
public class Factory {

    /**
     * Returns initialized Page Fragment of given type. It means that all fields annotated with <code>@FindBy</code> and
     * <code>@Page</code> annotations are initialized properly.
     * 
     * @param clazz
     * @param <T> the implementation of Page Fragment
     * @param the root element to set to the initialized Page Fragment
     */
    public static <T> T initializePageFragment(Class<T> clazz, final WebElement root) {
        if (root == null || clazz == null) {
            throw new IllegalArgumentException("Non of the parameters can be null!");
        }

        T pageFragment = instantiatePageFragment(clazz);

        Field[] declaredFields = clazz.getDeclaredFields();

        for (Field i : declaredFields) {

            Annotation[] annotations = i.getAnnotations();
            for (Annotation j : annotations) {

                if (j instanceof Root) {
                    WebElement rootElement = GrapheneProxy.getProxyForTargetWithInterfaces(root, WebElement.class);
                    try {
                        boolean accessible = i.isAccessible();
                        if (!accessible) {
                            i.setAccessible(true);
                        }
                        i.set(pageFragment, rootElement);
                        if (!accessible) {
                            i.setAccessible(false);
                        }
                    } catch (Exception e) {
                        // TODO more detailed
                        throw new IllegalStateException("cannot set root element");
                    }
                }

                if (j instanceof FindBy) {

                    final By findBy = getReferencedBy((FindBy) j);

                    WebElement referencedElement = GrapheneProxy.getProxyForFutureTarget(new GrapheneProxy.FutureTarget() {
                        @Override
                        public Object getTarget() {
                            return root.findElement(findBy);
                        }
                    }, WebElement.class);

                    try {
                        boolean accessible = i.isAccessible();
                        if (!accessible) {
                            i.setAccessible(true);
                        }
                        i.set(pageFragment, referencedElement);
                        if (accessible) {
                            i.setAccessible(false);
                        }
                    } catch (Exception e) {
                        // TODO more detailed
                        throw new IllegalStateException("cannot set referenced element!");
                    }
                }
            }
        }

        return pageFragment;
    }

    public static <T> T instantiatePageFragment(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            // TODO: handle exception
            throw new IllegalArgumentException(e);
        }
    }

    /*
     * can I do it in better way ?to iterate over all annotations methods and invoke them on what ?obviously it is not possible
     * to invoke it on annotation, since it can not be instantiated
     */
    public static By getReferencedBy(FindBy findByAnnotation) {
        String value = null;

        value = findByAnnotation.className().trim();
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
}
