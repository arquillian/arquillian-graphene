package org.jboss.arquillian.graphene.spi.components.table;

public interface ColumnFunction<T> {

    boolean accept(Column<T> column);
}
