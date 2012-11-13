package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.Proxy;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class JSInterfaceFactory<T> {

    private JSInterfaceHandler handler;

    private JSInterfaceFactory(WebDriver driver, Class<T> jsInterface) {

        if (!jsInterface.isInterface()) {
            throw new IllegalArgumentException("interface must be provided");
        }

        this.handler = new JSInterfaceHandler(driver, new JSTarget(jsInterface));

    }

    public static <T> T create(Class<T> jsInterface) {
        return create((WebDriver) GrapheneContext.getProxyForInterfaces(JavascriptExecutor.class), jsInterface);
    }

    public static <T> T create(WebDriver driver, Class<T> jsInterface) {
        return new JSInterfaceFactory<T>(driver, jsInterface).instantiate();
    }

    @SuppressWarnings("unchecked")
    public T instantiate() {
        Class<?> jsInterface = handler.getTarget().getInterface();
        return (T) Proxy.newProxyInstance(jsInterface.getClassLoader(), new Class<?>[] { jsInterface }, handler);
    }

}
