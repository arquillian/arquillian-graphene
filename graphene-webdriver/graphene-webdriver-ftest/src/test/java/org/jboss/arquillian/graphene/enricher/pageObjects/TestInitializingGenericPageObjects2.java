package org.jboss.arquillian.graphene.enricher.pageObjects;

import static org.junit.Assert.assertEquals;

import org.jboss.arquillian.graphene.enricher.pageObjects.pages.TestPage;
import org.jboss.arquillian.graphene.enricher.pageObjects.pages.TestPage2;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@RunWith(Arquillian.class)
public class TestInitializingGenericPageObjects2 extends AbstractTest<TestPage, TestPage2> {

    private final String EXPECTED_NESTED_ELEMENT_TEXT = "Some Value";

    @Test
    public void testPageObjectWithGenericTypeIsInitialized1() {
        loadPage();
        assertEquals("The page object was not set correctly!", pageWithGenericType.getAbstractPageFragment()
            .invokeMethodOnElementRefByXpath(), EXPECTED_NESTED_ELEMENT_TEXT);
    }

    @Test
    public void testPageObjectWithGenericTypeIsInitialized2() {
        loadPage();
        assertEquals("The page object was not set correctly!", anotherPageWithGenericType.getAbstractPageFragment()
            .invokeMethodOnElementRefByXpath(), EXPECTED_NESTED_ELEMENT_TEXT);
    }
}
