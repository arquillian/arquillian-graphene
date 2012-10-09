package org.jboss.arquillian.graphene.enricher;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;

import org.jboss.arquillian.graphene.enricher.fragment.AbstractPageFragmentStub;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class TestInitializePageFragment {

    private AbstractPageFragmentStub abstrPageFragmentStub;

    private final String ROOT_METHOD_RETURN_VAL = "root";
    private final String REF_BY_CLASS_METHOD_RETURN_VAL = "refByClassName";

    @Before
    public void initializeMocks() {
        abstrPageFragmentStub = Factory.initializePageFragment(AbstractPageFragmentStub.class, createRoot());
    }

    @Test
    public void testInitializedPageFragmentIsNotNull() {
        assertNotNull("The initialized page fragment can not be null!", abstrPageFragmentStub);
    }

    @Test
    public void testIsRootInitialized() {
        assertNotNull("Root should be initialized!", abstrPageFragmentStub.getRootProxy());
    }

    @Test
    public void testMethodInvocationOnRoot() {
        assertEquals("The return value of method invoked on root element is wrong!", abstrPageFragmentStub.invokeMethodOnRoot(),
            ROOT_METHOD_RETURN_VAL);
    }

    @Test
    public void testMethodInvocationOnReferencedElement() {
        assertEquals("The method onvoked on referenced element returned wrong value!",
            abstrPageFragmentStub.invokeMethodOnElementRefByClass(), REF_BY_CLASS_METHOD_RETURN_VAL);
    }

    private WebElement createRoot() {
        WebElement root = Mockito.mock(WebElement.class);
        when(root.getText()).thenReturn(ROOT_METHOD_RETURN_VAL);

        WebElement elemByClass = Mockito.mock(WebElement.class);
        when(elemByClass.getText()).thenReturn(REF_BY_CLASS_METHOD_RETURN_VAL);
        when(root.findElement(By.className(anyString()))).thenReturn(elemByClass);

        return root;
    }

}
