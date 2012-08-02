package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;

class JSTarget {

    private Class<?> jsInterface;
    private JavaScript javascriptAnnotation;
    private Dependency dependecyAnnotation;
    private ExecutionResolver resolver;

    public JSTarget(Class<?> jsInterface) {
        this.jsInterface = jsInterface;
        resolveAnnotations();
        this.resolver = createResolver(javascriptAnnotation);
    }

    private void resolveAnnotations() {
        this.javascriptAnnotation = jsInterface.getAnnotation(JavaScript.class);
        this.dependecyAnnotation = jsInterface.getAnnotation(Dependency.class);
    }

    public Class<?> getInterface() {
        return jsInterface;
    }

    public String getName() {
        if ("".equals(javascriptAnnotation.value())) {
            return null;
        }
        return javascriptAnnotation.value();
    }

    @SuppressWarnings("unchecked")
    public Collection<String> getSourceDependencies() {
        if (dependecyAnnotation == null) {
            return (Collection<String>) Collections.EMPTY_LIST;
        }
        return Arrays.asList(dependecyAnnotation.sources());
    }
    
    @SuppressWarnings("unchecked")
    public Collection<JSTarget> getJSInterfaceDependencies() {
        if (dependecyAnnotation == null) {
            return (Collection<JSTarget>) Collections.EMPTY_LIST;
        }
        return Collections2.transform(Arrays.asList(dependecyAnnotation.interfaces()), new Function<Class<?>, JSTarget>() {
            @Override
            public JSTarget apply(Class<?> input) {
                return new JSTarget(input);
            }
        });
    }
    
    public JSMethod getJSMethod(String methodName, Object... arguments) {
        Class<?>[] types = new Class[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            types[i] = arguments.getClass();
        }
        try {
            Method method = jsInterface.getMethod(methodName, types);
            return new JSMethod(this, method);
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }

    public boolean isInstallable() {
        return InstallableJavaScript.class.isAssignableFrom(jsInterface);
    }

    public ExecutionResolver getResolver() {
        return resolver;
    }

    private ExecutionResolver createResolver(JavaScript annotation) {
        try {
            return annotation.methodResolver().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("resolver " + annotation.methodResolver() + " can't be instantied", e);
        }
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
