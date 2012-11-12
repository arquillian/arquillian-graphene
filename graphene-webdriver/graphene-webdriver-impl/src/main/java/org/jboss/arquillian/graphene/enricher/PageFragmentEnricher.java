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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.exception.PageFragmentInitializationException;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Enricher injecting page fragments ({@link FindBy} annotation is used) to the fields of the given object.
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class PageFragmentEnricher extends AbstractWebElementEnricher {

    @Inject
    private Instance<ServiceLoader> serviceLoader;

    @Override
    public void enrich(SearchContext searchContext, Object target) {
        List<Field> fields = ReflectionHelper.getFieldsWithAnnotation(target.getClass(), FindBy.class);
        for (Field field : fields) {
            // Page fragment
            if (isPageFragmentClass(field.getType())) {
                setupPageFragment(searchContext, target, field);
            // List<Page fragment>
            } else {
                try {
                    if (field.getType().isAssignableFrom(List.class) && isPageFragmentClass(getListType(field))) {
                        setupPageFragmentList(searchContext, target, field);
                    }
                } catch(ClassNotFoundException e) {
                    throw new PageFragmentInitializationException(e.getMessage(), e);
                }
            }
        }
    }

    protected final boolean isPageFragmentClass(Class<?> clazz) {
        return !clazz.isInterface() && !Modifier.isFinal(clazz.getModifiers()) && !Modifier.isInterface(clazz.getModifiers());
    }

    protected final <T> List<T> createPageFragmentList(final Class<T> clazz, final SearchContext searchContext, final By rootBy) {
        List<T> result = GrapheneProxy.getProxyForFutureTarget(new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                WebDriver driver = GrapheneContext.getProxy();
                List<WebElement> elements = searchContext.findElements(rootBy);
                List<T> fragments = new ArrayList<T>();
                for (int i = 0; i < elements.size(); i++) {
                    fragments.add(createPageFragment(clazz, createWebElement(rootBy, searchContext, i)));
                }
                return fragments;
            }

        }, List.class);
        return result;
    }

    protected final <T> T createPageFragment(Class<T> clazz, WebElement root) {
        try {
            T pageFragment =  instantiate(clazz);
            List<Field> roots = ReflectionHelper.getFieldsWithAnnotation(clazz, Root.class);
            if (roots.size() > 1) {
                throw new PageFragmentInitializationException("The Page Fragment " + NEW_LINE + pageFragment.getClass() + NEW_LINE
                        + " can not have more than one field annotated with Root annotation!" + "Your fields with @Root annotation: "
                        + roots + NEW_LINE);
            }
            if (roots.size() == 1) {
                setValue(roots.get(0), pageFragment, root);
            }
            enrichRecursively(root, pageFragment);
            return pageFragment;
        } catch (NoSuchMethodException ex) {
            throw new PageFragmentInitializationException(" Check whether declared Page Fragment has no argument constructor!",
                    ex);
        } catch (IllegalAccessException ex) {
            throw new PageFragmentInitializationException(
                    " Check whether declared Page Fragment has public no argument constructor!", ex);
        } catch (InstantiationException ex) {
            throw new PageFragmentInitializationException(
                    " Check whether you did not declare Page Fragment with abstract type!", ex);
        } catch (Exception ex) {
            throw new PageFragmentInitializationException(ex);
        }
    }

    protected final void setupPageFragmentList(SearchContext searchContext, Object target, Field field) throws ClassNotFoundException {
        By rootBy = getReferencedBy(field.getAnnotation(FindBy.class));
        if (rootBy == null) {
            throw new PageFragmentInitializationException(
                    "Your declaration of Page Fragment in test "+field.getDeclaringClass().getName()+" is annotated with @FindBy without any "
                    + "parameters, in other words without reference to root of the particular Page Fragment on the page!"
                    + NEW_LINE);
        }
        List<?> pageFragments = createPageFragmentList(getListType(field), searchContext, rootBy);
        setValue(field, target, pageFragments);
    }

    protected final void setupPageFragment(SearchContext searchContext, Object target, Field field) {
        By rootBy = getReferencedBy(field.getAnnotation(FindBy.class));
        if (rootBy == null) {
            throw new PageFragmentInitializationException(
                    "Your declaration of Page Fragment in test "+field.getDeclaringClass().getName()+" is annotated with @FindBy without any "
                    + "parameters, in other words without reference to root of the particular Page Fragment on the page!"
                    + NEW_LINE);
        }
        WebElement root = createWebElement(rootBy, searchContext);
        Object pageFragment = createPageFragment(field.getType(), root);
        setValue(field, target, pageFragment);
    }
}
