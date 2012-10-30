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

import org.jboss.arquillian.graphene.enricher.exception.PageObjectInitializationException;
import org.jboss.arquillian.graphene.spi.annotations.Page;
import org.junit.Test;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestExceptionHandlingPageObjectInitialization extends AbstractTest {

    private static final String ERROR_MSG_PAGE_OBJECT_WITH_ABSTRACT_TYPE = "Check whether you did not declare Page Object with abstract type!";
    private static final String ERROR_MSG_PAGE_OBJECT_WITH_MISSING_NO_ARG_CONSTRUCTOR = "Check whether declared Page Object has no argument constructor!";
    private static final String ERROR_MSG_PAGE_OBJECT_WITH_PRIVATE_CONSTRUCTOR = "Check whether declared Page Object has public no argument constructor!";

    @Test
    public void testWrongPageObjectDeclaredWithAbstractType() {
        thrown.expect(PageObjectInitializationException.class);
        thrown.expectMessage(ERROR_MSG_PAGE_OBJECT_WITH_ABSTRACT_TYPE);

        PageObjectWithPageObjectDeclaredWithAbstractType pageWithAbstractType = new PageObjectWithPageObjectDeclaredWithAbstractType();

        enricher.enrich(pageWithAbstractType);
    }

    @Test
    public void testWrongPageObjectMissongNoArgConstructor() {
        thrown.expect(PageObjectInitializationException.class);
        thrown.expectMessage(ERROR_MSG_PAGE_OBJECT_WITH_MISSING_NO_ARG_CONSTRUCTOR);

        PageObjectWithPageObjectMissingNoArgConstructor pageWithMissingNoArgConstr = new PageObjectWithPageObjectMissingNoArgConstructor();

        enricher.enrich(pageWithMissingNoArgConstr);
    }

    @Test
    public void testInitializingPageObjectImplementedWithPrivateConstructor() {
        thrown.expect(PageObjectInitializationException.class);
        thrown.expectMessage(ERROR_MSG_PAGE_OBJECT_WITH_PRIVATE_CONSTRUCTOR);

        enricher.enrich(new PageObjectWithPageObjectWithPrivateConstructor());
    }

    public class PageObjectWithPageObjectWithPrivateConstructor {

        @SuppressWarnings("unused")
        @Page
        private WrongPageObjectPrivateConstructor wrongPageObject;

        public class WrongPageObjectPrivateConstructor {

            private WrongPageObjectPrivateConstructor() {

            }
        }
    }

    public class PageObjectWithPageObjectMissingNoArgConstructor {
        @SuppressWarnings("unused")
        @Page
        private WrongPageObject wrongPage;

        public class WrongPageObject {

            public WrongPageObject(int foo) {

            }
        }
    }

    public class PageObjectWithPageObjectDeclaredWithAbstractType {
        @SuppressWarnings("unused")
        @Page
        private AbstractPageType wrongPage;

    }

    public interface AbstractPageType {

    }

}
