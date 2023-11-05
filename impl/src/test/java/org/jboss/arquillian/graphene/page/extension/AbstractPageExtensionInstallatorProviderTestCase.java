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
package org.jboss.arquillian.graphene.page.extension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;
import org.jboss.arquillian.graphene.spi.javascript.JavaScript;
import org.jboss.arquillian.graphene.spi.page.PageExtension;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class AbstractPageExtensionInstallatorProviderTestCase {
    @Mock
    private PageExtensionRegistry registry;

    @Before
    public void prepareRegistry() {
        when(registry.getExtension(WithCycleA.class.getName())).thenReturn(new WithCycleA());
        when(registry.getExtension(WithCycleB.class.getName())).thenReturn(new WithCycleB());
        when(registry.getExtension(WithoutCycleA.class.getName())).thenReturn(new WithoutCycleA());
        when(registry.getExtension(WithoutCycleB.class.getName())).thenReturn(new WithoutCycleB());
    }

    @Test(expected=IllegalStateException.class)
    public void testCycleInRequirements() {
        PageExtensionInstallatorProvider provider = new TestedPageExtensionInstallatorProvider(registry);
        provider.installator(WithCycleA.class.getName());
    }

    @Test
    public void testNoCycleInRequirements() {
        try {

        } catch(IllegalStateException e) {
            Assert.fail("False positive cycle has been detected. " + e.getMessage());
        }
    }

    private static class TestedPageExtensionInstallatorProvider extends AbstractPageExtensionInstallatorProvider {

        TestedPageExtensionInstallatorProvider(PageExtensionRegistry registry) {
            super(registry);
        }

        @Override
        public PageExtensionInstallator createInstallator(PageExtension extension) {
            return null;
        }

    }

    private static class WithCycleA implements PageExtension {

        @Override
        public JavaScript getExtensionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public Collection<String> getRequired() {
            List<String> result = new ArrayList<String>();
            result.add(WithCycleB.class.getName());
            return result;
        }

        @Override
        public JavaScript getInstallationDetectionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public String getName() {
            return getClass().getName();
        }

    }

    private static class WithCycleB implements PageExtension {

        @Override
        public JavaScript getExtensionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public Collection<String> getRequired() {
            List<String> result = new ArrayList<String>();
            result.add(WithCycleA.class.getName());
            return result;
        }

        @Override
        public JavaScript getInstallationDetectionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public String getName() {
            return getClass().getName();
        }

    }

    private static class WithoutCycleA implements PageExtension {

         @Override
        public JavaScript getExtensionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public Collection<String> getRequired() {
            List<String> result = new ArrayList<String>();
            result.add(WithoutCycleB.class.getName());
            return result;
        }

        @Override
        public JavaScript getInstallationDetectionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public String getName() {
            return getClass().getName();
        }

    }

    private static class WithoutCycleB implements PageExtension {

        @Override
        public JavaScript getExtensionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public Collection<String> getRequired() {
            return Collections.EMPTY_LIST;
        }

        @Override
        public JavaScript getInstallationDetectionScript() {
            return JavaScript.fromString("");
        }

        @Override
        public String getName() {
            return getClass().getName();
        }

    }

}
