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

import java.lang.reflect.Method;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.graphene.spi.enricher.SearchContextTestEnricher;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.arquillian.test.spi.TestEnricher;

/**
 * Graphene enricher calls all {@link SearchContextTestEnricher} instances to start their enrichment.
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class GrapheneEnricher implements TestEnricher {

    @Inject
    private static Instance<ServiceLoader> serviceLoader;

    @ArquillianResource
    private URL contextRoot;

    @Override
    public void enrich(Object o) {
        Collection<SearchContextTestEnricher> sortedSearchContextEnrichers = AbstractSearchContextEnricher.getSortedSearchContextEnrichers(serviceLoader);
        removeFieldAccessValidatorEnricher(sortedSearchContextEnrichers);

        for (SearchContextTestEnricher enricher : sortedSearchContextEnrichers) {
            enricher.enrich(null, o);
        }
    }

    /*
    FieldAccessValidator should control only classes instantiated by Graphene, not the Test Case object
    */
    private void removeFieldAccessValidatorEnricher(Collection<SearchContextTestEnricher> searchContextEnrichers) {
        for (Iterator<SearchContextTestEnricher> iterator = searchContextEnrichers.iterator(); iterator.hasNext();) {
            if (iterator.next() instanceof FieldAccessValidatorEnricher) {
                iterator.remove();
            }
        }
    }

    @Override
    public Object[] resolve(Method method) {
        return new Object[method.getParameterTypes().length];
    }
}
