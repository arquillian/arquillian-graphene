package org.jboss.test.selenium.listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.apache.commons.lang.StringUtils;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class TestMethodSelector implements IAnnotationTransformer {

    public void transform(ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        String selectedMethod = System.getProperty("method");

        selectedMethod = StringUtils.replace(selectedMethod, "*", ".*");

        if (!testMethod.getName().matches(selectedMethod)) {
            annotation.setEnabled(false);
        }
    }
}
