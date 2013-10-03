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
package org.jboss.arquillian.graphene.ftest.issues;

import static org.junit.Assert.assertEquals;

import java.net.URL;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.drone.api.annotation.Drone;
import org.jboss.arquillian.graphene.GrapheneElement;
import org.jboss.arquillian.graphene.fragment.Root;
import org.jboss.arquillian.graphene.ftest.Resource;
import org.jboss.arquillian.graphene.ftest.Resources;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

/**
 * @author <a href="mailto:jhuska@redhat.com">Juraj Huska</a>
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ARQGRA345 {

    @ArquillianResource
    private URL contextRoot;

    @Drone
    private WebDriver browser;

    @FindBy(id = "root")
    private PageFragmentWithGrapheneElementAsRoot fragment;

    private static final String SAMPLE_PACKAGE = "org.jboss.arquillian.graphene.ftest.enricher";

    @Deployment
    public static WebArchive createTestArchive() {
        return Resources.inPackage(SAMPLE_PACKAGE).all().buildWar("test.war");
    }

    @Before
    public void loadPage() {
        Resource.inPackage(SAMPLE_PACKAGE).find("sample.html").loadPage(browser, contextRoot);
    }

    @Test
    public void graphene_element_should_be_able_to_be_used_as_root_of_page_fragment() {
        assertEquals(fragment.getInnerElement().getText(), "pseudo root");
    }

    public class PageFragmentWithGrapheneElementAsRoot {

        @Root
        private GrapheneElement root;

        @FindBy(id = "pseudoroot")
        private GrapheneElement innerElement;

        public GrapheneElement getInnerElement() {
            return innerElement;
        }

        public GrapheneElement getRoot() {
            return root;
        }
    }
}