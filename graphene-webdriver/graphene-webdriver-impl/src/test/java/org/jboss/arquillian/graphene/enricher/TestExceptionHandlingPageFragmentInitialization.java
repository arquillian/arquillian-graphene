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

import org.jboss.arquillian.graphene.enricher.exception.PageFragmentInitializationException;
import org.jboss.arquillian.graphene.enricher.fragment.AbstractPageFragmentStub;
import org.jboss.arquillian.graphene.enricher.fragment.WrongPageFragmentMissingNoArgConstructor;
import org.jboss.arquillian.graphene.enricher.fragment.WrongPageFragmentPrivateConstructor;
import org.jboss.arquillian.graphene.enricher.fragment.WrongPageFragmentTooManyRoots;
import org.junit.Test;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
public class TestExceptionHandlingPageFragmentInitialization extends AbstractTest {

    private static final String ERROR_MSG_TOO_MUCH_ROOTS = "can not have more than one field annotated with Root annotation!";
    private static final String ERROR_MSG_DECLARING_PAGE_FRAGMENT_WITHOUT_ROOT_REFERENCE = "in other words without reference to root of the particular Page Fragment on the page!";
    private static final String ERROR_MSG_DECLARING_PAGE_FRAGMENT_WITH_ABSTRACT_TYPE = "Check whether you did not declare Page Fragment with abstract type!";
    private static final String ERROR_MSG_DECLARING_PAGE_FRAGMENT_WITH_PRIVATE_CONSTRUCTOR = "Check whether declared Page Fragment has public no argument constructor!";

    @Test
    public void testWrongFragmentDeclaredWithTooManyRoots() {
        thrown.expect(PageFragmentInitializationException.class);
        thrown.expectMessage(ERROR_MSG_TOO_MUCH_ROOTS);

        enricher.enrich(new PageObjectWithPageFragmentImplementedWithTooManyRoots());
    }

    @Test
    public void testInitPageFragmentWithNullReferenceToRoot() {
        thrown.expect(PageFragmentInitializationException.class);
        thrown.expectMessage(ERROR_MSG_DECLARING_PAGE_FRAGMENT_WITHOUT_ROOT_REFERENCE);

        enricher.enrich(new PageObjectWithPageFragmentDeclaredWithNullRootReference());
    }

    @Test
    public void testInitPageFragmentDeclaredWithAbstractType() {
        thrown.expect(PageFragmentInitializationException.class);
        thrown.expectMessage(ERROR_MSG_DECLARING_PAGE_FRAGMENT_WITH_ABSTRACT_TYPE);

        enricher.enrich(new PageObjectWithPageFragmentDeclaredWithAbstractType());
    }

    @Test
    public void testInitPageFragmentImplementedWithoutNoArgConstructor() {
        thrown.expect(PageFragmentInitializationException.class);
        thrown.expectMessage(ERROR_MSG_DECLARING_PAGE_FRAGMENT_WITH_ABSTRACT_TYPE);

        enricher.enrich(new PageObjectWithPageFragmentImplementedWithoutNoArgConstr());
    }

    @Test
    public void testInitPageFragmentImplementedWithPrivateConstructor() {
        thrown.expect(PageFragmentInitializationException.class);
        thrown.expectMessage(ERROR_MSG_DECLARING_PAGE_FRAGMENT_WITH_PRIVATE_CONSTRUCTOR);

        enricher.enrich(new PageObjectWithPageFragmentPrivateConstructor());
    }

    public class PageObjectWithPageFragmentImplementedWithoutNoArgConstr {
        @SuppressWarnings("unused")
        @FindBy(id = "foo")
        private WrongPageFragmentMissingNoArgConstructor pageFragment;
    }

    public class PageObjectWithPageFragmentDeclaredWithAbstractType {
        @SuppressWarnings("unused")
        @FindBy(id = "foo")
        private AbstractType pageFragment;
    }

    public class PageObjectWithPageFragmentPrivateConstructor {
        @SuppressWarnings("unused")
        @FindBy(id = "foo")
        private WrongPageFragmentPrivateConstructor pageFragment;
    }

    public interface AbstractType {

    }

    public class PageObjectWithPageFragmentDeclaredWithNullRootReference {
        @SuppressWarnings("unused")
        @FindBy
        private AbstractPageFragmentStub pageFragment;
    }

    public class PageObjectWithPageFragmentImplementedWithTooManyRoots {
        @SuppressWarnings("unused")
        @FindBy(id = "foo")
        private WrongPageFragmentTooManyRoots pageFragment;
    }

}
