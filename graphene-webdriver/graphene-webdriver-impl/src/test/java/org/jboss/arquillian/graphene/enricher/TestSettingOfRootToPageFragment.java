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

import org.jboss.arquillian.graphene.enricher.fragment.AbstractPageFragmentStub;
import org.jboss.arquillian.graphene.enricher.fragment.WrongPageFragmentTooManyRoots;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mockito;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * @author Juraj Huska
 */
public class TestSettingOfRootToPageFragment {

    @SuppressWarnings("unused")
    @FindBy
    private AbstractPageFragmentStub pageFragmentStub;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final String errorMsgTooMuchRoots = "can not have more than one field annotated with Root annotation!";
    private final String errorMsgPassingNullRoot = "Non of the parameters can be null!";
    private final String errorMsgDeclaringPageFragmentWithoutRootReference = "in other words without reference to root of the particular Page Fragment on the page!";

    @Test
    public void testWrongFragmentDeclaredWithTooManyRoots() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsgTooMuchRoots);

        WebElement root = Mockito.mock(WebElement.class);
        Factory.initializePageFragment(WrongPageFragmentTooManyRoots.class, root);
    }

    @Test
    public void testInitPageFragmentWithNullRoot() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsgPassingNullRoot);

        Factory.initializePageFragment(AbstractPageFragmentStub.class, null);
    }

    @Test
    public void testInitPageFragment() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(errorMsgDeclaringPageFragmentWithoutRootReference);

        PageFragmentsEnricher enricher = new PageFragmentsEnricher();

        enricher.enrich(this);
    }
}
