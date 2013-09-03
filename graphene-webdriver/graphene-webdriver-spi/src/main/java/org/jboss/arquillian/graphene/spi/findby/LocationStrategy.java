package org.jboss.arquillian.graphene.spi.findby;

import java.lang.annotation.Annotation;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * <p>Allows to locate {@link WebElement}s by providing their locators ({@link By}).</p>
 *
 * <p>See an example usage in {@link ImplementsLocationStrategy}.
 *
 * @author Lukas Fryc
 *
 * @see ImplementsLocationStrategy
 */
public interface LocationStrategy {

    /**
     * Transforms annotation which is marked with this location strategy using {@link ImplementsLocationStrategy} annotation to locator {@link By}.
     */
    public abstract By fromAnnotation(Annotation annotation);

}
