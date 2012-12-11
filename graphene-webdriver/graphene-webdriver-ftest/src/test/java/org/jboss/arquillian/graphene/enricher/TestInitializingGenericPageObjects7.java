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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jboss.arquillian.graphene.enricher.page.TestPage;
import org.jboss.arquillian.graphene.enricher.page.TestPage2;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@RunWith(Arquillian.class)
public class TestInitializingGenericPageObjects7 extends EvenMoreSpecificAbstractTest2 {

    @Test
    public void testInitializingGenericPageObjectsInDeeperAbstractClassHierarchy() {
        assertNotNull("Page1 should be initialized!", getPage1());
        assertNotNull("Page2 should be initialized!", getPage2());

        assertTrue("Page1 class is wrong!", getPage1().getClass().equals(TestPage.class));
        assertTrue("Page2 class is wrong!", getPage2().getClass().equals(TestPage2.class));
    }
}
