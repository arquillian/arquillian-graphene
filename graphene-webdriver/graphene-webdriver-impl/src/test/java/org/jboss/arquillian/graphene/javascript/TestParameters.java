package org.jboss.arquillian.graphene.javascript;

import java.util.Arrays;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.GrapheneContext;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jboss.arquillian.graphene.TestingDriverStub;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
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
        instance = JSInterfaceFactory.create(GrapheneContext.setContextFor(new GrapheneConfiguration(), executor, Default.class), TestingInterface.class);
        when(executor.executeScript("return true;")).thenReturn(true);
    }

    @Test
    public void test_stringParameters() {
        // when
        instance.stringParameters("test");

        // then
        verify(executor, times(1)).executeScript(invocation("base", "stringParameters"), "test");
    }

    @Test
    public void test_integerParameters() {
        // when
        instance.integerParameters(2);

        // then
        verify(executor, times(1)).executeScript(invocation("base", "integerParameters"), 2);
    }

    @Test
    public void test_integerObjectParametes() {
        // when
        instance.integerObjectParameters(2);

        // then
        verify(executor, times(1)).executeScript(invocation("base", "integerObjectParameters"), 2);
    }

    @Test
    public void test_elementParameters() {
        WebElement element = mock(WebElement.class);

        // when
        instance.elementParameters(element);

        // then
        verify(executor, times(1)).executeScript(invocation("base", "elementParameters"), element);
    }

    @Test
    public void test_enumParameters() {
        // when
        instance.enumParameters(TestingEnum.VALUE2);

        // then
        verify(executor, times(1)).executeScript(invocation("base", "enumParameters"), "VALUE2");
    }
}
