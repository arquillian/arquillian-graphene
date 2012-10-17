/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
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

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
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
