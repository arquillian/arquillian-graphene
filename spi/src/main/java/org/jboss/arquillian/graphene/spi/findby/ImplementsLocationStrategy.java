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
package org.jboss.arquillian.graphene.spi.findby;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;

/**
 * <p>Enables to introduce new annotations of {@link FindBy} type with its own location strategy.</p>
 *
 * <p>E.g.: framework specific location strategies, extended grammars for well-known strategies, etc.</p>
 *
 * <p>Usage:</p>
 *
 * <pre>
 * &#64;Retention(RetentionPolicy.RUNTIME)
 * &#64;Target(ElementType.FIELD)
 * &#64;ImplementsLocationStrategy(by = XYZLocationStrategy.class)
 * public &#64;interface FindByXYZ {
 *     String value();
 * }
 * </pre>
 *
 * <pre>
 * public static class XYZLocationStrategy implements {@link LocationStrategy} {
 *     public {@link By} fromAnnotation({@link Annotation} annotation) {
 *         return ...;
 *     }
 * }
 * </pre>
 *
 * @author Lukas Fryc
 *
 * @see LocationStrategy
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface ImplementsLocationStrategy {

    Class<? extends LocationStrategy> value();

}
