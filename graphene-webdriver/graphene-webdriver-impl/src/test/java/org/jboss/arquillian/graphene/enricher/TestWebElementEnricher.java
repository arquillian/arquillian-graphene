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

import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.configuration.GrapheneConfiguration;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestWebElementEnricher extends AbstractGrapheneEnricherTest {

    @Mock
    WebDriver driver;

    @Mock
    WebElement element;

    @Test
    public void generated_webelement_implements_WrapsElement_interface() {
        TestPage page = new TestPage();
        getGrapheneEnricher().enrich(page);

        assertTrue(page.element instanceof WrapsElement);

        GrapheneContext.setContextFor(new GrapheneConfiguration(), driver, Default.class);
        when(driver.findElement(Mockito.any(By.class))).thenReturn(element);
        WebElement wrappedElement = ((WrapsElement) page.element).getWrappedElement();
        GrapheneContext.removeContextFor(Default.class);

        assertEquals(element, wrappedElement);
    }

    public static class TestPage {
        @FindBy(id = "id")
        private WebElement element;
    }

}
