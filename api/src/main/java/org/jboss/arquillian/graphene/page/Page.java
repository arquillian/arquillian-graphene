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
package org.jboss.arquillian.graphene.page;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>This annotation marks the given object to be instantiated and injected as Page object</p>
 *
 * <pre>
 * public class LoginForm {
 *
 *     &#064;FindBy(css = ".login")
 *     WebElement login;
 *
 *     &#064;FindBy(css = ".password")
 *     WebElement password;
 *
 *     &#064;FindBy(css = ".confirmation")
 *     WebElement confirmation;
 *
 *     public void login(String login, String password) {
 *         ...
 *     }
 * }
 *
 * &#064;RunWith(Arquillian.class)
 * public class Test {
 *
 *     &#064;Page
 *     LoginForm loginForm;
 *
 *     &#064;Test
 *     public void test() {
 *         loginForm.login("login", "password");
 *     }
 * }
 * </pre>
 *
 * @author Juraj Huska
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Page {

}
