package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;

public class JSMethod {

    private JSTarget target;
    private Method method;
    private String name;

    public JSMethod(JSTarget target, Method method) {
        this.target = target;
        this.method = method;
        this.name = resolveName(method);

    }

    public JSTarget getTarget() {
        return target;
    }

    public Method getMethod() {
        return method;
    }

    public String getName() {
        return name;
    }

    private String resolveName(Method method) {
        MethodName annotation = method.getAnnotation(MethodName.class);

        if (annotation != null && !StringUtils.isEmpty(annotation.value())) {
            return annotation.value();
        }

        return method.getName();
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
