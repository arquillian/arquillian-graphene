package org.jboss.arquillian.graphene.javascript;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.context.TestingDriverStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.openqa.selenium.WebElement;

public class TestParameters extends AbstractJavaScriptTest {

    @Spy
    TestingDriverStub executor = new TestingDriverStub();

    TestingInterface instance;

    @JavaScript("base")
    public interface TestingInterface {
        void stringParameters(String param);

        void integerParameters(int param);

        void integerObjectParameters(Integer param);

        void elementParameters(WebElement param);

        void enumParameters(TestingEnum param);
    }

    @Before
    public void prepareTest() {
        // given
        MockitoAnnotations.initMocks(this);
        GrapheneContext.set(executor);
        JSInterfaceFactory<TestingInterface> factory = JSInterfaceFactory.create(TestingInterface.class);
        instance = factory.instantiate();
    }

    @Test
    public void test_stringParameters() {
        // when
        instance.stringParameters("test");

        // then
        verify(executor, only()).executeScript(invocation("base", "stringParameters"), "test");
    }

    @Test
    public void test_integerParameters() {
        // when
        instance.integerParameters(2);

        // then
        verify(executor, only()).executeScript(invocation("base", "integerParameters"), 2);
    }

    @Test
    public void test_integerObjectParametes() {
        // when
        instance.integerObjectParameters(2);

        // then
        verify(executor, only()).executeScript(invocation("base", "integerObjectParameters"), 2);
    }

    @Test
    public void test_elementParameters() {
        WebElement element = mock(WebElement.class);

        // when
        instance.elementParameters(element);

        // then
        verify(executor, only()).executeScript(invocation("base", "elementParameters"), element);
    }

    @Test
    public void test_enumParameters() {
        // when
        instance.enumParameters(TestingEnum.VALUE2);

        // then
        verify(executor, only()).executeScript(invocation("base", "enumParameters"), "VALUE2");
    }
}
