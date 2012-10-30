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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.enricher.exception.GrapheneTestEnricherException;
import org.jboss.arquillian.graphene.enricher.exception.PageFragmentInitializationException;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.pagefactory.ElementLocator;

/**
 * Factory class for initializing the particular <b>Page Fragment</b>.
 * 
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * 
 */
public class Factory {

    public static final String NEW_LINE = System.getProperty("line.separator");

    /**
     * Returns initialized Page Fragment of given type. It means that all fields annotated with <code>@FindBy</code> and
     * <code>@Page</code> annotations are initialized properly.
     * 
     * @param clazzOfPageFragment the class of concrete Page Fragment implementation which will be initialized
     * @param rootOfPageFragment the root of the Page Fragment to reference its parts from it
     * @return properly initialized page fragment
     */
    @SuppressWarnings("unchecked")
    public static <T> T initializePageFragment(Class<T> clazzOfPageFragment, final WebElement rootOfPageFragment) {
        if (rootOfPageFragment == null || clazzOfPageFragment == null) {
            throw new IllegalArgumentException("Non of the parameters can be null! Your parameters: " + clazzOfPageFragment
                + ", " + rootOfPageFragment);
        }

        T pageFragment = null;
        try {
            pageFragment = (T) instantiateClass(clazzOfPageFragment);
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

        List<Field> fields = ReflectionHelper.getFieldsWithAnnotation(clazzOfPageFragment, Root.class);
        int fieldSize = fields.size();
        if (fieldSize > 1) {
            throw new PageFragmentInitializationException("The Page Fragment " + NEW_LINE + pageFragment.getClass() + NEW_LINE
                + " can not have more than one field annotated with Root annotation!" + "Your fields with @Root annotation: "
                + fields + NEW_LINE);
        }
        if (fieldSize == 1) {
            setObjectToField(fields.get(0), pageFragment, rootOfPageFragment);
        }

        initFieldsAnnotatedByFindBy(pageFragment, rootOfPageFragment);

        return pageFragment;
    }

    /**
     * Initializes all fields annotated with <code>@FindBy</code> annotations.
     * 
     * @param object instance of class which <code>@FindBy<code> annotations will be initialized.
     * @param root the root element of the param <code>object<code> if exists (e.g. root of page fragment), <code>null</code>
     *        passed if it does not have root (e.g. initializing tests).
     */
    static void initFieldsAnnotatedByFindBy(Object object, WebElement root) throws PageFragmentInitializationException {

        // gets all fields with findBy annotations and then removes these
        // which are not Page Fragments
        List<Field> fields = ReflectionHelper.getFieldsWithAnnotation(object.getClass(), FindBy.class);

        List<Field> copy = new ArrayList<Field>();
        copy.addAll(fields);

        fields = removePlainFindBy(fields);
        initPageFragmentsFields(fields, object, root);

        // initialize other non Page Fragment fields annotated with FindBy
        copy.removeAll(fields);
        Factory.initNotPageFragmentsFieldsOrLists(copy, object, root);
    }

    /**
     * Initialize given class.
     * 
     * @param classToInstantiate to be initialized
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws SecurityException
     * @throws NoSuchMethodException
     */
    static Object instantiateClass(Class<?> classToInstantiate) throws NoSuchMethodException, SecurityException,
        InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class<?> outerClass = classToInstantiate.getDeclaringClass();
        Object instance = null;

        // check whether declared page object is not nested class
        if (outerClass != null && !Modifier.isStatic(classToInstantiate.getModifiers())) {
            Constructor<?> construtor = classToInstantiate.getDeclaredConstructor(new Class[] { outerClass });

            Object outerObject = instantiateClass(outerClass);

            instance = construtor.newInstance(new Object[] { outerObject });

        } else {
            instance = classToInstantiate.newInstance();
        }

        return instance;
    }

