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
 * <p>
 * Specifies that given page object should be opened before the test is executed.
 * </p>
 *
 * <p>
 * It instantiates the page object in same way as {@link Page} annotation.
 * </p>
 *
 * <h4>Class-level injection</h4>
 *
 * <pre>
 *
 * &#064;InitialPage
 * LoginPage loginPage;
 *
 * &#064;Test
 * public void should_login_successfully() {
 *
 *     loginPage.login(USER_NAME, USER_PASSWORD);
 *     homePage.assertOnHomePage();
 *
 *     assertTrue(homePage.getUserName(), USER_NAME);
 * }
 * </pre>
 *
 * <h4>Method-level injection</h4>
 *
 * <pre>
 * &#064;Test
 * public void should_login_successfully(@InitialPage LoginPage loginPage) {
 *
 *     loginPage.login(USER_NAME, USER_PASSWORD);
 *     homePage.assertOnHomePage();
 *
 *     assertTrue(homePage.getUserName(), USER_NAME);
 * }
 * </pre>
 *
 * @author Lukas Fryc
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER })
public @interface InitialPage {

}
