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

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

import static org.apache.commons.lang.ArrayUtils.contains;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

/**
 * @author <a href="mailto:lfryc@redhat.com">Lukas Fryc</a>
 * @version $Revision$
 */
@SuppressWarnings("unchecked")
public abstract class AbstractConfigurationListener extends TestListenerAdapter {

    static ThreadLocal<Set<String>> configurationsSucceded = new StringSetLocal();
    static ThreadLocal<Set<String>> configurationsFailed = new StringSetLocal();
    static ThreadLocal<Set<String>> configurationsSkipped = new StringSetLocal();
    static ThreadLocal<Set<String>> methodsRunned = new StringSetLocal();

    private static final boolean DEBUG = true;

    Map<Method, Set<Annotation>> methods = new LinkedHashMap<Method, Set<Annotation>>();
    Integer methodTotal = null;

    ITestResult testResult;
    ITestContext testContext;

    @Override
    public final void onStart(ITestContext context) {
        setupContext(context);
        introduceMethods();
        methodTotal = context.getAllTestMethods().length;
        invokeMethods(BeforeClass.class);
    }

    @Override
    public final void onFinish(ITestContext context) {
        setupContext(context);
        invokeMethods(AfterClass.class);
        clearConfigurations();
    }

    public void setupContext(Object... contextParams) {
        testContext = null;
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
        methodsRunned.add(result.getMethod().getMethodName());
        invokeMethods(AfterMethod.class);
    }

    private void invokeMethods(Class<? extends Annotation>... typesToInvoke) {
        Set<Method> methodsToRemove = new LinkedHashSet<Method>();
        for (Entry<Method, Set<Annotation>> entry : methods.entrySet()) {
            tryInvoke(entry.getKey(), entry.getValue(), typesToInvoke);
            if (entry.getValue().isEmpty()) {
                methodsToRemove.add(entry.getKey());
            }
        }
        for (Method method : methodsToRemove) {
            methods.remove(method);
        }
        if (contains(typesToInvoke, AfterMethod.class) && methodsRunned.size() == methodTotal) {
            if (testResult != null
                && testResult.getMethod().getCurrentInvocationCount() == testResult.getMethod().getInvocationCount()) {
                invokeMethods(AfterClass.class);
            }
        }
    }

    private void tryInvoke(Method method, Set<Annotation> annotations, Class<? extends Annotation>[] typesToInvoke) {
        Set<Annotation> invokedAnnotations = new LinkedHashSet<Annotation>();
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> type = annotation.annotationType();
            if (!contains(typesToInvoke, annotation.annotationType())) {
                break;
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
                if (BeforeClass.class == type || AfterClass.class == type) {
                    invokedAnnotations.add(annotation);
                }
            }
        }
        for (Annotation annotation : invokedAnnotations) {
            annotations.remove(annotation);
        }
    }

    @Override
    public final void onConfigurationSuccess(ITestResult itr) {
        if (DEBUG) {
            System.out.println("#" + itr.getMethod().getMethodName());
        }
        configurationsSucceded.get().add(itr.getMethod().getMethodName());
    }

    @Override
    public final void onConfigurationFailure(ITestResult itr) {
        if (DEBUG) {
            System.out.println("#" + itr.getMethod().getMethodName());
        }
        configurationsFailed.get().add(itr.getMethod().getMethodName());
    }

    @Override
    public final void onConfigurationSkip(ITestResult itr) {
        if (DEBUG) {
            System.out.println("#" + itr.getMethod().getMethodName());
        }
        configurationsSkipped.get().add(itr.getMethod().getMethodName());
    }

    private void clearConfigurations() {
        configurationsSucceded.get().clear();
        configurationsFailed.get().clear();
        configurationsSkipped.get().clear();
        methods.clear();
    }

    private void introduceMethods() {
        for (Method method : this.getClass().getMethods()) {
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
            if (!methods.containsKey(method)) {
                methods.put(method, new LinkedHashSet<Annotation>());
            }
            Set<Annotation> annotations = methods.get(method);
            annotations.add(annotation);
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
            e.printStackTrace();
            configurationsFailed.get().add(method.getName());
        }
    }

    static class StringSetLocal extends ThreadLocal<Set<String>> {
        @Override
        protected Set<String> initialValue() {
            return new TreeSet<String>();
        }
    }
}
