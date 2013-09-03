package org.jboss.arquillian.graphene.intercept;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import org.jboss.arquillian.graphene.context.GrapheneContext;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jboss.arquillian.graphene.proxy.GrapheneProxy;
import org.jboss.arquillian.graphene.proxy.GrapheneProxyInstance;
import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * @author Lukas Fryc
 */
@RunWith(MockitoJUnitRunner.class)
public class TestInterceptorBuilder {

    @Mock
    WebDriver driver;

    @Mock
    Interceptor interceptor1;
    @Mock
    Interceptor interceptor2;

    @Mock
    By by;

    GrapheneContext context;

    @Before
    public void before() throws Throwable {
        Answer invoke = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return ((InvocationContext) invocation.getArguments()[0]).invoke();
            }
        };

        when(interceptor1.intercept(Mockito.any(InvocationContext.class))).thenAnswer(invoke);
        when(interceptor2.intercept(Mockito.any(InvocationContext.class))).thenAnswer(invoke);
        context = GrapheneContext.setContextFor(new GrapheneConfiguration(), driver, Default.class);
    }

    @Test
    public void test() throws Throwable {
        InterceptorBuilder builder = new InterceptorBuilder();
        builder.interceptInvocation(WebDriver.class, interceptor1).findElement(Interceptors.any(By.class));
        builder.interceptInvocation(WebDriver.class, interceptor2).findElement(Interceptors.any(By.class));
        Interceptor builtInterceptor = builder.build();

        WebDriver driverProxy = GrapheneProxy.getProxyForTargetWithInterfaces(context, driver, WebDriver.class);
        GrapheneProxyInstance proxy = (GrapheneProxyInstance) driverProxy;

        proxy.registerInterceptor(builtInterceptor);
        driverProxy.findElement(by);

        Mockito.inOrder(interceptor1, interceptor2);
        verify(interceptor1).intercept(Mockito.any(InvocationContext.class));
        verify(interceptor2).intercept(Mockito.any(InvocationContext.class));
    }
}
