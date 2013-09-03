package org.jboss.arquillian.graphene.spi.findby;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

/**
 * <p>Enables to introduce new annotations of {@link FindBy} type with its own location strategy.</p>
 *
 * <p>E.g.: framework specific location strategies, extended grammars for well-known strategies, etc.</p>
 *
 * <p>Usage:</p>
 *
 * <pre>
 * &#64;Retention(RetentionPolicy.RUNTIME)
 * &#64;Target(ElementType.FIELD)
 * &#64;ImplementsLocationStrategy(by = XYZLocationStrategy.class)
 * public &#64;interface FindByXYZ {
 *     String value();
 * }
 * </pre>
 *
 * <pre>
 * public static class XYZLocationStrategy implements {@link LocationStrategy} {
 *     public {@link By} fromAnnotation({@link Annotation} annotation) {
 *         return ...;
 *     }
 * }
 * </pre>
 *
 * @author Lukas Fryc
 *
 * @see LocationStrategy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ImplementsLocationStrategy {

    Class<? extends LocationStrategy> value();

}
