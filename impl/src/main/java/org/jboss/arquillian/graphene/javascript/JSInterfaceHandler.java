package org.jboss.arquillian.graphene.javascript;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.jboss.arquillian.graphene.context.GrapheneContext;

public class JSInterfaceHandler implements MethodInterceptor {

    private final JSTarget target;
    private final GrapheneContext context;

    public JSInterfaceHandler(JSTarget target, GrapheneContext context) {
        this.target = target;
        this.context = context;
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
        if (method.getName().equals("finalize") && method.getParameterTypes().length == 0) {
            return null;
        }
        args = (args != null) ? args : new Object[]{};
        JSCall call = new JSCall(new JSMethod(target, method), args);
        return target.getResolver().execute(context, call);
    }
}
