package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.Modifier;

public class JSInterfaceFactory<T> {

    private JSInterfaceHandler handler;

    private JSInterfaceFactory(Class<T> jsInterface) {

        if (!jsInterface.isInterface() && !Modifier.isAbstract(jsInterface.getModifiers())) {
            throw new IllegalArgumentException("interface or abstract class must be provided :" + jsInterface);
        }

        this.handler = new JSInterfaceHandler(new JSTarget(jsInterface));

    }

    public static <T> T create(Class<T> jsInterface) {
        return new JSInterfaceFactory<T>(jsInterface).instantiate();
    }

    @SuppressWarnings("unchecked")
    public T instantiate() {
        Class<?> jsInterface = handler.getTarget().getInterface();
        return (T) ClassImposterizer.INSTANCE.imposterise(handler, jsInterface);
    }
}
