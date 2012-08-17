package org.jboss.arquillian.graphene.spi.components.table;

public interface RowFunction {

    boolean accept(Row row);
}
