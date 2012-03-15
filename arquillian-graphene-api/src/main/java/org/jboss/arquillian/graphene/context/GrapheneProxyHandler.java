package org.jboss.arquillian.graphene.context;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.Queue;

import org.openqa.selenium.WebDriver;

/**
 * Proxy invocation handler
 */
class GrapheneProxyHandler implements InvocationHandler {

    private final String webDriverPackage = "org.openqa.selenium";
    private Queue<Method> methodQueue = new LinkedList<Method>();

    /**
     * End point for handling invocations on proxy
     */
    public Object invoke(Object p, Method method, Object[] args) throws Throwable {
        GrapheneProxyHandler proxy = (GrapheneProxyHandler) Proxy.getInvocationHandler(p);

        if (returnsWebDriverApi(method, args)) {
            return getProxy(method);
        }

        return invokeReal(proxy, method, args);
    }

    boolean returnsWebDriverApi(Method method, Object[] args) {
        Class<?> returnType = method.getReturnType();
        return returnType.isInterface() && returnType.getName().startsWith(webDriverPackage);
    }

    Object getProxy(Method method) {
        GrapheneProxyHandler newHandler = new GrapheneProxyHandler();
        newHandler.methodQueue.addAll(methodQueue);
        newHandler.methodQueue.add(method);
        return Proxy.newProxyInstance(WebDriver.class.getClassLoader(), new Class[] { method.getReturnType() }, newHandler);
    }

    Object invokeReal(GrapheneProxyHandler proxy, Method method, Object[] args) throws Throwable {
        Object target = getTarget();
        Object result;
        try {
            result = method.invoke(target, args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage(), e);
        }
        return result;
    }

    Object getTarget() throws Throwable {
        Object target = GrapheneContext.get();
        Method method;
        while ((method = methodQueue.poll()) != null) {
            target = method.invoke(target);
        }
        return target;
    }
}