package org.jboss.test.selenium.waiting.conversion;

public interface Convertor<F, T> {
    T forwardConversion(F object);
    F backwardConversion(T object);
}
