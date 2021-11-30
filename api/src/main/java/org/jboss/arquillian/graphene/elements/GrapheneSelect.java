/*
 * JBoss, Home of Professional Open Source
 * Copyright 2021, Red Hat, Inc. and individual contributors
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

package org.jboss.arquillian.graphene.elements;

import org.jboss.arquillian.graphene.spi.ImplementedBy;
import org.openqa.selenium.WrapsElement;
import org.openqa.selenium.support.ui.ISelect;

/**
 * Temporary class for GrapheneSelect elements
 * Replace with <code>{@link org.openqa.selenium.support.ui.Select}</code> after resolving issues with getDomProperty() and getDomAttribute() methods on WebDrivers
 * <p>
 * Models a SELECT tag, providing helper methods to select and deselect options.
 */
@ImplementedBy(className = "org.jboss.arquillian.graphene.elements.GrapheneSelectImpl")
public interface GrapheneSelect extends ISelect, WrapsElement {

    void setIsMulti(boolean isMulti);
}
