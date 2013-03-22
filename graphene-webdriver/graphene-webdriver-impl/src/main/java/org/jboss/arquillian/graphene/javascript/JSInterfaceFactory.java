package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.Modifier;
import org.jboss.arquillian.graphene.GrapheneContext;

public class JSInterfaceFactory<T> {

    private final JSInterfaceHandler handler;

    public JSInterfaceFactory(GrapheneContext context, Class<T> jsInterface) {
        if (!jsInterface.isInterface() && !Modifier.isAbstract(jsInterface.getModifiers())) {
            throw new IllegalArgumentException("interface or abstract class must be provided :" + jsInterface);
        }
        this.handler = new JSInterfaceHandler(new JSTarget(jsInterface), context);
    }

    public static <T> T create(GrapheneContext context, Class<T> jsInterface) {
        return new JSInterfaceFactory<T>(context, jsInterface).instantiate();
    }

    @SuppressWarnings("unchecked")
    public T instantiate() {
        Class<?> jsInterface = handler.getTarget().getInterface();
        return (T) ClassImposterizer.INSTANCE.imposterise(handler, jsInterface);
    }
}