    /**
     * It removes all fields with type <code>WebElement</code> of <code>java.util.List</code>from the given list of fields.
     * 
     * @param findByFields
     * @return
     */
    private static List<Field> removePlainFindBy(List<Field> findByFields) {

        for (Iterator<Field> i = findByFields.iterator(); i.hasNext();) {

            Field field = i.next();

            Class<?> fieldType = field.getType();

            if (fieldType.equals(WebElement.class)) {
                i.remove();
            } else if (fieldType.equals(List.class)) {
                i.remove();
            }
        }

        return findByFields;
    }

    /**
     * Initialize page fragments fields.
     * 
     * @param fields all page fragments fields we want to initialize
     * @param objectToSetPageFragment an object where initialized page fragment will be set
     * @param root root element of the parent page fragment, if page fragment does have parent (it is not nested) then it can be
     *        <code>null</code>.
     */
    private static void initPageFragmentsFields(List<Field> fields, Object objectToSetPageFragment, WebElement root)
        throws PageFragmentInitializationException {
        for (Field pageFragmentField : fields) {

            Class<?> implementationClass = pageFragmentField.getType();

            String errorMsgBegin = "The Page Fragment: " + implementationClass + NEW_LINE + "declared in: " + NEW_LINE
                + objectToSetPageFragment.getClass() + NEW_LINE + "can not be initialized properly. The possible reason is: "
                + NEW_LINE;

            FindBy findBy = pageFragmentField.getAnnotation(FindBy.class);
            final By by = Factory.getReferencedBy(findBy);

            if (by == null) {
                throw new PageFragmentInitializationException(errorMsgBegin
                    + "Your declaration of Page Fragment in tests is annotated with @FindBy without any "
                    + "parameters, in other words without reference to root of the particular Page Fragment on the page!"
                    + NEW_LINE);
            }

            WebElement rootElement = Factory.setUpTheProxyForWebElement(by, root);

            Object pageFragment = null;
            try {
                pageFragment = initializePageFragment(implementationClass, rootElement);
            } catch (Exception ex) {
                throw new PageFragmentInitializationException(errorMsgBegin + ex.getMessage(), ex.getCause());
            }

            setObjectToField(pageFragmentField, objectToSetPageFragment, pageFragment);
        }
    }

    /**
     * If the given root is null, the driver proxy is used for finding injected elements, otherwise the root element is used.
     * 
     * @param fields
     * @param objectToSetNotPageFragmentsFields
     * @param root
     */
    public static void initNotPageFragmentsFieldsOrLists(List<Field> fields, Object objectToSetNotPageFragmentsFields,
        WebElement root) {

        for (Field i : fields) {

            FindBy findBy = i.getAnnotation(FindBy.class);
            final By by = Factory.getReferencedBy(findBy);

            if (by == null) {
                throw new GrapheneTestEnricherException("Your @FindBy annotation over field " + NEW_LINE + i.getClass()
                    + NEW_LINE + " declared in: " + NEW_LINE + objectToSetNotPageFragmentsFields.getClass() + NEW_LINE
                    + " is annotated with empty @FindBy annotation, in other words it "
                    + "should contain parameter which will define the strategy for referencing that element.");
            }

            Class<?> fieldType = i.getType();

            if (fieldType.equals(WebElement.class)) {
                // it is plain WebElement field
                WebElement element = setUpTheProxyForWebElement(by, root);
                setObjectToField(i, objectToSetNotPageFragmentsFields, element);

            } else if (fieldType.equals(List.class)) {
                try {
                    // it is List of WebElements
                    Class<?> listType = getListType(i);
                    if (listType.isAssignableFrom(WebElement.class)) {
                        List<WebElement> elements = setUpTheProxyForListOfWebElements(by, root);
                        setObjectToField(i, objectToSetNotPageFragmentsFields, elements);
                    } else {
                        List<?> pageFragments = setUpTheProxyForListOfPageFragments(by, root, listType);
                        setObjectToField(i, objectToSetNotPageFragmentsFields, pageFragments);
                    }
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException("Can't inject a value to field '" + i.getName() + "' in '"
                        + objectToSetNotPageFragmentsFields.getClass().getName() + "'", e);
                }

            }

        }
    }

