package org.jboss.arquillian.graphene.spi.components.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.jboss.arquillian.graphene.spi.annotations.Root;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.FindBy;

/**
 * Factory class for initialising the particular <code>Component</code>.
 * 
 * @author jhuska
 * 
 */
public class Factory {

    /**
     * 
     * @param clazz
     * @return
     * @param <T> the final implementation of component
     */
    public static <T extends AbstractComponent> T initializeComponent(Class<T> clazz) {
        AbstractComponent component = instantiateComponent(clazz);

        // TODO read fields of component class and obtain root types and other elements
        Field[] declaredFields = clazz.getDeclaredFields();

        // TODO initialize root
        final RootReference rootReference = component.getRootReference();

        // TODO initialize other elements
        for (Field i : declaredFields) {

            Annotation[] annotations = i.getAnnotations();
            for (Annotation j : annotations) {

                if (j instanceof Root) {
                    WebElement rootElement = (WebElement) Proxy.newProxyInstance(WebElement.class.getClassLoader(),
                        new Class<?>[] { WebElement.class, Locatable.class }, new InvocationHandler() {

                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                WebElement root = rootReference.get();
                                if (root == null) {
                                    throw new RuntimeException("The root has to be set correct");
                                }
                                return (Object) method.invoke(root, args);
                            }
                        });
                    try {
                        boolean accessible = i.isAccessible();
                        if (!accessible) {
                            i.setAccessible(true);
                        }
                        i.set(component, rootElement);
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

                    WebElement referencedElement = (WebElement) Proxy.newProxyInstance(WebElement.class.getClassLoader(),
                        new Class<?>[] { WebElement.class, Locatable.class }, new InvocationHandler() {

                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                WebElement root = rootReference.get();
                                if (root == null) {
                                    throw new RuntimeException("You have to set root element correctly!");
                                }

                                WebElement myElement = root.findElement(findBy);

                                return method.invoke(myElement, args);
                            }
                        });

                    try {
                        boolean accessible = i.isAccessible();
                        if (!accessible) {
                            i.setAccessible(true);
                        }
                        i.set(component, referencedElement);
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

        return (T) component;
    }

    public static <T extends AbstractComponent> T instantiateComponent(Class<T> clazz) {
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
