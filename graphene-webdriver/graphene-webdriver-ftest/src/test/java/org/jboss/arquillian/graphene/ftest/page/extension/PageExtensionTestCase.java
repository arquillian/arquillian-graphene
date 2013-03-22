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
package org.jboss.arquillian.graphene.ftest.page.extension;

import static org.mockito.Mockito.when;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.Graphene;
import org.jboss.arquillian.graphene.GrapheneContext;
import org.jboss.arquillian.graphene.page.extension.PageExtensionRegistry;
import org.jboss.arquillian.graphene.spi.javascript.JavaScript;
import org.jboss.arquillian.graphene.spi.page.PageExtension;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.openqa.selenium.WebDriver;

/**
 * @author <a href="mailto:jpapouse@redhat.com">Jan Papousek</a>
 */
@RunWith(Arquillian.class)
public class PageExtensionTestCase {

    @Drone
    private WebDriver browser;

    @ArquillianResource
    private GrapheneContext context;

    public void loadPage() {
        URL page = this.getClass().getClassLoader().getResource("org/jboss/arquillian/graphene/ftest/page/extension/sample.html");
        browser.get(page.toString());
    }

    @Test
    public void testCorrectInstallation() {
        loadPage();
        // page extension construction
        PageExtension pageExtensionMock = Mockito.mock(PageExtension.class);
        when(pageExtensionMock.getExtensionScript()).thenReturn(JavaScript.fromString("Document.Graphene = {};"));
        when(pageExtensionMock.getInstallationDetectionScript()).thenReturn(JavaScript.fromString("return (typeof Document.Graphene != 'undefined');"));
        when(pageExtensionMock.getRequired()).thenReturn(Collections.EMPTY_LIST);
        // registry
        PageExtensionRegistry registry = context.getPageExtensionRegistry();
        registry.register(pageExtensionMock);
        // test
        context.getPageExtensionInstallatorProvider().installator(pageExtensionMock.getName()).install();
    }

    @Test(expected=IllegalStateException.class)
    public void testIncorrectInstallation() {
        loadPage();
        // page extension construction
        PageExtension pageExtensionMock = Mockito.mock(PageExtension.class);
        when(pageExtensionMock.getExtensionScript()).thenReturn(JavaScript.fromString("var Graphene = {};"));
        when(pageExtensionMock.getInstallationDetectionScript()).thenReturn(JavaScript.fromString("return (typeof Graphene != 'undefined');"));
        when(pageExtensionMock.getRequired()).thenReturn(Collections.EMPTY_LIST);
        // registry
        PageExtensionRegistry registry = context.getPageExtensionRegistry();
        registry.register(pageExtensionMock);
        // test
        context.getPageExtensionInstallatorProvider().installator(pageExtensionMock.getName()).install();
    }

    @Test
    public void testInstallationWithRequirements() {
        loadPage();
        // page extension construction
        PageExtension pageExtensionMock = Mockito.mock(PageExtension.class);
        when(pageExtensionMock.getExtensionScript()).thenReturn(JavaScript.fromString("Document.Graphene2 = Document.Graphene1 + 1;"));
        when(pageExtensionMock.getInstallationDetectionScript()).thenReturn(JavaScript.fromString("return ((typeof Document.Graphene2 != 'undefined') && Document.Graphene2 == 2);"));
        List<String> requirements = new ArrayList<String>();
        requirements.add(SimplePageExtension.class.getName());
        when(pageExtensionMock.getRequired()).thenReturn(requirements);
        // registry
        PageExtensionRegistry registry = context.getPageExtensionRegistry();
        registry.register(new SimplePageExtension());
        registry.register(pageExtensionMock);
        // test
        context.getPageExtensionInstallatorProvider().installator(pageExtensionMock.getName()).install();
        Assert.assertTrue(context.getPageExtensionInstallatorProvider().installator(SimplePageExtension.class.getName()).isInstalled());
        Assert.assertTrue(context.getPageExtensionInstallatorProvider().installator(pageExtensionMock.getName()).isInstalled());
    }

    @Test(expected=IllegalStateException.class)
    public void testInstallationWithCyclicRequirements() {
        loadPage();
        // page extension construction
        PageExtension pageExtensionMock = Mockito.mock(PageExtension.class);
        when(pageExtensionMock.getExtensionScript()).thenReturn(JavaScript.fromString("Document.Graphene2 = Document.Graphene1 + 1;"));
        when(pageExtensionMock.getInstallationDetectionScript()).thenReturn(JavaScript.fromString("return ((typeof Document.Graphene2 != 'undefined') && Document.Graphene2 == 2);"));
        List<String> requirements = new ArrayList<String>();
        requirements.add(SimplePageExtension.class.getName());
        requirements.add(CyclicPageExtension1.class.getName());
        requirements.add(CyclicPageExtension2.class.getName());
        when(pageExtensionMock.getRequired()).thenReturn(requirements);
        // registry
        PageExtensionRegistry registry = context.getPageExtensionRegistry();
        registry.register(new SimplePageExtension());
        registry.register(pageExtensionMock);
        registry.register(new CyclicPageExtension1());
        registry.register(new CyclicPageExtension2());
        // test
        context.getPageExtensionInstallatorProvider().installator(pageExtensionMock.getName()).install();
    }

    private static class SimplePageExtension implements PageExtension {

        @Override
        public JavaScript getExtensionScript() {
            return JavaScript.fromString("Document.Graphene1 = 1;");
        }

        @Override
        public JavaScript getInstallationDetectionScript() {
            return JavaScript.fromString("return ((typeof Document.Graphene1 != 'undefined') && Document.Graphene1 == 1);");
        }

        @Override
        public Collection<String> getRequired() {
            return Collections.EMPTY_LIST;
        }

        @Override
        public String getName() {
            return getClass().getName();
        }

    }

    private static class CyclicPageExtension1 implements PageExtension {

        @Override
        public JavaScript getExtensionScript() {
            return JavaScript.fromString("Document.Cyclic1 = 1;");
        }

        @Override
        public JavaScript getInstallationDetectionScript() {
            return JavaScript.fromString("return ((typeof Document.Cyclic1 != 'undefined') && Document.Cyclic1 == 1);");
        }

        @Override
        public Collection<String> getRequired() {
            List<String> required = new ArrayList<String>();
            required.add(CyclicPageExtension2.class.getName());
            return required;
        }

        @Override
        public String getName() {
            return getClass().getName();
        }

    }

    private static class CyclicPageExtension2 implements PageExtension {

        @Override
        public JavaScript getExtensionScript() {
            return JavaScript.fromString("Document.Cyclic2 = 1;");
        }

        @Override
        public JavaScript getInstallationDetectionScript() {
            return JavaScript.fromString("return ((typeof Document.Cyclic2 != 'undefined') && Document.Cyclic2 == 1);");
        }

        @Override
        public Collection<String> getRequired() {
            List<String> required = new ArrayList<String>();
            required.add(CyclicPageExtension1.class.getName());
            return required;
        }

        @Override
        public String getName() {
            return getClass().getName();
        }

    }
}
