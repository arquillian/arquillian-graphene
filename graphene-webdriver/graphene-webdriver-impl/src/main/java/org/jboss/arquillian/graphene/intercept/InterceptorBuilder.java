package org.jboss.arquillian.graphene.intercept;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.jboss.arquillian.graphene.GrapheneContext;

import org.jboss.arquillian.graphene.proxy.Interceptor;
import org.jboss.arquillian.graphene.proxy.InvocationContext;

/**
 * The builder for new interceptors for type-safe definition of intercepted methods.
 *
 * @author Lukas Fryc
 */
public class InterceptorBuilder {

    private Map<Method, List<Interceptor>> interceptors = new HashMap<Method, List<Interceptor>>();

    public InterceptorBuilder() {
    }

    /**
     * Returns proxy for given type which execution causes remembering what method should be intercepted by given interceptor.
     *
     * @param type the type of the generated proxy
     * @param interceptor the interceptor for intercepting methods executed on generated proxy
     * @return proxy for executing methods which shoyld be intercepted by given interceptor
     */
    public <T> T interceptInvocation(Class<T> type, final Interceptor interceptor) {
        return (T) ClassImposterizer.INSTANCE.imposterise(new MethodInterceptor() {
            @Override
            public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
                registerMethodInterceptor(method, interceptor);
                return null;
            }
        }, type);
    }

    private void registerMethodInterceptor(Method method, Interceptor interceptor) {
        List<Interceptor> list = this.interceptors.get(method);
        if (list == null) {
            list = new LinkedList<Interceptor>();
            this.interceptors.put(method, list);
        }
        list.add(interceptor);
    }

    /**
     * Builds the final version of {@link Interceptor} intercepting all methods recorded via
     * {@link #interceptInvocation(Class, Interceptor)}.
     *
     * @return the final version of {@link Interceptor}
     */
    public Interceptor build() {

        return new Interceptor() {

            final Map<Method, List<Interceptor>> interceptors = new HashMap<Method, List<Interceptor>>(
                    InterceptorBuilder.this.interceptors);

            @Override
            public Object intercept(final InvocationContext originalContext) throws Throwable {
                Collection<Interceptor> interceptors = this.interceptors.get(originalContext.getMethod());

                if (interceptors != null) {
                    final Iterator<Interceptor> iterator = interceptors.iterator();

                    if (iterator.hasNext()) {
                        Interceptor interceptor = iterator.next();

                        return interceptor.intercept(new InvocationContext() {

                            @Override
                            public Object invoke() throws Throwable {
                                if (iterator.hasNext()) {
                                    return iterator.next().intercept(this);
                                } else {
                                    return originalContext.invoke();
                                }
                            }

                            @Override
                            public Object getTarget() {
                                return originalContext.getTarget();
                            }

                            @Override
                            public Object getProxy() {
                                return originalContext.getProxy();
                            }

                            @Override
                            public Method getMethod() {
                                return originalContext.getMethod();
                            }

                            @Override
                            public Object[] getArguments() {
                                return originalContext.getArguments();
                            }

                            @Override
                            public GrapheneContext getGrapheneContext() {
                                return originalContext.getGrapheneContext();
                            }
                        });
                    }
                }

                return originalContext.invoke();
            }
        };
    }
}
