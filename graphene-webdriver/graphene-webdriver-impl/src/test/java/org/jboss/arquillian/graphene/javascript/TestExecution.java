package org.jboss.arquillian.graphene.javascript;

import static org.mockito.Mockito.only;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.context.TestingDriverStub;
import org.junit.Test;

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

        // when
        GrapheneContext.set(executor);
        TestingInterface instance = JSInterfaceFactory.create(TestingInterface.class);
        instance.method();

        // then
        verify(executor, only()).executeScript(invocation("testingInterface", "method"));
    }

    @Test
    public void test_execution_with_named_method() {

        // given
        TestingDriverStub executor = spy(new TestingDriverStub());

        // when
        GrapheneContext.set(executor);
        TestingInterface instance = JSInterfaceFactory.create(TestingInterface.class);
        instance.namedMethod();

        // then
        verify(executor, only()).executeScript(invocation("testingInterface", "anotherMethodName"));
    }

    @JavaScript("base")
    public static interface TestingInterfaceWithBase {
        public void method();
    }

    @Test
    public void test_execution_with_base() {

        // given
        TestingDriverStub executor = spy(new TestingDriverStub());

        // when
        GrapheneContext.set(executor);
        TestingInterfaceWithBase instance = JSInterfaceFactory.create(TestingInterfaceWithBase.class);
        instance.method();

        // then
        verify(executor, only()).executeScript(invocation("base", "method"));
    }
}
