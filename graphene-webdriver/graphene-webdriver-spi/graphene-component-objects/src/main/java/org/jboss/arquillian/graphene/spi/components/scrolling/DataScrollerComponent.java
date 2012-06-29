package org.jboss.arquillian.graphene.spi.components.scrolling;

import org.jboss.arquillian.graphene.spi.components.common.Component;

/**
 * <p>
 * DataScrollerComponent represents a scroller attached to some data structure,
 * like to the table.
 * </p>
 * <p>
 * His purpose is to scroll over data, which is divided into multiple pages.
 * </p>
 * 
 * @author jhuska
 * 
 */
public interface DataScrollerComponent extends Component {

	/**
	 * Goes to the first page of data structure the scroller is bounded to.
	 */
	void gotoFirstPage();

	/**
	 * Goes to the last page of data structure the scroller is bounded to.
	 */
	void gotoLastPage();

	/**
	 * <p>
	 * Goes to the next page of data structure the scroller is bounded to.
	 * </p>
	 * <p>
	 * That is when it is currently on the page number 1, after invocation it
	 * will be on the page number 2.
	 * </p>
	 * 
	 * @return false if the scroller is already on the last page, true otherwise
	 */
	boolean gotoNextPage();

	/**
	 * <p>
	 * Goes to the previous page of data structure the scroller is bounded to.
	 * </p>
	 * <p>
	 * That is when it is currently on the page number 2, after invocation it
	 * will be on the page number 1.
	 * </p>
	 * 
	 * @return false if the scroller is already on the first page, true
	 *         otherwise
	 */
	boolean gotoPreviousPage();

	/**
	 * Returns the total number of pages, the data structure is divided to.
	 * 
	 * @return
	 */
	int getNumberOfPages();
}
