package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.Proxy;

public class JSInterfaceFactory<T> {

    private JSInterfaceHandler handler;

    private JSInterfaceFactory(Class<T> jsInterface) {

        if (!jsInterface.isInterface()) {
            throw new IllegalArgumentException("interface must be provided");
        }

        this.handler = new JSInterfaceHandler(new JSTarget(jsInterface));

    }

    public static <T> T create(Class<T> jsInterface) {
        return new JSInterfaceFactory<T>(jsInterface).instantiate();
    }

    @SuppressWarnings("unchecked")
    public T instantiate() {
        Class<?> jsInterface = handler.getTarget().getInterface();
        return (T) Proxy.newProxyInstance(jsInterface.getClassLoader(), new Class<?>[] { jsInterface }, handler);
    }

}
