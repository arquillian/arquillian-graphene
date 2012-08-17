package org.jboss.arquillian.graphene.spi.components.table;

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
 * @author jhuska
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