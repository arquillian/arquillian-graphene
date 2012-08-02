package org.jboss.arquillian.graphene.javascript;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.context.TestingDriverStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class TestReturnValues extends AbstractJavaScriptTest {

    @Spy
    TestingDriverStub executor = new TestingDriverStub();

    TestingInterface instance;

    Object answer;

    @JavaScript("base")
    public interface TestingInterface {
        void voidMethod();

        String returnString();

        int returnInteger();

        Integer returnIntegerObject();

        TestingEnum returnEnumValue();
    }

    @Before
    public void prepareTest() {
        // given
        MockitoAnnotations.initMocks(this);
        GrapheneContext.set(executor);
        JSInterfaceFactory<TestingInterface> factory = JSInterfaceFactory.create(TestingInterface.class);
        instance = factory.instantiate();

        when(executor.executeScript(Mockito.anyString())).then(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return answer;
            }
        });
    }

    @Test
    public void test_voidMethod() {
        // when
        instance.voidMethod();

        // then
        verify(executor, only()).executeScript(invocation("base", "voidMethod"));
    }

    @Test
    public void test_returnString() {
        // when
        answer = "someString";
        String result = instance.returnString();

        // then
        assertEquals(answer, result);
        verify(executor, only()).executeScript(invocation("base", "returnString"));
    }

    @Test
    public void test_returnInteger() {
        // when
        answer = 1;
        int result = instance.returnInteger();

        // then
        assertEquals(answer, result);
        verify(executor, only()).executeScript(invocation("base", "returnInteger"));
    }

    @Test
    public void test_returnIntegerObject() {
        // when
        answer = 1;
        Integer result = instance.returnIntegerObject();

        // then
        assertEquals(answer, result);
        verify(executor, only()).executeScript(invocation("base", "returnIntegerObject"));
    }

    @Test
    public void test_returnIntegerObject_null() {
        // when
        answer = null;
        Integer result = instance.returnIntegerObject();

        // then
        assertEquals(answer, result);
        verify(executor, only()).executeScript(invocation("base", "returnIntegerObject"));
    }

    @Test
    public void test_returnEnumValue() {
        // when
        answer = "VALUE2";
        TestingEnum result = instance.returnEnumValue();

        // then
        assertEquals(TestingEnum.VALUE2, result);
        verify(executor, only()).executeScript(invocation("base", "returnEnumValue"));
    }
}
