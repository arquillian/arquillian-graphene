/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.arquillian.ajocado.testng.listener;

import static org.apache.commons.lang.ArrayUtils.contains;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

/**
 * TestNG Listener able to watch configuration method and tie it's lifecycle to them (using specified method
 * dependencies, defining ordering on methods).
 * 
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
@SuppressWarnings("unchecked")
public abstract class AbstractConfigurationListener extends TestListenerAdapter implements IInvokedMethodListener {

    private static final boolean DEBUG = Boolean.valueOf(System.getProperty("selenium.debug", "false"));

    /*
     * Thread local set of configuration methods, which was already executed for current test class and separated by
     * statuses
     */
    private static ThreadLocal<Set<String>> configurationsSucceded = new StringSetLocal();
    private static ThreadLocal<Set<String>> configurationsFailed = new StringSetLocal();
    private static ThreadLocal<Set<String>> configurationsSkipped = new StringSetLocal();

    /**
     * The method names which was already executed at least once
     */
    private static ThreadLocal<Set<String>> methodsRunned = new StringSetLocal();

    /**
     * The set of configuration methods, which wasn't executed yet
     */
    private static Configurations configurations = new Configurations();

    private static class Configurations extends ThreadLocal<SortedSet<ConfigurationMethod>> {
        @Override
        protected SortedSet<ConfigurationMethod> initialValue() {
            return new TreeSet<ConfigurationMethod>();
        }
    }

    /**
     * Denotes that configuration methods type AfterMethod or BeforeMethod was already added to configuration for
     * current test method
     */
    private static ThreadLocalBoolean methodConfigurationsAdded = new ThreadLocalBoolean();

    private static class ThreadLocalBoolean extends ThreadLocal<Boolean> {
        @Override
        protected Boolean initialValue() {
            return Boolean.FALSE;
        }
    }
    
    private static ThreadLocalBoolean beforeSuiteConfigurationsAdded = new ThreadLocalBoolean();
    private static ThreadLocalBoolean afterSuiteConfigurationsAdded = new ThreadLocalBoolean();

    /**
     * Remembers the current running class to register switch to another class
     */
    private static ThreadLocal<Class<?>> lastRunnedClass = new ThreadLocal<Class<?>>();

    /*
     * Test Context information
     */
    private static ThreadLocal<ITestResult> testResult = new ThreadLocal<ITestResult>();
    private static ThreadLocal<ITestContext> testContext = new ThreadLocal<ITestContext>();

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.TestListenerAdapter#onStart(org.testng.ITestContext)
     */
    @Override
    public void onStart(ITestContext context) {
        setupContext(context);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.TestListenerAdapter#onFinish(org.testng.ITestContext)
     */
    @Override
    public void onFinish(ITestContext context) {
        setupContext(context);
        if (lastRunnedClass.get() != null) {
            onClassFinish();
        }
        if (!afterSuiteConfigurationsAdded.get()) {
            introduceMethods(AfterSuite.class);
            afterSuiteConfigurationsAdded.set(true);
        }
        invokeMethods(AfterSuite.class);
    }

    public void onSuiteStart() {
        if (!beforeSuiteConfigurationsAdded.get()) {
            introduceMethods(BeforeSuite.class);
            beforeSuiteConfigurationsAdded.set(true);
        }
        invokeMethods(BeforeSuite.class);
    }

    /**
     * Invoked before the any configuration method for given test class is invoked.
     */
    public void onClassStart() {
        onSuiteStart();
        introduceMethods(BeforeClass.class, AfterClass.class);
        invokeMethods(BeforeClass.class);
    }

    /**
     * Invoked after the all configuration methods for given test class are invoked.
     */
    public void onClassFinish() {
        invokeMethods(AfterClass.class);
        clearConfigurations();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.TestListenerAdapter#onTestStart(org.testng.ITestResult)
     */
    @Override
    public void onTestStart(ITestResult result) {
        setupContext(result);
        checkClassChange();
        if (!methodConfigurationsAdded.get()) {
            introduceMethods(BeforeMethod.class, AfterMethod.class);
            methodConfigurationsAdded.set(true);
        }
        invokeMethods(BeforeMethod.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.TestListenerAdapter#onTestFailedButWithinSuccessPercentage(org.testng.ITestResult)
     */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        setupContext(result);
        methodsRunned.get().add(result.getMethod().getMethodName());
        invokeMethods(AfterMethod.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.TestListenerAdapter#onTestFailure(org.testng.ITestResult)
     */
    @Override
    public void onTestFailure(ITestResult result) {
        setupContext(result);
        methodsRunned.get().add(result.getMethod().getMethodName());
        invokeMethods(AfterMethod.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.TestListenerAdapter#onTestSkipped(org.testng.ITestResult)
     */
    @Override
    public void onTestSkipped(ITestResult result) {
        setupContext(result);
        methodsRunned.get().add(result.getMethod().getMethodName());
        invokeMethods(AfterMethod.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.TestListenerAdapter#onTestSuccess(org.testng.ITestResult)
     */
    @Override
    public void onTestSuccess(ITestResult result) {
        setupContext(result);
        methodsRunned.get().add(result.getMethod().getMethodName());
        invokeMethods(AfterMethod.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.TestListenerAdapter#onConfigurationSuccess(org.testng.ITestResult)
     */
    @Override
    public void onConfigurationSuccess(ITestResult itr) {
        if (DEBUG) {
            System.out.println("#" + itr.getMethod().getMethodName());
        }
        configurationsSucceded.get().add(itr.getMethod().getMethodName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.TestListenerAdapter#onConfigurationFailure(org.testng.ITestResult)
     */
    @Override
    public void onConfigurationFailure(ITestResult itr) {
        if (DEBUG) {
            System.out.println("#" + itr.getMethod().getMethodName());
        }
        configurationsFailed.get().add(itr.getMethod().getMethodName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.testng.TestListenerAdapter#onConfigurationSkip(org.testng.ITestResult)
     */
    @Override
    public void onConfigurationSkip(ITestResult itr) {
        if (DEBUG) {
            System.out.println("#" + itr.getMethod().getMethodName());
        }
        configurationsSkipped.get().add(itr.getMethod().getMethodName());
    }

    private void checkClassChange() {
        if (testResult.get().getTestClass().getRealClass() != lastRunnedClass.get()) {
            if (lastRunnedClass.get() != null) {
                onClassFinish();
            }
            onClassStart();
            lastRunnedClass.set(testResult.get().getTestClass().getRealClass());
        }
    }

    /**
     * <p>
     * Invoked before each invocation of configuration or test method of test cases.
     * </p>
     * 
     * <p>
     * Satisfies, that all configuration methods, which can be executed before the invocated method are executed.
     * </p>
     */
    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        setupContext(testResult);
        checkClassChange();

        BeforeClass beforeClass = method.getTestMethod().getMethod().getAnnotation(BeforeClass.class);
        if (beforeClass != null) {
            setupContext();
            invokeMethods(BeforeClass.class);
        }
        AfterClass afterClass = method.getTestMethod().getMethod().getAnnotation(AfterClass.class);
        if (afterClass != null) {
            invokeMethods(AfterClass.class);
        }
        BeforeMethod beforeMethod = method.getTestMethod().getMethod().getAnnotation(BeforeMethod.class);
        Test testMethod = method.getTestMethod().getMethod().getAnnotation(Test.class);
        if (beforeMethod != null || testMethod != null) {
            setupContext(Reporter.getCurrentTestResult());
            if (!methodConfigurationsAdded.get()) {
                introduceMethods(BeforeMethod.class, AfterMethod.class);
                methodConfigurationsAdded.set(true);
            }
            invokeMethods(BeforeMethod.class);
        }
        AfterMethod afterMethod = method.getTestMethod().getMethod().getAnnotation(AfterMethod.class);
        if (afterMethod != null) {
            invokeMethods(AfterMethod.class);
        }
    }

    /**
     * <p>
     * Invoked after each invocation of configuration or test method of test cases.
     * </p>
     * 
     * <p>
     * Satisfies that all dependencies of this method which can be immediately executed are executed.
     * </p>
     */
    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        setupContext(testResult);
        if (method.isTestMethod()) {
            methodConfigurationsAdded.set(false);
        }
        BeforeClass beforeClass = method.getTestMethod().getMethod().getAnnotation(BeforeClass.class);
        if (beforeClass != null) {
            setupContext();
            invokeMethods(BeforeClass.class);
        }
        AfterClass afterClass = method.getTestMethod().getMethod().getAnnotation(AfterClass.class);
        if (afterClass != null) {
            invokeMethods(AfterClass.class);
        }
        BeforeMethod beforeMethod = method.getTestMethod().getMethod().getAnnotation(BeforeMethod.class);
        if (beforeMethod != null) {
            setupContext();
            invokeMethods(BeforeMethod.class);
        }
        AfterMethod afterMethod = method.getTestMethod().getMethod().getAnnotation(AfterMethod.class);
        Test testMethod = method.getTestMethod().getMethod().getAnnotation(Test.class);
        if (afterMethod != null || testMethod != null) {
            invokeMethods(AfterMethod.class);
        }
    }

    /**
     * Invokes method for given annotations.
     * 
     * @param typesToInvoke
     *            the array of annotation, whose methods should be executed
     */
    private void invokeMethods(Class<? extends Annotation>... typesToInvoke) {
        SortedSet<ConfigurationMethod> configurationsToRemove = new TreeSet<ConfigurationMethod>();
        for (ConfigurationMethod configuration : configurations.get()) {
            if (tryInvoke(configuration.method, configuration.annotation, typesToInvoke)) {
                configurationsToRemove.add(configuration);
            }
        }
        for (ConfigurationMethod configuration : configurationsToRemove) {
            configurations.get().remove(configuration);
        }
    }

    /**
     * <p>
     * Checks the conditions for executing given method.
     * </p>
     * 
     * <p>
     * If all conditions for performing are satisfied, the method is invoked.
     * </p>
     * 
     * @param method
     *            the annotated method which should be checked and possibly executed
     * @param annotation
     *            the annotation assigned to method; specifies the place in lifecycle where the method should be
     *            executed
     * @param typesToInvoke
     *            the array of annotations which should be executed
     * @return true if the method was invoked; false otherwise
     */
    private boolean tryInvoke(Method method, Annotation annotation, Class<? extends Annotation>[] typesToInvoke) {
        Class<? extends Annotation> type = annotation.annotationType();
        if (!contains(typesToInvoke, annotation.annotationType())) {
            return false;
        }
        boolean invoke = true;
        // verify dependencies of current method
        for (String dependency : getMethodDependencies(annotation)) {
            if (getMethodAlwaysRun(annotation)) {
                invoke &= configurationsSucceded.get().contains(dependency)
                    || configurationsSkipped.get().contains(dependency)
                    || configurationsFailed.get().contains(dependency);
            } else {
                invoke &= configurationsSucceded.get().contains(dependency);
            }
        }
        // verify the success of the method
        invoke &= !((AfterMethod.class == type) && (!testResult.get().isSuccess()) && (!getMethodAlwaysRun(annotation)));
        // should test pass regarding to previous verifications?
        if (invoke) {
            invokeMethod(method);
            return true;
        }

        return false;
    }

    /**
     * Invokes the given method, possibly injecting the current context information as parameter.
     * 
     * @param method
     *            the method which should be invoked
     */
    private void invokeMethod(Method method) {
        Object[] parameters = new Object[method.getParameterTypes().length];
        for (int i = 0; i < method.getParameterTypes().length; i++) {
            Class<?> parameterType = method.getParameterTypes()[i];
            if (parameterType == ITestResult.class) {
                parameters[i] = testResult;
            } else if (parameterType == ITestContext.class) {
                parameters[i] = testContext;
            } else {
                throw new IllegalArgumentException();
            }
        }

        try {
            if (DEBUG) {
                System.out.println("%" + method.getName());
            }
            method.invoke(this, parameters);
            configurationsSucceded.get().add(method.getName());
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (InvocationTargetException e) {
            e.getCause().printStackTrace();
            configurationsFailed.get().add(method.getName());
        }
    }

    /**
     * <p>
     * Shortcut for setting the current context up.
     * </p>
     * 
     * <p>
     * From the array of context parameters the particular object are selected and used to setup context.
     * </p>
     * 
     * @param contextParams
     */
    private void setupContext(Object... contextParams) {
        testResult.set(null);

        for (Object contextParam : contextParams) {
            if (contextParam instanceof ITestResult) {
                testResult.set((ITestResult) contextParam);
            } else if (contextParam instanceof ITestContext) {
                testContext.set((ITestContext) contextParam);
            }
        }
    }

    /**
     * <p>
     * Clears the configuration for the last tested class, preparing to serve for other class.
     * </p>
     * 
     * <p>
     * Should be called after the invocation all methods in last class.
     * </p>
     */
    private void clearConfigurations() {
        configurationsSucceded.get().clear();
        configurationsFailed.get().clear();
        configurationsSkipped.get().clear();
        methodsRunned.get().clear();
        configurations.get().clear();
        methodConfigurationsAdded.set(false);
    }

    /**
     * Introduces the configuration of methods which should be executed for current tested class.
     */
    private void introduceMethods(Class<? extends Annotation>... annotations) {
        for (Method method : this.getClass().getMethods()) {
            for (Class<? extends Annotation> annotationType : annotations) {
                Annotation annotation = method.getAnnotation(annotationType);
                introduceAnnotatedMethod(method, annotation);
            }
        }
    }

    /**
     * Introduces one method together with associated annotation.
     */
    private void introduceAnnotatedMethod(Method method, Annotation annotation) {
        if (annotation != null) {
            ConfigurationMethod configuration = new ConfigurationMethod(method, annotation);
            configurations.get().add(configuration);
        }
    }

    /**
     * Properties on TestNG configuration methods we want to access
     */
    private static enum ConfigurationProperty {
        alwaysRun, dependsOnMethods
    }

    /**
     * Get the list of dependencies (method names) which the given annotation specifies.
     * 
     * @param annotation
     *            the annotation for what we want to get dependencies
     * @return the list of dependencies (method names)
     */
    private String[] getMethodDependencies(Annotation annotation) {
        return (String[]) getMethodProperty(annotation, ConfigurationProperty.dependsOnMethods);
    }

    /**
     * Get true if the given annotation specifies, that method should be always run for such configuration.
     * 
     * @param annotation
     *            the annotation for what we want to get if it should be always executed
     * @return true if the given annotation specifies configuration, which should be always executed; false otherwise
     */
    private boolean getMethodAlwaysRun(Annotation annotation) {
        return (Boolean) getMethodProperty(annotation, ConfigurationProperty.alwaysRun);
    }

    /**
     * Helper for obtaining the property of annotation.
     */
    private Object getMethodProperty(Annotation annotation, ConfigurationProperty configurationProperty) {
        try {
            return annotation.getClass().getMethod(configurationProperty.toString()).invoke(annotation);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException(e);
        } catch (SecurityException e) {
            throw new IllegalStateException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Implementation of thread local Set of Strings
     */
    private static class StringSetLocal extends ThreadLocal<Set<String>> {
        @Override
        protected Set<String> initialValue() {
            return new TreeSet<String>();
        }
    }

    /**
     * Encapsulates configuration method and one it's annotation.
     */
    private class ConfigurationMethod implements Comparable<ConfigurationMethod> {
        private Method method;
        private Annotation annotation;

        public ConfigurationMethod(Method method, Annotation annotation) {
            super();
            this.method = method;
            this.annotation = annotation;
        }

        public int compareTo(ConfigurationMethod o) {
            int result = comparator.compare(this.annotation.annotationType(), o.annotation.annotationType());
            if (result != 0) {
                return result;
            }
            if (contains(getMethodDependencies(annotation), o.method.getName())) {
                // this depends on o
                return +1;
            }
            if (contains(getMethodDependencies(o.annotation), method.getName())) {
                // o depends on this
                if (result != 0) {
                    throw new IllegalStateException("Cyclic dependency found");
                }
                return -1;
            }
            if (result == 0) {
                result = method.getName().compareTo(o.method.getName());
            }
            if (result == 0) {
                result = annotation.annotationType().getCanonicalName()
                    .compareTo(o.annotation.annotationType().getCanonicalName());
            }
            return result;
        }

        @Override
        public String toString() {
            return method.getName() + " (" + annotation.annotationType().getSimpleName() + ")";
        }
    }

    private Comparator<Class<?>> comparator = new Comparator<Class<?>>() {
        @Override
        public int compare(Class<?> o1, Class<?> o2) {
            if (o1 == o2) {
                return 0;
            }
            for (Class<?> type : configurationOrder) {
                if (o1 == type) {
                    return -1;
                } else if (o2 == type) {
                    return +1;
                }
            }
            throw new IllegalStateException();
        }
    };

    private static final Class<?>[] configurationOrder = new Class[] { BeforeSuite.class, BeforeClass.class,
            BeforeMethod.class, AfterMethod.class, AfterClass.class, AfterSuite.class };
}
