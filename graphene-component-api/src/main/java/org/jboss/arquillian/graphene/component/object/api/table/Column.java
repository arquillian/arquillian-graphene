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
package org.jboss.arquillian.graphene.component.object.api.table;

import java.util.List;

/**
 * <p>
 * Represents the column of the table.
 * </p>
 * <p>
 * It should be possible to determine the particular column either by the header or footer, but most often by the order number.
 * Columns are indexed from 0.
 * </p>
 * 
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * 
 */
public interface Column<T> {

    List<Cell<T>> getAllCells();

    int getNumberOfCells();

    /**
     * <p>
     * Sets the number of rows you want to have in this particular column. It is useful for tables with lot of rows.
     * </p>
     * 
     * @param numberOfRows
     */
    void setNumberOfRows(int numberOfRows);
}