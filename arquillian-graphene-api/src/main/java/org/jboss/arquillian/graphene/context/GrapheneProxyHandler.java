package org.jboss.arquillian.graphene.context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.openqa.selenium.WebDriver;

/**
 * Proxy invocation handler
 */
class GrapheneProxyHandler implements InvocationHandler {

    private Object target;
    private FutureTarget future;

    private GrapheneProxyHandler() {
    }

    public static GrapheneProxyHandler forFuture(FutureTarget future) {
        GrapheneProxyHandler handler = new GrapheneProxyHandler();
        handler.future = future;
        return handler;
    }

    public static GrapheneProxyHandler forTarget(Object target) {
        GrapheneProxyHandler handler = new GrapheneProxyHandler();
        handler.target = target;
        return handler;
    }

    /**
     * End point for handling invocations on proxy
     */
    public Object invoke(Object p, Method method, Object[] args) throws Throwable {

        Object result = invokeReal(method, args);

        if (isProxyable(method, args)) {
            return getProxy(method, result);
        }

        return result;
    }

    boolean isProxyable(Method method, Object[] args) {
        Class<?> returnType = method.getReturnType();
        return returnType.isInterface();
    }

    Object getProxy(Method method, Object result) throws Throwable {
        GrapheneProxyHandler newHandler = GrapheneProxyHandler.forTarget(result);
        return Proxy.newProxyInstance(WebDriver.class.getClassLoader(), new Class[] { method.getReturnType() }, newHandler);
    }

    Object invokeReal(Method method, Object[] args) throws Throwable {
        Object result;
        try {
            result = method.invoke(getTarget(), args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage(), e);
        }
        return result;
    }
    
    Object getTarget() {
        return (future == null) ? target : future.getTarget();
    }
}