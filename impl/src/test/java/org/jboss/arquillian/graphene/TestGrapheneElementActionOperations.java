/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene;

import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.interactions.Locatable;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class TestGrapheneElementActionOperations extends GrapheneActionOperationsBootstrap {

    private GrapheneElement grapheneElement;

    @Before
    public void createGrapheneElement() {
        grapheneElement = new GrapheneElementImpl(webElement);
    }

    @Test
    public void testGrapheneElementActionDoubleClick() {
        // when
        grapheneElement.doubleClick();

        // then
        verify(mouse).doubleClick(grapheneElement.getCoordinates());
        verifyNoMoreInteractions(mouse, keyboard);
    }

    @Test
    public void testGrapheneElementActionWriteIntoElement() {
        // when
        grapheneElement.writeIntoElement("hi");

        // then
        verify(mouse).mouseMove(grapheneElement.getCoordinates());
        verify(mouse).click(grapheneElement.getCoordinates());
        verify(keyboard).sendKeys("hi");
        verifyNoMoreInteractions(mouse, keyboard);
    }
}
