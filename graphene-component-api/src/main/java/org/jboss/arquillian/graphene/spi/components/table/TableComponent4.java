package org.jboss.arquillian.graphene.spi.components.table;

public interface TableComponent4<A, B, C, D> extends TableComponent {

    Column<A> getColumn1();

    Column<B> getColumn2();

    Column<C> getColumn3();

    Column<D> getColumn4();

}
