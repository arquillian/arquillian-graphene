package org.jboss.arquillian.graphene.spi.components.checkbox;

public interface CheckboxComponent {

    void check();

    void uncheck();

    boolean isChecked();
}
