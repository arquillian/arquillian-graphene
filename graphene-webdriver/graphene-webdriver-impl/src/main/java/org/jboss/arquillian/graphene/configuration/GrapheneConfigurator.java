/**
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
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
package org.jboss.arquillian.graphene.configuration;

import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.config.descriptor.api.ExtensionDef;
import org.jboss.arquillian.core.api.Event;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.graphene.context.GrapheneConfigurationContext;
import org.jboss.arquillian.graphene.spi.event.GrapheneConfigured;
import org.jboss.arquillian.graphene.spi.event.GrapheneUnconfigured;
import org.jboss.arquillian.test.spi.annotation.SuiteScoped;
import org.jboss.arquillian.test.spi.event.suite.AfterClass;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.jboss.logging.Logger;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 * @author <a href="mailto:kpiwko@redhat.com">Karel Piwko</a>
 */
public class GrapheneConfigurator {

    private static final Logger LOGGER = Logger.getLogger(GrapheneConfigurator.class);

    @Inject
    @SuiteScoped
    private InstanceProducer<GrapheneConfiguration> configuration;

    @Inject
    private Event<GrapheneConfigured> configuredEvent;

    @Inject
    private Event<GrapheneUnconfigured> unconfiguredEvent;

    public void configureGraphene(@Observes BeforeClass event, ArquillianDescriptor descriptor) {
        GrapheneConfiguration c = new GrapheneConfiguration();
        this.configuration.set(c);
        map(descriptor, c, "graphene");
        mapFromSystemProperties(c, "graphene");
        this.configuredEvent.fire(new GrapheneConfigured());
        GrapheneConfigurationContext.set(c);
    }

    public void unconfigureGraphene(@Observes AfterClass event) {
        GrapheneConfigurationContext.reset();
        unconfiguredEvent.fire(new GrapheneUnconfigured());
    }

    private static void mapFromSystemProperties(Object target, String descriptorQualifier) {
        String prefix = new StringBuilder("arquillian.").append(descriptorQualifier).append(".").toString();
        Map<String, Field> fields = SecurityActions.getAccessableFields(target.getClass());
        Map<String, String> candidates = SecurityActions.getProperties(prefix);
        for (Entry<String, String> candidate: candidates.entrySet()) {
            String propertyName = keyTransformReverse(candidate.getKey().substring(prefix.isEmpty() ? 0 : prefix.length(), candidate.getKey().length()));
            set(fields, target, propertyName, candidate.getValue());
        }
    }

    private static void map(ArquillianDescriptor descriptor, Object target, String descriptorQualifier) {
        for (ExtensionDef extensionDef: descriptor.getExtensions()) {
            if (extensionDef.getExtensionName().equals(descriptorQualifier)) {
                map(extensionDef, target);
                return;
            }
        }
    }

    private static void map(ExtensionDef descriptor, Object target) {
        Map<String, Field> fields = SecurityActions.getAccessableFields(target.getClass());
        for (Entry<String, String> candidate: descriptor.getExtensionProperties().entrySet()) {
            String propertyName = keyTransformReverse(candidate.getKey());
            set(fields, target, propertyName, candidate.getValue());
        }
    }

    private static void set(Map<String, Field> fields, Object target, String propertyName, String propertyValue) {
        Field field = fields.get(propertyName);
        if (field == null) {
            LOGGER.warn("You are trying to set '" + propertyName + "' property, but there is no property with this name in Graphene configuration.");
        } else if (field.getAnnotation(Deprecated.class) != null) {
            LOGGER.warn("The property called '" + propertyName + "' is deprecated in Graphene configuration.");
        } else {
            try {
                field.set(target, convert(box(field.getType()), propertyValue));
            } catch (Exception e) {
                throw new IllegalStateException("Can't map Graphene configuration for " + target.getClass().getName() + ".");
            }
        }
    }

    /**
     * Maps a property to a field name.
     *
     * Replaces dot ('.') and lower case character with an upper case character
     *
     * @param propertyName The name of field
     * @return Corresponding field name
     */
    private static String keyTransformReverse(String propertyName) {
        StringBuilder sb = new StringBuilder();

        boolean upperCaseFlag = false;
        for (int i = 0; i < propertyName.length(); i++) {
            char c = propertyName.charAt(i);
            if (c == '.') {
                upperCaseFlag = true;
            } else if (upperCaseFlag && Character.isLowerCase(c)) {
                sb.append(Character.toUpperCase(c));
                upperCaseFlag = false;
            } else {
                sb.append(c);
            }
        }

        return sb.toString();
    }

    /**
     * A helper boxing method. Returns boxed class for a primitive class
     *
     * @param primitive A primitive class
     * @return Boxed class if class was primitive, unchanged class in other cases
     */
    private static Class<?> box(Class<?> primitive) {
        if (!primitive.isPrimitive()) {
            return primitive;
        }

        if (int.class.equals(primitive)) {
            return Integer.class;
        } else if (long.class.equals(primitive)) {
            return Long.class;
        } else if (float.class.equals(primitive)) {
            return Float.class;
        } else if (double.class.equals(primitive)) {
            return Double.class;
        } else if (short.class.equals(primitive)) {
            return Short.class;
        } else if (boolean.class.equals(primitive)) {
            return Boolean.class;
        } else if (char.class.equals(primitive)) {
            return Character.class;
        } else if (byte.class.equals(primitive)) {
            return Byte.class;
        }

        throw new IllegalArgumentException("Unknown primitive type " + primitive);
    }

    /**
     * A helper converting method.
     *
     * Converts string to a class of given type
     *
     * @param <T> Type of returned value
     * @param clazz Type of desired value
     * @param value String value to be converted
     * @return Value converted to a appropriate type
     */
    private static <T> T convert(Class<T> clazz, String value) {
        if (String.class.equals(clazz)) {
            return clazz.cast(value);
        } else if (Integer.class.equals(clazz)) {
            return clazz.cast(Integer.valueOf(value));
        } else if (Double.class.equals(clazz)) {
            return clazz.cast(Double.valueOf(value));
        } else if (Long.class.equals(clazz)) {
            return clazz.cast(Long.valueOf(value));
        } else if (Boolean.class.equals(clazz)) {
            return clazz.cast(Boolean.valueOf(value));
        } else if (URL.class.equals(clazz)) {
            try {
                return clazz.cast(new URI(value).toURL());
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException("Unable to convert value " + value + " to URL", e);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Unable to convert value " + value + " to URL", e);
            }
        } else if (URI.class.equals(clazz)) {
            try {
                return clazz.cast(new URI(value));
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("Unable to convert value " + value + " to URL", e);
            }
        }

        throw new IllegalArgumentException("Unable to convert value " + value + "to a class: " + clazz.getName());
    }

}
