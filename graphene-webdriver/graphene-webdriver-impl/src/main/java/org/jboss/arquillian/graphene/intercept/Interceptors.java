package org.jboss.arquillian.graphene.intercept;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * Utility class with helper methods for building interceptors using {@link InterceptorBuilder}.
 *
 * @author Lukas Fryc
 */
public final class Interceptors {

    public static <T> T any(Class<T> type) {
        return (T) ClassImposterizer.INSTANCE.imposterise(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                return null;
            }
        }, type);
    }
}
