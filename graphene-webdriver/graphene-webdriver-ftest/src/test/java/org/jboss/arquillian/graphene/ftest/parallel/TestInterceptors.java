/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.arquillian.graphene.ftest.parallel;

import java.util.concurrent.atomic.AtomicInteger;

import junit.framework.Assert;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Before;
import org.junit.Test;

import qualifier.Browser1;
import qualifier.Browser2;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestInterceptors extends AbstractParallelTest {

    @Browser1
    @ArquillianResource
    private GrapheneContext context1;

    @Browser2
    @ArquillianResource
    private GrapheneContext context2;

    @ArquillianResource
    private GrapheneContext contextDefault;

    private AtomicInteger counter1 = new AtomicInteger();
    private AtomicInteger counter2 = new AtomicInteger();
    private AtomicInteger counterDefault = new AtomicInteger();

    @Before
    public void resetCounters() {
        this.counter1.set(0);
        this.counter2.set(0);
        this.counterDefault.set(0);
    }

    @Test
    public void testContextVsDrone() {
        ((GrapheneProxyInstance) browser1).registerInterceptor(createInterceptor(counter1));
        ((GrapheneProxyInstance) browser2).registerInterceptor(createInterceptor(counter2));
        ((GrapheneProxyInstance) browserDefault).registerInterceptor(createInterceptor(counterDefault));

        context1.getWebDriver().getTitle();
        context2.getWebDriver().getTitle();
        contextDefault.getWebDriver().getTitle();

        Assert.assertEquals(1, counter1.get());
        Assert.assertEquals(1, counter2.get());
        Assert.assertEquals(1, counterDefault.get());
    }

    protected Interceptor createInterceptor(final AtomicInteger counter) {
        return new Interceptor() {
            @Override
            public Object intercept(InvocationContext context) throws Throwable {
                counter.incrementAndGet();
                return context.invoke();
            }
        };
    }

}
