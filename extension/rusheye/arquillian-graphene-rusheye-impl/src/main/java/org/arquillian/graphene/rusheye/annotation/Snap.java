package org.arquillian.graphene.rusheye.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Snap {
	String value() default "";
	float onePixelThreshold() default -1f;
	int similarityCutOff() default -1;
	String[] masks() default {};
}
