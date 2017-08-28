/*package org.jboss.arquillian.graphene.assertions;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.page.document.Document;
import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.jboss.arquillian.graphene.request.RequestGuard;
import org.jboss.arquillian.graphene.request.RequestType;
import org.jboss.arquillian.graphene.wait.FluentWait;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.jboss.arquillian.graphene.guard.RequestGuardFactory;

import java.io.*;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.input.ReversedLinesFileReader;

public class GrapheneVerify extends Actions {

    private final RequestGuard guard;
    private final Document document;
    private final FluentWait<WebDriver, Void> waitGuard;
    private final GrapheneContext context;
    private final DocumentReady documentReady = new DocumentReady(); //method from other class
    private final RequestIsDone requestIsDone = new RequestIsDone(); // both from requestGuardFactory
    private final RequestChange requestChange = new RequestChange();

    public Actions Verify(WebDriver driver){
        return new Actions(driver);
    }

    public static WebElement verify(WebElement element) {
        this.guard = guard;
        this.document = document;
        this.context = context;
        this.waitGuard = waitAjax()
                .withTimeout(context.getConfiguration().getWaitGuardInterval(), TimeUnit.SECONDS)
                .pollingEvery(Math.min(context.getConfiguration().getWaitGuardInterval() * 100, 200), TimeUnit.MILLISECONDS);
    }

    *//**
     * Returns the guarded object checking whether the request of the given type is done during each method invocation. If the
     * request is not found, the {@link RequestGuardException} is thrown.
     *
     * @param <T> type of the given target
     * @param target object to be guarded
     * @param requestExpected the request type being checked after each method invocation
     * @param strict indicates that the expected request type can be interleaved by another type
     * @return the guarded object
     *//*
    public <T> T guard(T target, final RequestType requestExpected, final boolean strict) {
        if (requestExpected == null) {
            throw new IllegalArgumentException("The paremeter [requestExpected] is null.");
        }
        if (target == null) {
            throw new IllegalArgumentException("The paremeter [target] is null.");
        }
        GrapheneProxyInstance proxy;
        if (GrapheneProxy.isProxyInstance(target)) {
            proxy = (GrapheneProxyInstance) ((GrapheneProxyInstance) target).copy();
        } else {
            proxy = (GrapheneProxyInstance) GrapheneProxy.getProxyForTarget(context, target);
        }
        proxy.registerInterceptor(new Interceptor() {
            @Override
            public Object intercept(InvocationContext context) throws Throwable {

                guard.clearRequestDone();

                Object result = context.invoke();

                RequestType requestType;

                if (strict) {
                    requestType = waitForRequestChange();
                } else {
                    requestType = waitForRequestType(requestExpected);
                }

                if (requestType.equals(requestExpected)) {
                    waitForRequestFinished();
                } else {
                    throw new RequestGuardException(requestExpected, requestType);
                }

                return result;
            }

            @Override
            public int getPrecedence() {
                return 0;
            }

            private RequestType waitForRequestChange() {
                try {
                    return waitGuard.until(requestChange);
                } catch (TimeoutException e) {
                    return RequestType.NONE;
                }
            }

            private RequestType waitForRequestType(RequestType requestType) {
                try {
                    return waitGuard.until(new RequestTypeDone(requestExpected));
                } catch (TimeoutException e) {
                    return guard.getRequestType();
                }
            }

            private void waitForRequestFinished() {
                switch (requestExpected) {

                    case HTTP:
                        waitGuard.withMessage("Document didn't become ready").until(documentReady);
                        return;

                    case XHR:
                        waitGuard.until(requestIsDone);
                        return;

                    case NONE:
                        return;
                }
            }
        });
        return (T) proxy;
    }
}*/

/*try{
            ReversedLinesFileReader n = new ReversedLinesFileReader(new File("./log.txt"));
            String line;
            line = n.readLine();
            if(line.equals("after click " + element))
                line = n.readLine();
                if(line.equals("before click " + element))
                    return element;
            else
                throw new CouldNotVerifyException("Cannot verify the action was performed");



        }
        catch(FileNotFoundException e){
            System.out.println("no file");
        }
        catch(IOException e){
            System.out.println("io issues");
        }
    } //what can i do here?*/