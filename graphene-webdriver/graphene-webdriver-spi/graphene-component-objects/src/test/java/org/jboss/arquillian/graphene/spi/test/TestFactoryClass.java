package org.jboss.arquillian.graphene.spi.test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

import org.jboss.arquillian.graphene.spi.components.common.AbstractComponent;
import org.jboss.arquillian.graphene.spi.components.common.AbstractComponentStub;
import org.jboss.arquillian.graphene.spi.components.common.Factory;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TestFactoryClass {

    private AbstractComponent abstrComponent;

    private final String ROOT_METHOD_RETURN_VAL = "root";
    private final String REF_BY_CLASS_METHOD_RETURN_VAL = "refByClassName";

    @BeforeMethod
    public void initializeMocks() {
        abstrComponent = Factory.initializeComponent(AbstractComponentStub.class);
    }

    @Test
    public void testInitializedComponentNotNull() {
        assertNotNull(abstrComponent, "The initialized component can not be null!");
    }

    @Test
    public void testIsRootInitialized() {
        assertNotNull(((AbstractComponentStub) abstrComponent).getRootProxy(), "Root should be initialized!");
    }

    @Test
    public void testSettingRoot() {
        WebElement root = setRoot();
        WebElement root2 = abstrComponent.getRoot();
        assertTrue(root == root2, "Got root should be the same object as set root!");
        assertEquals(root2.getText(), ROOT_METHOD_RETURN_VAL, "Mothod invoked on got root has different return value!");
    }

    @Test
    public void testMethodInvocationOnRoot() {
        try {
            ((AbstractComponentStub) abstrComponent).invokeMethodOnRoot();
            fail("The runtime exception should be thrown, since root is not set and you are invoking a method on it!");
        } catch (RuntimeException ex) {
            // expected
        }

        setRoot();
        assertEquals(((AbstractComponentStub) abstrComponent).invokeMethodOnRoot(), ROOT_METHOD_RETURN_VAL,
            "The return value of method invoked on root element is wrong!");
    }

    @Test
    public void testMethodInvocationOnReferencedElement() {
        try {
            ((AbstractComponentStub) abstrComponent).invokeMethodOnElementRefByClass();
            fail("The RuntimeException should be thrown, since you have invoked method on element which referenced from root, and you have not set the root!");
        } catch (RuntimeException ex) {
            // OK
        }
        WebElement root = setRoot();
        WebElement elemByClass = Mockito.mock(WebElement.class);
        when(elemByClass.getText()).thenReturn(REF_BY_CLASS_METHOD_RETURN_VAL);
        when(root.findElement(By.className(anyString()))).thenReturn(elemByClass);
//        when(root.findElement(Mockito.any(By.class))).thenAnswer(new Answer<WebElement>() {
//            @Override
//            public WebElement answer(InvocationOnMock invocation) throws Throwable {
//                invocation.getArguments()[1];
//            }
//        })

        
        assertEquals(((AbstractComponentStub) abstrComponent).invokeMethodOnElementRefByClass(),
            REF_BY_CLASS_METHOD_RETURN_VAL, "The method onvoked on referenced element returned wrong value!");
    }

    private WebElement setRoot() {
        WebElement root = Mockito.mock(WebElement.class);
        when(root.getText()).thenReturn(ROOT_METHOD_RETURN_VAL);

        abstrComponent.setRoot(root);
        return root;
    }

}
