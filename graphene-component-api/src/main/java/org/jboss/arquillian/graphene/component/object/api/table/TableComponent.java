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

import org.jboss.arquillian.graphene.component.object.api.scrolling.DataScrollerComponent;

/**
 * <p>
 * DataTableComponent represents a table.
 * </p>
 * <p>
 * It consist from cells, which are joined into rows and columns.
 * </p>
 * 
 * 
 * 
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * 
 */
public interface TableComponent {

    /**
     * Associates this data table with a given data scroller
     * 
     * @param scroller the scroller to associate this table with
     */
    void setDateScroller(DataScrollerComponent scroller);

    /**
     * <p>
     * Returns the total number of rows in this particular table.
     * </p>
     * <p>
     * The <code>rowspan</code> html atribute is not considered, in other words the row with <code>rowspan</code> equals 2 is
     * considered as one row.
     * </p>
     * 
     * @return
     */
    int getNumberOfRows();

    /**
     * <p>
     * Returns total number of cells in this particular table.
     * </p>
     * 
     * @return
     */
    int getNumberOfCells();

    <T> List<Cell<T>> findCells(CellFunction<T> function);

    List<Row> findRow(RowFunction function);

    <T> List<Column<T>> findColumns(ColumnFunction<T> function);

    /**
     * <p>
     * Returns the total number of columns in this particular table.
     * </p>
     * <p>
     * The <code>colspan</code> html atribute is not considered, in other words the column with <code>colspan</code> equals 2 is
     * considered as one column.
     * </p>
     * 
     * @return
     */
    int getNumberOfColumns();

    /**
     * Returns the particular cell, the cell with coordinations determined by given row and column.
     * 
     * @param row
     * @param column
     * @return
     */
    <T> Cell<T> getCell(Row row, Column<T> column);

    /**
     * Returns the list of all header cells, in other words the whole table header.
     * 
     * @return
     */
    Header getTableHeader();

    /**
     * Returns the list of all footer cells, in other words the whole table footer.
     * 
     * @return
     */
    Footer getTableFooter();

    /**
     * 
     * @return
     */
    List<Row> getAllRows();

    /**
     * Returns the first row of the table, the header row if available, is not counted.
     * 
     * @return
     */
    Row getFirstRow();

    /**
     * Returns the last row of the table, the footer row if available, is not counted.
     * 
     * @return
     */
    Row getLastRow();

    /**
     * <p>
     * Returns the row with the order determined by given param <code>order</code>.
     * </p>
     * <p>
     * Rows are indexed from 0. The header row if available is not counted.
     * </p>
     * 
     * @param order the order of the row
     * @return the particular row, or null if it does not exist
     */
    Row getRow(int order);
}
