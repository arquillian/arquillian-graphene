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

import junit.framework.Assert;
import org.jboss.arquillian.graphene.enricher.exception.PageFragmentInitializationException;
import org.jboss.arquillian.graphene.enricher.fragment.AbstractPageFragmentStub;
import org.jboss.arquillian.graphene.enricher.fragment.WrongPageFragmentMissingNoArgConstructor;
import org.jboss.arquillian.graphene.enricher.fragment.WrongPageFragmentTooManyRoots;
import org.junit.Test;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestPageFragmentEnricher extends AbstractGrapheneEnricherTest {

    @Test
    public void testMissingNoArgConstructor() {
        NoArgConstructorTest target = new NoArgConstructorTest();
        getGrapheneEnricher().enrich(target);
        Assert.assertNull(target.pageFragment);
    }


    @Test
    public void testTooManyRoots() {
        thrown.expect(PageFragmentInitializationException.class);
        getGrapheneEnricher().enrich(new TooManyRootsTest());
    }

    @Test
    public void testAbstractType() {
        AbstractTypeTest target = new AbstractTypeTest();
        getGrapheneEnricher().enrich(target);
        Assert.assertNull(target.pageFragment);
    }

    public static class NoArgConstructorTest {
        @FindBy(id="blah")
        private WrongPageFragmentMissingNoArgConstructor pageFragment;
    }

    public static class TooManyRootsTest {
        @SuppressWarnings("unused")
        @FindBy(id = "foo")
        private WrongPageFragmentTooManyRoots pageFragment;
    }

    public static class AbstractTypeTest {
        @SuppressWarnings("unused")
        @FindBy(id = "foo")
        private AbstractType pageFragment;
    }

    public abstract class AbstractType {

    }
}
