package org.jboss.arquillian.graphene.findby;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jboss.arquillian.graphene.spi.findby.ImplementsLocationStrategy;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@ImplementsLocationStrategy(ByJQuery.JQueryLocationStrategy.class)
public @interface FindByJQuery {

    String value();
}
