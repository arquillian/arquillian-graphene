package org.jboss.arquillian.graphene.findby;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.List;

import org.jboss.arquillian.core.spi.Validate;
import org.jboss.arquillian.graphene.spi.findby.LocationStrategy;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

public class ByJQuery extends By {

    private static final String IMPLEMENTATION_CLASS = "org.jboss.arquillian.graphene.findby.ByJQueryImpl";

    private By implementation;

    public ByJQuery(String selector) {
        Validate.notNull(selector, "Cannot find elements when selector is null!");
        this.implementation = instantiate(selector);
    }

    public static ByJQuery selector(String selector) {
        return new ByJQuery(selector);
    }

    @Override
    public WebElement findElement(SearchContext context) {
        return implementation.findElement(context);
    }

    @Override
    public List<WebElement> findElements(SearchContext context) {
        return implementation.findElements(context);
    }

    @Override
    public String toString() {
        return implementation.toString();
    }

    private static By instantiate(String selector) {
        try {
            @SuppressWarnings("unchecked")
            Class<? extends By> clazz = (Class<? extends By>) Class.forName(IMPLEMENTATION_CLASS);

            Constructor<? extends By> constructor = clazz.getConstructor(String.class);

            return constructor.newInstance(selector);

        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("Cannot find class " + IMPLEMENTATION_CLASS
                    + ", make sure you have arquillian-graphene-impl.jar included on the classpath.", e);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public static class JQueryLocationStrategy implements LocationStrategy {

        @Override
        public ByJQuery fromAnnotation(Annotation annotation) {
            FindByJQuery findBy = (FindByJQuery) annotation;
            return new ByJQuery(findBy.value());
        }
    }
}
