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
package org.jboss.arquillian.graphene.proxy;

import java.lang.reflect.*;
import java.util.*;

import net.sf.cglib.core.*;
import net.sf.cglib.proxy.*;

import org.objenesis.*;

/**
 * Thanks to jMock guys for this handy class that wraps all the cglib magic.
 * In particular it workarounds a cglib limitation by allowing to proxy a class even if the misses a no args constructor.
 *
 * @author Mario Fusco
 * @author Sebastian Jancke
 */
@SuppressWarnings("rawtypes")
final class ClassImposterizer  {

    static final ClassImposterizer INSTANCE = new ClassImposterizer();

    private ClassImposterizer() {}

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

    <T> T imposterise(Callback callback, Class<T> mockedType, Class<?>... ancillaryTypes) {
        setConstructorsAccessible(mockedType, true);
        Class<?> proxyClass = createProxyClass(mockedType, ancillaryTypes);
        return mockedType.cast(createProxy(proxyClass, callback));
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