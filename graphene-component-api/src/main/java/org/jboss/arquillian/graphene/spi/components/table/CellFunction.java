package org.jboss.arquillian.graphene.spi.components.table;

/**
 * 
 * @author jhuska
 * 
 */
public interface CellFunction<T> {

    boolean accept(Cell<T> cell);
}