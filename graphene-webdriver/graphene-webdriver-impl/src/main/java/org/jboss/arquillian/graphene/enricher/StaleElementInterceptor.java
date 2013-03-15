package org.jboss.arquillian.graphene.enricher;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

import java.util.concurrent.atomic.AtomicReference;

import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import com.google.common.base.Predicate;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.TimeoutException;

public class StaleElementInterceptor implements Interceptor {

    @Override
    public Object intercept(final InvocationContext context) throws Throwable {
        final AtomicReference<Object> result = new AtomicReference<Object>();
        final AtomicReference<Throwable> failure = new AtomicReference<Throwable>();
        final AtomicReference<Throwable> staleness = new AtomicReference<Throwable>();
        try {
            waitGui().until(new Predicate<WebDriver>() {
                @Override
                public boolean apply(WebDriver driver) {
                    try {
                        result.set(context.invoke());
                        return true;
                    } catch (StaleElementReferenceException e) {
                        staleness.set(e);
                        return false;
                    } catch (Throwable e) {
                        failure.set(e);
                        return true;
                    }
                }
            });
        } catch(TimeoutException e) {
            if (staleness.get() != null) {
                throw staleness.get();
            } else {
                throw e;
            }
        }

        if (failure.get() != null) {
            throw failure.get();
        }

        return result.get();
    }
}
