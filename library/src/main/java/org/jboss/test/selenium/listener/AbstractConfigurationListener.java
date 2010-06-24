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
package org.jboss.test.selenium.listener;

import static org.apache.commons.lang.ArrayUtils.contains;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
@SuppressWarnings("unchecked")
public abstract class AbstractConfigurationListener extends TestListenerAdapter implements IInvokedMethodListener {

    static ThreadLocal<Set<String>> configurationsSucceded = new StringSetLocal();
    static ThreadLocal<Set<String>> configurationsFailed = new StringSetLocal();
    static ThreadLocal<Set<String>> configurationsSkipped = new StringSetLocal();
    static ThreadLocal<Set<String>> methodsRunned = new StringSetLocal();

    private static final boolean DEBUG = true;

    SortedSet<Configuration> configurations = new TreeSet<Configuration>();
    SortedSet<Configuration> methodConfigurations = new TreeSet<Configuration>();
    boolean methodConfigurationsAdded = false;

    Class lastRunnedClass = null;

    ITestResult testResult;
    ITestContext testContext;

    @Override
    public final void onStart(ITestContext context) {
        setupContext(context);
        introduceMethods();
        invokeMethods(BeforeSuite.class);
    }

    @Override
    public final void onFinish(ITestContext context) {
        setupContext(context);
        if (lastRunnedClass != null) {
            onClassFinish();
        }
        invokeMethods(AfterSuite.class);
    }

    public void onClassStart() {
        introduceMethods();
        invokeMethods(BeforeClass.class);
    }

    public void onClassFinish() {
        invokeMethods(AfterClass.class);
        clearConfigurations();
    }

    public void setupContext(Object... contextParams) {
        testResult = null;

        for (Object contextParam : contextParams) {
            if (contextParam instanceof ITestResult) {
                testResult = (ITestResult) contextParam;
            } else if (contextParam instanceof ITestContext) {
                testContext = (ITestContext) contextParam;
            }
        }
    }

    @Override
    public void onTestStart(ITestResult result) {
        setupContext(result);
        if (methodsRunned.get().size() == 0) {
            invokeMethods(BeforeClass.class);
        }
        if (!methodConfigurationsAdded) {
            configurations.addAll(methodConfigurations);
            methodConfigurationsAdded = true;
        }
        invokeMethods(BeforeMethod.class);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        setupContext(result);
        methodsRunned.get().add(result.getMethod().getMethodName());
        invokeMethods(AfterMethod.class);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        setupContext(result);
        methodsRunned.get().add(result.getMethod().getMethodName());
        invokeMethods(AfterMethod.class);
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        setupContext(result);
        methodsRunned.get().add(result.getMethod().getMethodName());
        invokeMethods(AfterMethod.class);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        setupContext(result);
        methodsRunned.get().add(result.getMethod().getMethodName());
        invokeMethods(AfterMethod.class);
    }

    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
        if (testResult.getTestClass().getRealClass() != lastRunnedClass) {
            if (lastRunnedClass != null) {
                onClassFinish();
            }
            onClassStart();
            lastRunnedClass = testResult.getTestClass().getRealClass();
        }

