package org.jboss.arquillian.graphene.spi.components.common;

/**
 * It should ensure in the constructor that it is going represent either WebElement, Page object or Component.
 * 
 * @author jhuska
 * 
 */
public interface NestedElements<T> {

    T getValue();
}
