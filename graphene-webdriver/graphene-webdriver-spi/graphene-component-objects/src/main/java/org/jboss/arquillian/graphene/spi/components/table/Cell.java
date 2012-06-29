package org.jboss.arquillian.graphene.spi.components.table;

import org.jboss.arquillian.graphene.spi.components.common.ComponentsContainer;

/**
 * Represents one particular cell of the table.
 * 
 * @author jhuska
 */
public interface Cell<T> extends ComponentsContainer<T> {

	Row whichRow();

	Column<T> whichColumn();
}