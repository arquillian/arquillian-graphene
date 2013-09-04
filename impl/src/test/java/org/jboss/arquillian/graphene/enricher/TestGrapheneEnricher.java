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
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
public class TestGrapheneEnricher extends AbstractGrapheneEnricherTest {

    @Test
    public void testNotNullAfterEnrichment() {
        CorrectTest correctTest = new CorrectTest();
        getGrapheneEnricher().enrich(correctTest);
        Assert.assertNotNull(correctTest.pageFragment);
        Assert.assertNotNull(correctTest.pageFragment.getLocatorRefByClassName());
        Assert.assertNotNull(correctTest.pageFragment.getLocatorRefByCssSelector());
        Assert.assertNotNull(correctTest.pageFragment.getLocatorRefById());
        Assert.assertNotNull(correctTest.pageFragment.getLocatorRefByLinkText());
        Assert.assertNotNull(correctTest.pageFragment.getLocatorRefByName());
        Assert.assertNotNull(correctTest.pageFragment.getLocatorRefByPartialLinkText());
        Assert.assertNotNull(correctTest.pageFragment.getLocatorRefByTagName());
        Assert.assertNotNull(correctTest.pageFragment.getLocatorRefByXPath());
        Assert.assertNotNull(correctTest.pageFragment.getRootProxy());
        Assert.assertNotNull(correctTest.pageFragment.getSpansInPageFragment());
    }



    public static class CorrectTest {
        @FindBy(id="blah")
        private AbstractPageFragmentStub pageFragment;
    }

}
