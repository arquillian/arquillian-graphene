package org.jboss.arquillian.graphene.ftest.enricher;

import java.util.concurrent.atomic.AtomicInteger;
import org.jboss.arquillian.graphene.ftest.enricher.AbstractPageTest.AbstractPage;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
@Ignore
public class TestPageObjects extends AbstractPageTest{

    @Page
    private SimplePage page;

    @Test
    public void testFinalFiledInAbstractPage1() {
        Assert.assertTrue(page.isNameNotNull());
    }

    @Test
    public void testFinalFiledInAbstractPage2() {
        Assert.assertTrue(getPage().isNameNotNull());
    }

    @Test
    public void testFinalMethods() {
        final AtomicInteger counter = new AtomicInteger(0);
        ((GrapheneProxyInstance) getPage()).registerInterceptor(new Interceptor() {
            @Override
            public Object intercept(InvocationContext context) throws Throwable {
                if (context.getMethod().getName().equals("isNameNotNull")) {
                    counter.incrementAndGet();
                }
                return context.invoke();
            }
        });
        Assert.assertEquals("The interceptor hasn't been called.", 1, counter.intValue());
    }

    @Override
    public AbstractPage getPage() {
        return page;
    }

    public static class SimplePage extends AbstractPage {
    }

}
abstract class AbstractPageTest {

    public abstract AbstractPage getPage();

    public static abstract class AbstractPage {
        protected final String name = "NAME";

        public final boolean isNameNotNull() {
            return name != null;
        }
    }

}
