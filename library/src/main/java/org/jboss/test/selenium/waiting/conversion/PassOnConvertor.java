package org.jboss.test.selenium.waiting.conversion;

public class PassOnConvertor<T> implements Convertor<T, T> {
    public T forwardConversion(T object) {
        return object;
    };

    public T backwardConversion(T object) {
        return object;
    };

}
