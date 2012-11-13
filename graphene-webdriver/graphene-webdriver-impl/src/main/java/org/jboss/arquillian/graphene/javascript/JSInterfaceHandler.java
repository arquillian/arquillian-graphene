package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import org.openqa.selenium.WebDriver;

public class JSInterfaceHandler implements InvocationHandler {

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
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        args = (args != null) ? args : new Object[]{};
        JSCall call = new JSCall(new JSMethod(target, method), args);
        return target.getResolver().execute(driver, call);
    }

}
