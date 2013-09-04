package org.jboss.arquillian.graphene.enricher;

import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.openqa.selenium.WebDriver;

public class InFrameInterceptor implements Interceptor {

    private int indexOfFrame = -1;
    private String nameOrIdOfFrame = null;

    public InFrameInterceptor(String nameOrIdOfFrame) {
        if (nameOrIdOfFrame == null || nameOrIdOfFrame.length() == 0) {
            throw new IllegalArgumentException("nameOrIdOfFrame can not be null or an empty string!");
        }
        this.nameOrIdOfFrame = nameOrIdOfFrame;
    }

    public InFrameInterceptor(int indexOfFrame) {
        if (indexOfFrame < 0) {
            throw new IllegalArgumentException("indexOfFrame can not be less than zero!");
        }
        this.indexOfFrame = indexOfFrame;
    }

    @Override
    public Object intercept(InvocationContext context) throws Throwable {
        WebDriver browser = context.getGrapheneContext().getWebDriver();
        if (indexOfFrame != -1) {
            browser.switchTo().frame(indexOfFrame);
        } else if (nameOrIdOfFrame != null) {
            browser.switchTo().frame(nameOrIdOfFrame);
        }
        Object result = null;
        try {
            result = context.invoke();
        } finally {
            browser.switchTo().defaultContent();
        }
        if (result instanceof GrapheneProxyInstance) {
            ((GrapheneProxyInstance) result).registerInterceptor(this);
        }
        return result;
    }
}
