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
package org.jboss.arquillian.graphene.spi.components.scrolling;

/**
 * <p>
 * DataScrollerComponent represents a scroller attached to some data structure, like to the table.
 * </p>
 * <p>
 * His purpose is to scroll over data, which is divided into multiple pages.
 * </p>
 * 
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * 
 */
public interface DataScrollerComponent {

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
     * That is when it is currently on the page number 1, after invocation it will be on the page number 2.
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
     * That is when it is currently on the page number 2, after invocation it will be on the page number 1.
     * </p>
     * 
     * @return false if the scroller is already on the first page, true otherwise
     */
    boolean gotoPreviousPage();

    /**
     * Returns the total number of pages, the data structure is divided to.
     * 
     * @return
     */
    int getNumberOfPages();
}
