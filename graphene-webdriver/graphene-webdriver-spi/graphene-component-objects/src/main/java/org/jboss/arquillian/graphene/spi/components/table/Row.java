package org.jboss.arquillian.graphene.spi.components.table;

import java.util.List;

/**
 * <p>
 * Represents the row of the table.
 * </p>
 * <p>
 * It should be possible to determine the particular row either by the
 * header or footer, but most often by the order number. Rows are indexed
 * from 0.
 * </p>
 * 
 * @author jhuska
 */
public interface Row {

	List<Cell<?>> getAllCells();
	
	<T> Cell<T> getCell(Column<T> column);
	
	/**
	 * Sets the number of columns you want to have in this particular row.
	 * It is useful for tables with lot of columns.
	 * 
	 * @param numberOfColumns
	 */
	void setNumberOfColumns(int numberOfColumns);
}
