package org.jboss.arquillian.graphene.javascript;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target({ TYPE, FIELD })
@Retention(RUNTIME)
@Documented
public @interface JavaScript {

    String value() default "";

    Class<? extends ExecutionResolver> methodResolver() default DefaultExecutionResolver.class;

    abstract static class DefaultExecutionResolver implements ExecutionResolver {
        static final String IMPLEMENTATION = "org.jboss.arquillian.graphene.javascript.DefaultExecutionResolver";
    }
}
