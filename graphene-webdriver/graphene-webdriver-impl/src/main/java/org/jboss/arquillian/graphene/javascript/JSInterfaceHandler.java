package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import org.openqa.selenium.WebDriver;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class JSInterfaceHandler implements MethodInterceptor {

    private final JSTarget target;
    private final WebDriver driver;

    public JSInterfaceHandler(WebDriver driver, JSTarget jsTarget) {
        this.target = jsTarget;
        this.driver = driver;
    }

    public JSTarget getTarget() {
        return target;
    }

    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        if (!target.getInterface().isInterface()) {
            if (!Modifier.isAbstract(method.getModifiers())) {
                return methodProxy.invokeSuper(obj, args);
            }
        }
        args = (args != null) ? args : new Object[]{};
        JSCall call = new JSCall(new JSMethod(target, method), args);
        return target.getResolver().execute(driver, call);
    }
}
