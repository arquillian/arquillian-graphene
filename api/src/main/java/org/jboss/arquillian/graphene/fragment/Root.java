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
package org.jboss.arquillian.graphene.fragment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks the <tt>WebElement</tt> field where should be injected the root of the given fragment,
 * given by <tt>&#064;FindBy</tt> annotation on a injection point.
 *
 * <pre>
 * public class AutocompletionInput {
 *
 *     &#064;Root
 *     WebElement input;
 *
 *     public void typeText(String text) {
 *         input.typeText(text);
 *     }
 *
 *
 *     public void select(String suggestion) {
 *         ...
 *     }
 * }
 *
 * &#064;RunWith(Arquillian.class)
 * public class Test {
 *
 *     &#064;FindBy(css = ".autocompletion")
 *     AutocompletionInput autocomplete;
 *
 *     &#064;Test
 *     public void test() {
 *         autocomplete.typeText("Arq");
 *         autocomplete.select("Arquillian");
 *     }
 * }
 * </pre>
 *
 * @author Juraj Huska
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Root {
}
