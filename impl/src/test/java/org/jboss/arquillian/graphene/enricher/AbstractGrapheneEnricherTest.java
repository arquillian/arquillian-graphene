/**
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.enricher;

import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Arrays;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.drone.api.annotation.Default;
import org.jboss.arquillian.graphene.TestingDriver;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.configuration.GrapheneConfiguration;
import org.jboss.arquillian.graphene.spi.enricher.SearchContextTestEnricher;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractGrapheneEnricherTest {

    @Mock
    private Instance<ServiceLoader> serviceLoaderInstance;

    @Mock
    protected TestingDriver browser;

    @Mock
    private ServiceLoader serviceLoader;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private TestEnricher grapheneEnricher;
    private SearchContextTestEnricher webElementEnricher;
    private SearchContextTestEnricher pageObjectEnricher;
    private SearchContextTestEnricher pageFragmentEnricher;

    @Before
    public void prepareServiceLoader() throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        grapheneEnricher = new GrapheneEnricher();
        Instance<GrapheneConfiguration> configuration = new Instance() {
            @Override
            public Object get() {
                return new GrapheneConfiguration();
            }

        };
        webElementEnricher = new WebElementEnricher(configuration);
        pageObjectEnricher = new PageObjectEnricher();
        pageFragmentEnricher = new PageFragmentEnricher(configuration);
        when(serviceLoaderInstance.get()).thenReturn(serviceLoader);
        when(serviceLoader.all(TestEnricher.class)).thenReturn(Arrays.asList(grapheneEnricher));
        when(serviceLoader.all(SearchContextTestEnricher.class)).thenReturn(Arrays.asList(webElementEnricher, pageObjectEnricher, pageFragmentEnricher));
        for (Object o: Arrays.asList(grapheneEnricher, webElementEnricher, pageObjectEnricher, pageFragmentEnricher)) {
            Field serviceLoaderField;
            if (o instanceof SearchContextTestEnricher) {
                serviceLoaderField = AbstractSearchContextEnricher.class.getDeclaredField("serviceLoader");
            } else {
                serviceLoaderField = o.getClass().getDeclaredField("serviceLoader");
            }
            if (!serviceLoaderField.isAccessible()) {
                serviceLoaderField.setAccessible(true);
            }
            serviceLoaderField.set(o, serviceLoaderInstance);
        }

        GrapheneContext.setContextFor(new GrapheneConfiguration(), browser, Default.class);
    }

    protected final TestEnricher getGrapheneEnricher() {
        return grapheneEnricher;
    }

    protected final SearchContextTestEnricher getPageFragmentEnricher() {
        return pageFragmentEnricher;
    }

    protected final SearchContextTestEnricher getPageObjectEnricher() {
        return pageObjectEnricher;
    }

    protected final SearchContextTestEnricher getWebElementEnricher() {
        return webElementEnricher;
    }

    protected final Instance<ServiceLoader> getServiceLoader() {
        return serviceLoaderInstance;
    }

}
