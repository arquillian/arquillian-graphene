package org.jboss.arquillian.ajocado.framework;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AjocadoConfigurationContext implements InvocationHandler {
    /**
     * The thread local context of AjaxSelenium
     */
    private static final ThreadLocal<AjocadoConfiguration> REFERENCE = new ThreadLocal<AjocadoConfiguration>();

    private AjocadoConfigurationContext() {
    }

    /**
     * Sets the AjaxSelenium context for current thread
     * 
     * @param selenium
     *            the AjaxSelenium instance
     */
    public static void setContext(AjocadoConfiguration selenium) {
        REFERENCE.set(selenium);
    }

    /**
     * Returns the context of AjaxSelenium for current thread
     * 
     * @return the context of AjaxSelenium for current thread
     */
    private static AjocadoConfiguration getCurrentContext() {
        return REFERENCE.get();
    }

    public static boolean isContextInitialized() {
        return getCurrentContext() != null;
    }

    /**
     * Returns the instance of proxy to thread local context of AjaxSelenium
     * 
     * @return the instance of proxy to thread local context of AjaxSelenium
     */
    public static AjocadoConfiguration getProxy() {
        return (AjocadoConfiguration) Proxy.newProxyInstance(AjocadoConfigurationContext.class.getClassLoader(),
            new Class[] { AjocadoConfiguration.class }, new AjocadoConfigurationContext());
    }

    /**
     * End point for handling invocations on proxy
     */
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result;
        try {
            if (!isContextInitialized()) {
                throw new IllegalStateException("Context is not initialized");
            }
            result = method.invoke(getCurrentContext(), args);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (Exception e) {
            throw new RuntimeException("unexpected invocation exception: " + e.getMessage(), e);
        }
        return result;
    }
}
