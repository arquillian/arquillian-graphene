package org.jboss.arquillian.graphene.javascript;

import java.lang.annotation.Documented;
import static java.lang.annotation.ElementType.TYPE;
import java.lang.annotation.Retention;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

@Target({ TYPE })
@Retention(RUNTIME)
@Documented
public @interface Dependency {

    /**
     * Array of URLs pointing to resources containing needed JavaScript source code.
     */
    String[] sources() default {};

    /**
     * Array of dependencies - interfaces annotated by {@link JavaScript} annotation.
     */
    Class<?>[] interfaces() default {};
}