    /**
     * Sets up the proxy element for the given By instance. If the given root is null, driver proxy is used for finding the web
     * element, otherwise the root element is used.
     * 
     * @param by
     * @param root
     * @return
     */
    public static WebElement setUpTheProxyForWebElement(final By by, final WebElement root) {
        return setUpTheProxyForWebElement(new ElementLocator() {
            @Override
            public WebElement findElement() {
                return root == null ? GrapheneContext.getProxy().findElement(by) : root.findElement(by);
            }

            @Override
            public List<WebElement> findElements() {
                return root == null ? GrapheneContext.getProxy().findElements(by) : root.findElements(by);
            }
        });
    }

    public static WebElement setUpTheProxyForWebElement(final By by, final WebElement root, final int indexInList) {
        return setUpTheProxyForWebElement(new ElementLocator() {
            @Override
            public WebElement findElement() {
                return root == null ? GrapheneContext.getProxy().findElements(by).get(indexInList) : root.findElements(by).get(
                    indexInList);
            }

            @Override
            public List<WebElement> findElements() {
                return root == null ? GrapheneContext.getProxy().findElements(by) : root.findElements(by);
            }
        });
    }

    public static WebElement setUpTheProxyForWebElement(final ElementLocator locator) {
        // proxy for WebElement should implement also Locatable.class to be usable with org.openqa.selenium.interactions.Actions
        WebElement e = GrapheneProxy.getProxyForFutureTarget(new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                return locator.findElement();
            }
        }, WebElement.class, Locatable.class);
        return e;
    }

    public static void setObjectToField(Field field, Object objectWithField, Object object) {

        boolean accessible = field.isAccessible();
        if (!accessible) {
            field.setAccessible(true);
        }
        try {
            field.set(objectWithField, object);
        } catch (Exception ex) {
            throw new GrapheneTestEnricherException("During enriching of " + NEW_LINE + objectWithField.getClass() + NEW_LINE
                + " the field " + NEW_LINE + field + " was not able to be set! Check the cause!", ex);
        }
        if (!accessible) {
            field.setAccessible(false);
        }
    }

    public static <PF> List<PF> setUpTheProxyForListOfPageFragments(final By by, final WebElement root,
        final Class<PF> pageFragmentClass) {
        List<PF> result = GrapheneProxy.getProxyForFutureTarget(new GrapheneProxy.FutureTarget() {
            @Override
            public Object getTarget() {
                WebDriver driver = GrapheneContext.getProxy();
                List<WebElement> elements = root == null ? driver.findElements(by) : root.findElements(by);
                List<PF> fragments = new ArrayList<PF>();
                for (int i = 0; i < elements.size(); i++) {
                    fragments.add(initializePageFragment(pageFragmentClass, setUpTheProxyForWebElement(by, root, i)));
                }
                return fragments;
            }

        }, List.class);
        return result;
    }

    public static List<WebElement> setUpTheProxyForListOfWebElements(final By by, final WebElement root) {
        List<WebElement> elements = GrapheneProxy.getProxyForFutureTarget(new GrapheneProxy.FutureTarget() {

            @Override
            public Object getTarget() {
                WebDriver driver = GrapheneContext.getProxy();
                List<WebElement> result = new ArrayList<WebElement>();
                List<WebElement> elements = root == null ? driver.findElements(by) : root.findElements(by);
                for (int i = 0; i < elements.size(); i++) {
                    result.add(setUpTheProxyForWebElement(by, root, i));
                }
                return result;
            }
        }, List.class);
        return elements;
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

    private static Class<?> getListType(Field listField) throws ClassNotFoundException {
        return Class.forName(listField.getGenericType().toString().split("<")[1].split(">")[0].split("<")[0]);
    }
}
