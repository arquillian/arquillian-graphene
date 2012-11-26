/**
 * Thanks to Mockito guys for some modifications. This class has been further modified for use in lambdaj
 * and then modified for use in Arquillian Graphene project.
 *
 * Mockito License for redistributed, modified file.
 *
Copyright (c) 2007 Mockito contributors
This program is made available under the terms of the MIT License.
 *
 *
 * jMock License for original distributed file
 *
Copyright (c) 2000-2007, jMock.org
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list of
conditions and the following disclaimer. Redistributions in binary form must reproduce
the above copyright notice, this list of conditions and the following disclaimer in
the documentation and/or other materials provided with the distribution.

Neither the name of jMock nor the names of its contributors may be used to endorse
or promote products derived from this software without specific prior written
permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT
SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY
WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
DAMAGE.
*/
package org.jboss.arquillian.graphene.cglib;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.cglib.core.DefaultNamingPolicy;
import net.sf.cglib.core.NamingPolicy;
import net.sf.cglib.core.Predicate;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.CallbackFilter;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.Factory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.NoOp;

import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;

/**
 * Thanks to jMock guys for this handy class that wraps all the cglib magic.
 * In particular it workarounds a cglib limitation by allowing to proxy a class even if the misses a no args constructor.
 *
 * @author Mario Fusco
 * @author Sebastian Jancke
 */
@SuppressWarnings("rawtypes")
public class ClassImposterizer  {

    protected ClassImposterizer() {}

    private final Objenesis objenesis = new ObjenesisStd();

    private static final NamingPolicy DEFAULT_POLICY = new DefaultNamingPolicy() {
        /**
         * {@inheritDoc}
         */
        @Override
        protected String getTag() {
            return "CGLIB";
        }
    };

    private static final NamingPolicy SIGNED_CLASSES_POLICY = new DefaultNamingPolicy() {
        /**
         * {@inheritDoc}
         */
        @Override
        public String getClassName(String prefix, String source, Object key, Predicate names) {
            return "codegen." + super.getClassName(prefix, source, key, names);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected String getTag() {
            return "CGLIB";
        }
    };

    private static final CallbackFilter IGNORE_BRIDGE_METHODS = new CallbackFilter() {
        public int accept(Method method) {
            return method.isBridge() ? 1 : 0;
        }
    };

    protected <T> T imposteriseProtected(MethodInterceptor interceptor, Class<?> mockedType, Class<?>... ancillaryTypes) {
        if (mockedType.isInterface()) {
            return imposteriseInterface(interceptor, mockedType, ancillaryTypes);
        } else {
            return imposteriseClass(interceptor, mockedType, ancillaryTypes);
        }
    }
    
    @SuppressWarnings("unchecked")
    protected <T> T imposteriseClass(MethodInterceptor interceptor, Class<?> mockedType, Class<?>... ancillaryTypes) {
        setConstructorsAccessible(mockedType, true);
        Class<?> proxyClass = createProxyClass(mockedType, ancillaryTypes);
        return (T) mockedType.cast(createProxy(proxyClass, interceptor));
    }

    protected <T> T imposteriseInterface(MethodInterceptor interceptor, Class<?> mockedInterface, Class<?>... ancillaryTypes) {
        
        if (!Modifier.isPublic(mockedInterface.getModifiers())) {
            throw new IllegalArgumentException("Imposterized interface must be public: " + mockedInterface);
        }
        
        List<Class<?>> list = new ArrayList<Class<?>>(Arrays.asList(ancillaryTypes));
        list.add(mockedInterface);
        
        Class<?>[] interfaces = list.toArray(new Class<?>[list.size()]);

        return imposteriseClass(interceptor, Object.class, interfaces);
    }

    private void setConstructorsAccessible(Class<?> mockedType, boolean accessible) {
        for (Constructor<?> constructor : mockedType.getDeclaredConstructors()) {
            constructor.setAccessible(accessible);
        }
    }

    private Class<?> createProxyClass(Class<?> mockedType, Class<?>...interfaces) {
        if (mockedType == Object.class) mockedType = ClassWithSuperclassToWorkAroundCglibBug.class;

        Enhancer enhancer = new ClassEnhancer();
        enhancer.setUseFactory(true);
        enhancer.setSuperclass(mockedType);
        enhancer.setInterfaces(interfaces);

        enhancer.setCallbackTypes(new Class[]{MethodInterceptor.class, NoOp.class});
        enhancer.setCallbackFilter(IGNORE_BRIDGE_METHODS);
        enhancer.setNamingPolicy(mockedType.getSigners() != null ? SIGNED_CLASSES_POLICY : DEFAULT_POLICY);

        return enhancer.createClass();
    }

    private static class ClassEnhancer extends Enhancer {
        /**
         * {@inheritDoc}
         */
        @Override
        protected void filterConstructors(Class sc, List constructors) { }
    }

    private Object createProxy(Class<?> proxyClass, Callback callback) {
        Factory proxy = (Factory) objenesis.newInstance(proxyClass);
        proxy.setCallbacks(new Callback[] {callback, NoOp.INSTANCE});
        return proxy;
    }

    /**
     * Class With Superclass To WorkAround Cglib Bug
     */
    public static class ClassWithSuperclassToWorkAroundCglibBug {}
}