        BeforeClass beforeClass = method.getTestMethod().getMethod().getAnnotation(BeforeClass.class);
        if (beforeClass != null) {
            setupContext(testResult);
            invokeMethods(BeforeClass.class);
        }
        AfterClass afterClass = method.getTestMethod().getMethod().getAnnotation(AfterClass.class);
        if (afterClass != null) {
            setupContext(testResult);
            invokeMethods(AfterClass.class);
        }
        BeforeMethod beforeMethod = method.getTestMethod().getMethod().getAnnotation(BeforeMethod.class);
        if (beforeMethod != null) {
            setupContext(testResult);
            if (!methodConfigurationsAdded) {
                configurations.addAll(methodConfigurations);
                methodConfigurationsAdded = true;
            }
            invokeMethods(BeforeMethod.class);
        }
        AfterMethod afterMethod = method.getTestMethod().getMethod().getAnnotation(AfterMethod.class);
        if (afterMethod != null) {
            setupContext(testResult);
            invokeMethods(AfterMethod.class);
        }
    }

    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        if (method.isTestMethod()) {
            methodConfigurationsAdded = false;
        }
        BeforeClass beforeClass = method.getTestMethod().getMethod().getAnnotation(BeforeClass.class);
        if (beforeClass != null) {
            setupContext(testResult);
            invokeMethods(BeforeClass.class);
        }
        AfterClass afterClass = method.getTestMethod().getMethod().getAnnotation(AfterClass.class);
        if (afterClass != null) {
            setupContext(testResult);
            invokeMethods(AfterClass.class);
        }
        BeforeMethod beforeMethod = method.getTestMethod().getMethod().getAnnotation(BeforeMethod.class);
        if (beforeMethod != null) {
            setupContext(testResult);
            invokeMethods(BeforeMethod.class);
        }
        AfterMethod afterMethod = method.getTestMethod().getMethod().getAnnotation(AfterMethod.class);
        if (afterMethod != null) {
            setupContext(testResult);
            invokeMethods(AfterMethod.class);
        }
    }

    protected void invokeMethods(Class<? extends Annotation>... typesToInvoke) {
        SortedSet<Configuration> configurationsToRemove = new TreeSet<Configuration>();
        for (Configuration configuration : configurations) {
            if (tryInvoke(configuration.method, configuration.annotation, typesToInvoke)) {
                configurationsToRemove.add(configuration);
            }
        }
        for (Configuration configuration : configurationsToRemove) {
            configurations.remove(configuration);
        }
    }

    protected boolean tryInvoke(Method method, Annotation annotation, Class<? extends Annotation>[] typesToInvoke) {
        Class<? extends Annotation> type = annotation.annotationType();
        if (!contains(typesToInvoke, annotation.annotationType())) {
            return false;
        }
        boolean invoke = true;
        // verify dependencies of current method
        for (String dependency : getMethodDependencies(annotation)) {
            if (getMethodAlwaysRun(annotation)) {
                invoke &=
                    configurationsSucceded.get().contains(dependency)
                        || configurationsSkipped.get().contains(dependency)
                        || configurationsFailed.get().contains(dependency);
            } else {
                invoke &= configurationsSucceded.get().contains(dependency);
            }
        }
        // verify the success of the method
        invoke &= !((AfterMethod.class == type) && (!testResult.isSuccess()) && (!getMethodAlwaysRun(annotation)));
        // should test pass regarding to previous verifications?
        if (invoke) {
            invokeMethod(method);
            return true;
        }

        return false;
    }

    @Override
    public final void onConfigurationSuccess(ITestResult itr) {
        if (DEBUG) {
            System.out.println("#" + itr.getMethod().getMethodName());
        }
        configurationsSucceded.get().add(itr.getMethod().getMethodName());
        invokeAfterConfiguration(itr);
    }

    @Override
    public final void onConfigurationFailure(ITestResult itr) {
        if (DEBUG) {
            System.out.println("#" + itr.getMethod().getMethodName());
        }
        configurationsFailed.get().add(itr.getMethod().getMethodName());
        invokeAfterConfiguration(itr);
    }

    @Override
    public final void onConfigurationSkip(ITestResult itr) {
        if (DEBUG) {
            System.out.println("#" + itr.getMethod().getMethodName());
        }
        configurationsSkipped.get().add(itr.getMethod().getMethodName());
        invokeAfterConfiguration(itr);
    }

    private void invokeAfterConfiguration(ITestResult itr) {
        for (Class annotation : new Class[]{BeforeClass.class, BeforeMethod.class, AfterMethod.class, AfterClass.class}) {
            if (itr.getMethod().getMethod().getAnnotation(annotation) != null) {
                invokeMethods(annotation);
            }
        }
    }

    private void clearConfigurations() {
        configurationsSucceded.get().clear();
        configurationsFailed.get().clear();
        configurationsSkipped.get().clear();
        methodsRunned.get().clear();
        configurations.clear();
        methodConfigurations.clear();
        methodConfigurationsAdded = false;
    }

    private void introduceMethods() {
        for (Method method : this.getClass().getMethods()) {
            BeforeSuite beforeSuite = method.getAnnotation(BeforeSuite.class);
            introduceAnnotatedMethod(method, beforeSuite);
            AfterSuite afterSuite = method.getAnnotation(AfterSuite.class);
            introduceAnnotatedMethod(method, afterSuite);
            BeforeClass beforeClass = method.getAnnotation(BeforeClass.class);
            introduceAnnotatedMethod(method, beforeClass);
            AfterClass afterClass = method.getAnnotation(AfterClass.class);
            introduceAnnotatedMethod(method, afterClass);
            BeforeMethod beforeMethod = method.getAnnotation(BeforeMethod.class);
            introduceAnnotatedMethod(method, beforeMethod);
            AfterMethod afterMethod = method.getAnnotation(AfterMethod.class);
            introduceAnnotatedMethod(method, afterMethod);
        }
    }

    private void introduceAnnotatedMethod(Method method, Annotation annotation) {
        if (annotation != null) {
            Configuration configuration = new Configuration(method, annotation);
            configurations.add(configuration);
            if (annotation.annotationType() == AfterMethod.class || annotation.annotationType() == BeforeMethod.class) {
                methodConfigurations.add(configuration);
            }
            methodConfigurationsAdded = true;
        }
    }

    private static enum MethodProperty {
        alwaysRun, dependsOnMethods
    }

    private String[] getMethodDependencies(Annotation annotation) {
        return (String[]) getMethodProperty(annotation, MethodProperty.dependsOnMethods);
    }

    private boolean getMethodAlwaysRun(Annotation annotation) {
        return (Boolean) getMethodProperty(annotation, MethodProperty.alwaysRun);
    }

    private Object getMethodProperty(Annotation annotation, MethodProperty methodProperty) {
        try {
            return annotation.getClass().getMethod(methodProperty.toString()).invoke(annotation);
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

    private void invokeMethod(Method method) {
        Object[] parameters = new Object[method.getParameterTypes().length];
        for (int i = 0; i < method.getParameterTypes().length; i++) {
            Class parameterType = method.getParameterTypes()[i];
            if (parameterType == ITestResult.class) {
                parameters[i] = testResult;
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

    static class StringSetLocal extends ThreadLocal<Set<String>> {
        @Override
        protected Set<String> initialValue() {
            return new TreeSet<String>();
        }
    }

    class Configuration implements Comparable<Configuration> {
        private Method method;
        private Annotation annotation;

        public Configuration(Method method, Annotation annotation) {
            super();
            this.method = method;
            this.annotation = annotation;
        }

        public int compareTo(Configuration o) {
            int result = 0;
            if (contains(getMethodDependencies(annotation), o.method.getName())) {
                // this depends on o
                result = +1;
            }
            if (contains(getMethodDependencies(o.annotation), method.getName())) {
                // o depends on this
                if (result != 0) {
                    throw new IllegalStateException("Cyclic dependency found");
                }
                result = -1;
            }
            if (result == 0) {
                result = method.getName().compareTo(o.method.getName());
            }
            if (result == 0) {
                result =
                    annotation.annotationType().getCanonicalName().compareTo(
                        o.annotation.annotationType().getCanonicalName());
            }
            return result;
        }

        @Override
        public String toString() {
            return method.getName() + " (" + annotation.annotationType().getSimpleName() + ")";
        }
    }
}
