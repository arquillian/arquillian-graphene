package org.jboss.arquillian.graphene.context;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.openqa.selenium.WebDriver;

public class TestGrapheneContextHolding {

    @Mock
    WebDriver driver;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void when_instance_is_provided_to_context_then_context_holds_it() {
        // having
        // when
        GrapheneContext.set(driver);

        // then
        assertSame(driver, GrapheneContext.get());
    }

    @Test
    public void when_instance_is_provided_to_context_then_context_is_initialized() {
        // having
        // when
        GrapheneContext.set(driver);

        // then
        assertTrue(GrapheneContext.isInitialized());
    }

    @Test
    public void when_context_is_reset_then_context_is_not_initialized() {
        // having
        GrapheneContext.set(driver);

        // when
        GrapheneContext.reset();

        // then
        assertFalse(GrapheneContext.isInitialized());
    }

    @Test(expected = NullPointerException.class)
    public void when_calling_get_on_not_initialized_context_then_context_throws_exception() {
        GrapheneContext.get();
    }

    @Test(expected = IllegalArgumentException.class)
    public void when_set_null_instance_to_context_then_context_throws_exception() {
        GrapheneContext.set(null);
    }
}
