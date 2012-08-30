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
package org.jboss.arquillian.graphene.spi.components.table;

import java.util.List;

/**
 * <p>
 * Represents the row of the table.
 * </p>
 * <p>
 * It should be possible to determine the particular row either by the header or footer, but most often by the order number.
 * Rows are indexed from 0.
 * </p>
 * 
 * 
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * 
 */
public interface Row {

    List<Cell<?>> getAllCells();

    <T> Cell<T> getCell(Column<T> column);

    /**
     * Sets the number of columns you want to have in this particular row. It is useful for tables with lot of columns.
     * 
     * @param numberOfColumns
     */
    void setNumberOfColumns(int numberOfColumns);
}
