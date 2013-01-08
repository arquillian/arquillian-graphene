package org.jboss.arquillian.graphene.enricher;

import static org.jboss.arquillian.graphene.Graphene.waitGui;

import java.util.concurrent.atomic.AtomicReference;

import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;

import com.google.common.base.Predicate;

public class StaleElementInterceptor implements Interceptor {

    @Override
    public Object intercept(final InvocationContext context) throws Throwable {
        final AtomicReference<Object> result = new AtomicReference<Object>();
        final AtomicReference<Throwable> failure = new AtomicReference<Throwable>();

        waitGui().until(new Predicate<WebDriver>() {
            @Override
            public boolean apply(WebDriver driver) {
                try {
                    result.set(context.invoke());
                    return true;
                } catch (StaleElementReferenceException e) {
                    return false;
                } catch (Throwable e) {
                    failure.set(e);
                    return true;
                }
            }
        });

        if (failure.get() != null) {
            throw failure.get();
        }

        return result.get();
    }
}
