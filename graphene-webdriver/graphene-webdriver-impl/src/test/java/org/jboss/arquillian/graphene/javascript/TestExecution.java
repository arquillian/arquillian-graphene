package org.jboss.arquillian.graphene.javascript;

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.TestingDriverStub;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.javascript.JavaScript;
import org.junit.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestExecution extends AbstractJavaScriptTest {


    @JavaScript
    public static interface TestingInterface {
        public void method();

        @MethodName("anotherMethodName")
        public void namedMethod();
    }

    @Test
    public void test_execution() {

        // given
        TestingDriverStub executor = spy(new TestingDriverStub());
        when(executor.executeScript("return true;")).thenReturn(true);

        // when
        TestingInterface instance = JSInterfaceFactory.create(GrapheneContext.setContextFor(new GrapheneConfiguration(), executor, Default.class), TestingInterface.class);
        instance.method();

        // then
        verify(executor, times(1)).executeScript(invocation("TestingInterface", "method"));
    }

    @Test
    public void test_execution_with_named_method() {

        // given
        TestingDriverStub executor = spy(new TestingDriverStub());
        when(executor.executeScript("return true;")).thenReturn(true);

        // when
        TestingInterface instance = JSInterfaceFactory.create(GrapheneContext.setContextFor(new GrapheneConfiguration(), executor, Default.class), TestingInterface.class);
        instance.namedMethod();

        // then
        verify(executor, times(1)).executeScript(invocation("TestingInterface", "anotherMethodName"));
    }

    @JavaScript("base")
    public static interface TestingInterfaceWithBase {
        public void method();
    }

    @Test
    public void test_execution_with_base() {

        // given
        TestingDriverStub executor = spy(new TestingDriverStub());
        when(executor.executeScript("return true;")).thenReturn(true);

        // when
        TestingInterfaceWithBase instance = JSInterfaceFactory.create(GrapheneContext.setContextFor(new GrapheneConfiguration(), executor, Default.class), TestingInterfaceWithBase.class);
        instance.method();

        // then
        verify(executor, times(1)).executeScript(invocation("base", "method"));
    }
}
