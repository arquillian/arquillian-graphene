/**
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc. and individual contributors
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.graphene.context.GrapheneContext;
import org.jboss.arquillian.graphene.spi.annotations.Location;
import org.jboss.arquillian.graphene.spi.enricher.SearchContextTestEnricher;
import org.jboss.arquillian.test.spi.TestEnricher;
import org.openqa.selenium.WebDriver;

/**
 * Graphene enricher calls all {@link SearchContextTestEnricher} instances to start
 * their enrichment.
 *
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class GrapheneEnricher implements TestEnricher {

    @Inject
    private Instance<ServiceLoader> serviceLoader;
    
    @Override
    public void enrich(Object o) {
        final WebDriver driver = GrapheneContext.getProxy();
        for (SearchContextTestEnricher enricher : serviceLoader.get().all(SearchContextTestEnricher.class)) {
            enricher.enrich(driver, o);
        }
    }

    @Override
    public Object[] resolve(Method method) {
        processLocationAnnotation(method);

        return new Object[method.getParameterTypes().length];
    }

    /**
     * If method contains </code>@Location</code> over any of the parameters, it will open the page in the browser.
     * @param method
     * @throws IllegalArgumentException when the <code>@Location</code> annotation over parameter 
     * type is either empty or it points to the non existing URL or together with <code>contextRoot</code> 
     * returned by Arquillian an <code>MalformedURLException</code>.     
    */
    private void processLocationAnnotation(Method method) {
        //on the index of Location annotation will be stored also the parameter type
        int index = getIndexOfParameterWithAnnotation(Location.class, method);

        Class<?> parameterType = method.getParameterTypes()[index];
        String locationValue = parameterType.getAnnotation(Location.class).value();
        if (locationValue.length() == 0) {
            throw new IllegalArgumentException("The @Location value over: " + parameterType
                    + " should be a URL of the page which the page object represents. Can not be empty!");
        }
        URL contextRoot = null; //need to get contextRoot somehow
        if (contextRoot == null) {
            //there is no deployment
            URL url = this.getClass().getClassLoader().getResource(locationValue);
            if (url == null) {
                throw new IllegalArgumentException("Graphene is trying to open: " + locationValue
                        + " as a local resource as it seems that you are not deploying on any container and "
                        + "the resource does not exist. Check out the @Location value over: " + parameterType);
            }
            GrapheneContext.getProxy().get(url.toExternalForm());
        } else {
            try {
                GrapheneContext.getProxy().get(new URL(contextRoot, locationValue).toExternalForm());
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Graphene is trying to open: " + contextRoot.toExternalForm()
                        + locationValue + ". However there was an error! Check out @Location value over: " + parameterType, 
                        e);
            }
        }
    }

    /**
     * Returns the index of the first parameter which contains the <code>annotation</code>
     * @param annotation
     * @param method
     * @return
     */
    private int getIndexOfParameterWithAnnotation(Class<? extends Annotation> annotation, Method method) {
        Annotation[][] annotationsOfAllParameters = method.getParameterAnnotations();

        int result = 0;
        boolean founded = false;
        for (; result < annotationsOfAllParameters.length; result++) {
            for (int j = 0; j < annotationsOfAllParameters[result].length; j++) {
                if (annotationsOfAllParameters[result][j].annotationType().equals(annotation)) {
                    founded = true;
                    break;
                }
            }
            if (founded) {
                break;
            }
        }
        if (!founded) {
            //the method has no parameters with Location annotation
            return -1;
        }

        return result;
    }
}
